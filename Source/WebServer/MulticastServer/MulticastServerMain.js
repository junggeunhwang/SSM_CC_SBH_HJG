/**
 * New node file
 */
var http = require('http');
var https = require('https');
var fs = require('fs');
var crypto = require('crypto');
var multiparty = require('multiparty');
var util = require('util');
var redis = require('redis');

// in memory database
var imdb = redis.createClient();

var httpServer;
var httpsServer;

// https verification options
var options = {
  key: fs.readFileSync('privatekey.pem'),
  cert: fs.readFileSync('certrequest.pem')
};

// http port
var port = 8010;
// https port
var sslport = 8020;

// imdb 관련
var loginkey = "loginkey";
var userkey = "userkey";
var queueprefix = "q"; // 사용자 큐를 의미하는 키에 붙이는 접미사
var roomexistingprefix = "r"; // 사용자 방이 있는지 여부를 체크하는 키에 붙이는 접미사
var roomprefix = "rm"; // 사용자 방을 의미하는 리스트 키에 붙이는 접미사
var friendlistprefix = "frl"; // 친구 목옥을 의미하는 키 접미사
var expiredKeyprefix = "exp"; // 만료 키에 붙이는 접미사

// 개인 고유 키 필드 네임
var hfname = "name";
var hfimage = "image";
var hflogin = "login";

// ttl 관련

var expiredTime = "25"; // 기본 15초


function HttpEventProcessCallback(req,res)
{
	// http request from port 8010
	
	res.writeHead(200, {'Content-Type': 'text/plain'});
	res.end('wrong request');
}

function renewExpireTime(uniqueNumber)
{
	// 구현
	var expiredUserId = expiredKeyprefix+uniqueNumber;
	
	imdb.set(expiredUserId,"expired",function(err,reply){
		imdb.expire(expiredUserId,expiredTime,function(err,reply){
			// 작업 완료
		});
	});
}


// postMulticastData

function postMulticastData(uniqueNumber,datatype,datainfo)
{
	// 이 시점에 로그인이랑 방 검사는 모두 완료됨	
	/*
	 * 멀티캐스트 할 데이터는 크게 2가지로 나뉨,
	 * 1. 파일
	 * 2. 스트링
	 * 
	 * 스트링일 경우는 해당 데이터를 그대로 큐에 저장
	 * 파일일 경우는 파일 이름을 저장
	 * 저장 방법은 이 정보를 보낸 uniqueNumber, datatype,datainfo 로 구성*/
	
	var postObj = {};
	postObj.uniqueNumber = uniqueNumber;
	postObj.type = datatype;
	postObj.data = datainfo;
	
	var postStr = JSON.stringify(postObj);
	//var target = JSON.parse(postStr);
	
	// 방 식별자를 얻기
	var userRoomexistId = roomexistingprefix + uniqueNumber;
	
	imdb.get(userRoomexistId,function(err,reply){
		// 방 식별자 
		var roomId = reply;
		// 방에 있는 모든 유저 정보를 얻어옴
		imdb.lrange(roomId,"0","-1",function(err,reply){
			var userList = reply;
			
			// 순회하며 각 사용자 큐에 삽입
			for(var i = 0;i<userList.length;i++)
			{
				var queueId = queueprefix + userList[i];
				// 자기 자신일 경우 삽입하지 않음
				if(datainfo === "JOINROOM")
				{
					imdb.rpush(queueId,postStr);
				}
				else
				{
					if(userList[i] !== uniqueNumber)
					{
						if(datatype !== "file")
						{
							imdb.rpush(queueId,postStr);
						}
						else
						{
							imdb.lpush(queueId,postStr);
						}
					}
				}
			}
		});
	});
	
	//console.log(postStr);
	//console.log(target);
}

// exitroom 작업의 처리
function ProcessExitRoomRequest(res,uniqueNumber,isDirect)
{
	// 로그인 검사
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// 로그인 하지 않음
			if(isDirect === true)
			{
				res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'exitroom'});
				res.end("ENOTLOGIN");
				console.log("-status : ENOTLOGIN");
			}			
		}
		else
		{
			// 로그인 상태가 false라면
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// 로그인 하지 않음
					// 로그인 하지 않음
					if(isDirect === true)
					{
						res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'exitroom'});
						res.end("ENOTLOGIN");
						console.log("-status : ENOTLOGIN");
					}
				}
				else
				{
					// 아니면 로그인
					var userRoomexistId = roomexistingprefix+uniqueNumber;
					var userQueueId = queueprefix+uniqueNumber;
					// 방이 있는지 여부
					imdb.exists(userRoomexistId,function(err,reply){
						if(reply === 0)
						{
							// 방이 없음
							if(isDirect === true)
							{
								res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'exitroom'});
								res.end("ENOTJOINROOM");
								console.log("-status : ENOTJOINROOM");
							}
						}
						else
						{
							imdb.get(userRoomexistId,function(err,reply){
								// 해당 방의 리스트에서 사용자 제거
								var roomId = reply;
								// reply는 방 리스트의 키
								imdb.lrem(roomId,"0",uniqueNumber,function(err,reply){
									// 방 리스트의 크기가 0이 아니면 방의 사용자에게 나갔다고 알림
									imdb.llen(roomId,function(err,reply){
										if(reply !== 0)
										{
											postMulticastData(uniqueNumber, "string", "EXITROOM");
										}
										// 사용자의 멀티캐스트 수신 큐 제거
										imdb.del(userQueueId,function(err,reply){
											// 사용자의 방 식별 키 제거
											imdb.del(userRoomexistId,function(err,reply){
												// 방 탈출 성공
												if(isDirect === true)
												{
													res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'exitroom'});
													res.end("SUCCESS");
													console.log("-status : SUCCESS");
												}
											});
										});								
									});					
								});
							});					
						}
					});
				}
			});
		}
	});
}

function ProcessLogoutRequest(res, uniqueNumber,isResponseRequired)
{
	// 로그인 여부 확인
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// 로그인 하지 않음
			if(isResponseRequired === true)
			{
				res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'logout'});
				res.end("ENOTLOGIN");
				console.log("-status : ENOTLOGIN");
			}
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// 로그인 하지 않음.
					if(isResponseRequired === true)
					{
						res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'logout'});
						res.end("ENOTLOGIN");
						console.log("-status : ENOTLOGIN");
					}
				}
				else
				{
					// 로그인 리스트 삭제
					// 리스트에서 삭제
					imdb.lrem(loginkey,"0",uniqueNumber,function(err,reply){
						// 로그인 status를 false로 변경
						imdb.hset(uniqueNumber,hflogin,"false",function(err,reply){
							// 키 죽음
							var expiredkey = expiredKeyprefix + uniqueNumber;
							
							imdb.del(expiredkey,function(err,reply){
								// 로그아웃 성공
								if(isResponseRequired === true)
								{
									res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':'logout'});	
									res.end("SUCCESS");
									console.log("-status : SUCCESS");
								}
							});							
						});
					});
				}
			});			
		}
	});
}

function ProcessInviteroomRequest(res,uniqueNumber,targetNumber,typedata)
{
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// user not login
			res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
			res.end("ENOTLOGIN");
			console.log("-status : ENOTLOGIN");
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// user not login
					res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
					res.end("ENOTLOGIN");
					console.log("-status : ENOTLOGIN");
					
				}
				else
				{
					var userRoomexistId = roomexistingprefix + uniqueNumber;
					var targetRoomexistId = roomexistingprefix + targetNumber;
					
					imdb.exists(userRoomexistId,function(err,reply){
						if(reply === 0)
						{
							// 요청한 사용자가 방에 있는 상태가 아님
							res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
							res.end("ENOTJOINROOM");
							console.log("-status : ENOTJOINROOM");
						}
						else
						{
							imdb.exists(targetNumber,function(err,reply){
								if(reply === 0)
								{
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("ETARGETNOTLOGIN");
									console.log("-status : ETARGETNOTLOGIN");
								}
								else
								{
									imdb.hget(targetNumber,hflogin,function(err,loginstatus){
										if(loginstatus === "false")
										{
											res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
											res.end("ETARGETNOTLOGIN");
											console.log("-status : ETARGETNOTLOGIN");
										}
										else
										{
											imdb.exists(targetRoomexistId,function(err,reply){
												// 상대가 방에 참여중인 경우
												if(reply === 1)
												{
													res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
													res.end("ETARGETALREADYJOINROOM");
													console.log("-status : ETARGETALREADYJOINROOM");
												}
												else
												{
													// 방에 참여 가능
													// 해당 사용자의 방을 얻어옴
													imdb.get(userRoomexistId,function(err,reply){
														var roomId = reply;
														console.log("room "+roomId+" exists!");
														
														// 대상 사용자를 방에 추가
														imdb.set(targetRoomexistId,roomId,function(err,reply){
															// 현 사용자를 방 리스트에 추가
															imdb.rpush(roomId,targetNumber,function(err,reply)
																	{
																// 방에 추가 성공
																postMulticastData(targetNumber, "string", "JOINROOM");
																console.log("current room"+roomId+" users : "+reply);
																res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
																res.end("SUCCESS");
																console.log("-status : SUCCESS");
																	});
														});	
													});
												}
											});
										}
									});
								}
							});
						}
					});
				}
			});
		}
	});
}

function ProcessAddfriendRequest(res,uniqueNumber,targetNumber,typedata)
{
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// user not login
			res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
			res.end("ENOTLOGIN");
			console.log("-status : ENOTLOGIN");
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// user not login
					res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
					res.end("ENOTLOGIN");
					console.log("-status : ENOTLOGIN");
				}
				else
				{
					imdb.exists(targetNumber,function(err,reply){
						if(reply === 0)
						{
							// target not login
							res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
							res.end("ENOTARGET");
							console.log("-status : ENOTARGET");
						}
						else
						{
							// 여기서 친구추가를 시도한다.
							var userfriendSetId = friendlistprefix+uniqueNumber;
							var targetfriendSetId = friendlistprefix+targetNumber;
							
							/*imdb.sadd(userfriendSetId,targetNumber,function(err,reply){
								if(reply === 0)
								{
									// 친구 중복
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("EALREADYFRIEND");
									console.log("-status : EALREADYFRIEND");
								}
								else
								{
									// 대상 친구 목록에도 나를 추가
									imdb.sadd(targetfriendSetId,uniqueNumber,function(err,reply){
										// 여기서 중복은 엄밀히 발생하지 않음
										// 성공
										res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
										res.end("SUCCESS");
										console.log("-status : SUCCESS");
									});
								}
							});*/
							
							imdb.hexists(userfriendSetId,targetNumber,function(err,reply){
								if(reply === 1)
								{
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("EALREADYFRIEND");
									console.log("-status : EALREADYFRIEND");
								}
								else
								{
									var myname,targetname;
									// 친구 이름 얻기
									imdb.hget(targetNumber,hfname,function(err,name){
										targetname = name;
										
										// 내 친구 목록에 추가
										
										imdb.hset(userfriendSetId,targetNumber,targetname,function(err,reply){
											
											// 상대방 친구 목록에도 추가
											
											// 내 이름 얻기
											
											imdb.hget(uniqueNumber,hfname,function(err,name){
												myname = name;
												
												imdb.hset(targetfriendSetId,uniqueNumber,myname,function(err,reply){
													// 성공
													res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
													res.end("SUCCESS");
													console.log("-status : SUCCESS");
												});
											});
										});
									});
								}								
							});
						}
					});
				}
			});
		}
	});
}

function ProcessDelfriendRequest(res,uniqueNumber,targetNumber,typedata)
{
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// user not login
			res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
			res.end("ENOTLOGIN");
			console.log("-status : ENOTLOGIN");
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// user not login
					res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
					res.end("ENOTLOGIN");
					console.log("-status : ENOTLOGIN");
				}
				else
				{
					imdb.exists(targetNumber,function(err,reply){
						if(reply === 0)
						{
							// target not login
							res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
							res.end("ENOTARGET");
							console.log("-status : ENOTARGET");
						}
						else
						{
							// 여기서 친구삭제를 시도한다.
							var userfriendSetId = friendlistprefix+uniqueNumber;
							var targetfriendSetId = friendlistprefix+targetNumber;
							
							/*imdb.srem(userfriendSetId,targetNumber,function(err,reply){
								if(reply === 0)
								{
									// 친구가 아님
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("ENOTFRIEND");
									console.log("-status : ENOTFRIEND");
								}
								else
								{
									// 대상 친구 목록에서도 나를 제거
									imdb.srem(targetfriendSetId,uniqueNumber,function(err,reply){
										// 여기서 중복은 엄밀히 발생하지 않음
										// 성공
										res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
										res.end("SUCCESS");
										console.log("-status : SUCCESS");
									});
								}
							});*/
							
							imdb.hexists(userfriendSetId,targetNumber,function(err,reply){
								if(reply === 0)
								{
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("ENOTFRIEND");
									console.log("-status : ENOTFRIEND");
								}
								else
								{
									// 내 목록에서 제거
									
									imdb.hdel(userfriendSetId,targetNumber,function(err,reply){
										// 친구 목록에서도 제거
										
										imdb.hdel(targetfriendSetId,uniqueNumber,function(err,reply){
											res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
											res.end("SUCCESS");
											console.log("-status : SUCCESS");
										});
									});
								}
							});
						}
					});
				}
			});
		}
	});
}

function ProcessFriendlistRequest(res,uniqueNumber,targetNumber,typedata)
{
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// user not login
			// user not login
			res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
			res.end("ENOTLOGIN");
			console.log("-status : ENOTLOGIN");
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// user not login
					res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
					res.end("ENOTLOGIN");
					console.log("-status : ENOTLOGIN");
				}
				else
				{
					var userFriendSetId = friendlistprefix + uniqueNumber;
					
					imdb.exists(userFriendSetId,function(err,reply){
						if(reply === 0)
						{
							// no friendlist
							// user not login
							res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
							res.end("ENOLIST");
							console.log("-status : ENOLIST");
						}
						else
						{
							var responseValue = "";
							var friendcount;
							
							imdb.hlen(userFriendSetId,function(err,reply){
								friendcount = reply;
								
								imdb.hgetall(userFriendSetId,function(err,reply){
									var friends = reply;
									var i;
									
									responseValue+=friendcount;
									responseValue+=";";
									
									imdb.hkeys(userFriendSetId,function(err,reply){
										var keys = reply;
										
										for(i=0;i<friendcount;i++)
										{
											responseValue+= keys[i].toString() +";"+ friends[keys[i]].toString() + ";";
										}
										

										res.writeHead(200, {'type': 'string', 'src' : "NONE",'order':typedata});
										res.end(responseValue);
									});
									
									console.log("-status : SUCCESS");
								});
								
								// 모든걸 가져옴
							});
							
							/*imdb.smembers(userFriendSetId,function(err,reply){
								var i = 0;
								for(;i<reply.length;i++)
								{
									responseValue+=reply[i].toString() + ";";
								}
								
								res.writeHead(200, {'type': 'string', 'src' : targetNumber,'order':typedata});
								res.end(responseValue);
								console.log("-status : SUCCESS");
							});*/
						}
					});
				}
			});
		}
	});
}

function ProcessGetnameRequest(res,uniqueNumber,targetNumber,typedata)
{
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// user not login
			// user not login
			res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
			res.end("ENOTLOGIN");
			console.log("-status : ENOTLOGIN");
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// user not login
					res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
					res.end("ENOTLOGIN");
					console.log("-status : ENOTLOGIN");
				}
				else
				{
					imdb.exists(targetNumber,function(err,reply){
						if(reply === 0)
						{
							// target not login at least once
							res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
							res.end("ENOTARGET");
							console.log("-status : ENOTARGET");
						}
						else
						{
							imdb.hget(targetNumber,hfname,function(err,reply){
								var targetName = reply;
								
								if(targetName === "")
								{
									// target not register name
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("ENONAME");
									console.log("-status : ENONAME");
								}
								else
								{
									// user not login
									res.writeHead(200, {'type': 'string', 'src' : targetNumber,'order':typedata});
									res.end(targetName);
									console.log("-status : SUCCESS");
								}
							});
						}						
					});
				}
			});
		}
	});
}

function ProcessGetimageRequest(res,uniqueNumber,targetNumber,typedata)
{
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// user not login
			// user not login
			res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
			res.end("ENOTLOGIN");
			console.log("-status : ENOTLOGIN");
		}
		else
		{
			imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
				if(loginstatus === "false")
				{
					// user not login
					res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
					res.end("ENOTLOGIN");
					console.log("-status : ENOTLOGIN");
				}
				else
				{
					imdb.exists(targetNumber,function(err,reply){
						if(reply === 0)
						{
							// target not login at least once
							res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
							res.end("ENOTARGET");
							console.log("-status : ENOTARGET");
						}
						else
						{
							imdb.hget(targetNumber,hfimage,function(err,reply){
								var imagePath = reply;
								
								if(imagePath === "")
								{
									// target not register name
									res.writeHead(200, {'type': 'text', 'src' : targetNumber,'order':typedata});
									res.end("ENOIMAGE");
									console.log("-status : ENONAME");
								}
								else
								{
									res.writeHead(200, {'type': 'file', 'src' : targetNumber,'order':typedata});
									// success
									var readStream = fs.createReadStream('tempfile/'+imagePath);
									
									readStream.on('data', function(data) {
								        res.write(data);
								    });
								    
								    readStream.on('end', function() {
								        res.end();
								        console.log("-status : Send File data successfully.");
								    });
								}
							});
						}						
					});
				}
			});
		}
	});
}

function ProcessPostRequest(res,err,fields,files)
{
	if(fields === undefined)
	{
		// 이 경우는 무시
		console.log("essential property is missing!");
		console.log(fields);
		
		res.writeHead(200, {'type' : 'text','src' : 'NONE','order' : 'null'});
		res.end('EMISSING');
		console.log("-status : EMISSING");
		return;
	}
	// 잘못된 요청
	if(fields.hasOwnProperty('uniqueNumber') === false || fields.hasOwnProperty('type') === false)
	{
		console.log("essential property is missing!");
		console.log(fields);
		
		res.writeHead(200, {'type' : 'text','src' : 'NONE','order' : 'null'});
		res.end('EMISSING');
		console.log("-status : EMISSING");
	}
	else
	{
		var uniqueNumber = fields.uniqueNumber;
		var type = fields.type;
		
		console.log("uniqueNumber : " + uniqueNumber);
		console.log("type : " + type);
		
		uniqueNumber = uniqueNumber.toString();
		type = type.toString();
		
		if(type === "string" || type === "file")
		{
			console.log("Register Multicasting Data. Type : "+type);
			// 로그인 여부
			imdb.exists(uniqueNumber,function(err,reply){
				if(reply === 0)
				{
					// 키가 없어도 로그인 상태가 아님
					res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':type});
			    	res.end("ENOTLOGIN");
			    	console.log("-status : ENOTLOGIN");
				}
				else
				{
					// 로그인 상태가 false이어도 로그인 아님
					imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
						if(loginstatus === "false")
						{
							res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':type});
					    	res.end("ENOTLOGIN");
					    	console.log("-status : ENOTLOGIN");
						}
						else
						{
							var userRoomexistId = roomexistingprefix+uniqueNumber;
							
							imdb.exists(userRoomexistId,function(err,reply){
								if(reply === 0)
								{
									// 방이 없음
									res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':type});
							    	res.end("ENOTJOINROOM");
							    	console.log("-status : ENOTJOINROOM");
								}
								else
								{
									// 멀티캐스트
									var postData;
									if(type === "string")
									{
										// 스트링 멀티캐스트
										postData = fields.data[0].toString();
										
										console.log("received string data : "+postData);
									}
									else
									{
										// 파일 멀티캐스트
										// 파일이 없는 경우
										if(files.hasOwnProperty('data') === false)
										{
											console.log("essential property is missing!");
											console.log(fields);
											
											res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : type});
											res.end('EMISSING');
											console.log("-status : EMISSING");
											return;
										}
										var filedata = files.data[0];
										var receivedPath = filedata.path;
										var filename = receivedPath.replace(/^.*[\\\/]/, '');
										
										postData = filename;
										
										console.log("received path : "+filedata.path);
										console.log("file name : "+filename);    			
									}
									postMulticastData(uniqueNumber, type , postData);
									res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':type});
							    	res.end('SUCCESS');
							    	console.log("-status : SUCCESS");
								}
							});
						}
					});
				}
			});
		}
		else if(type === "request")
		{
			// 요청 데이터 프로세싱
			if(fields.hasOwnProperty('data') === false)
			{
				// console.log("essential property is missing!");
				console.log(fields);
				
				res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
				res.end('EMISSING');
				console.log("-status : EMISSING");
				return;
			}
			var typedata = fields.data.toString();
			//var typeextradata = fields.extradata;
			console.log("request type : "+typedata);
			if(typedata === "get")
			{
				// 로그인 검사
				// 방 포함 검사
				// 큐 검사				
				imdb.exists(uniqueNumber,function(err,reply){
					// 로그인 확인
					if(reply === 0)
					{
						res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
				    	res.end('ENOTLOGIN');
				    	console.log("-status : ENOTLOGIN");
					}
					else
					{
						imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
							if(loginstatus === "false")
							{
								// status 가 false여도 로그인 아님
								res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
						    	res.end('ENOTLOGIN');
						    	console.log("-status : ENOTLOGIN");
							}
							else
							{
								// 이 시점에서 만료시간 갱신
								var userQueueId = queueprefix + uniqueNumber;
								renewExpireTime(uniqueNumber);
								imdb.lpop(userQueueId,function(err,reply){
																
									var result = reply;
									
									if(result === null)
									{
										res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
										res.end('ENODATA');
										console.log("-status : ENODATA");
									}
									else
									{
										var multicastedObj = JSON.parse(result);
										
										console.log(multicastedObj);
										
										if(multicastedObj.type === "string")
										{
											// 이 경우 단순 스트링 전송
											res.writeHead(200, {'type': 'string', 'src' : multicastedObj.uniqueNumber,'order':typedata});
											res.end(multicastedObj.data);
											console.log("-status : Send String data successfully.");
										}
										else if(multicastedObj.type === "file")
										{
											res.writeHead(200, {'type': 'file', 'src' : multicastedObj.uniqueNumber,'order':typedata});
											var readStream = fs.createReadStream('tempfile/'+multicastedObj.data);
											
											readStream.on('data', function(data) {
										        res.write(data);
										    });
										    
										    readStream.on('end', function() {
										        res.end();
										        console.log("-status : Send File data successfully.");
										    });
										}
										else
										{
											res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
											res.end('EUNEXPECTED');
											console.log("-status : EUNEXPECTED");
										}
									}
								});
							}
						});
					}
				});				
			}
			else if(typedata === "mkroom") // 방만들기
			{
				// 로그인 체크				
				imdb.exists(uniqueNumber,function(err,reply)
						{
							if(reply === 0)
							{
								// 로그인 하지 않음
								res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
								res.end("ENOTLOGIN");
								console.log("-status : ENOTLOGIN");
							}
							else
							{
								// 로그인 상태 체크
								imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
									if(loginstatus === "false")
									{
										// 로그인 상태 아님
										res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
										res.end("ENOTLOGIN");
										console.log("-status : ENOTLOGIN");
									}
									else
									{
										var userRoomexistId = roomexistingprefix+uniqueNumber;
										// 방 여부 체크
										imdb.exists(userRoomexistId,function(err,reply){
											if(reply === 0)
											{
												// 방을 만듬
												var roomId = roomprefix + uniqueNumber + new Date().getTime(); 
												imdb.set(userRoomexistId,roomId,function(err,reply){
													console.log("room "+roomId+" created!");
													// 현 사용자를 방 리스트에 추가
													imdb.rpush(roomId,uniqueNumber,function(err,reply)
															{
																postMulticastData(uniqueNumber, "string", "JOINROOM");
																console.log("room "+roomId+" users : "+reply);
																res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
																res.end("SUCCESS");
																console.log("-status : SUCCESS");
															});
												});
											}
											else
											{
												// 이미 방이 있음
												res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
												res.end("EALREADYJOINROOM");
												console.log("-status : EALREADYJOINROOM");
											}
										});
									}
								});
							}
						});			
				
			}
			else if(typedata === "joinroom")
			{
				// 로그인 체크
				// joinroom은 extradata 필요
				if(fields.hasOwnProperty('extradata') === false)
				{
					// 없으면 미싱임
					console.log("essential property is missing!");
					console.log(fields);
					
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var typeextradata = fields.extradata;
				imdb.exists(uniqueNumber,function(err,reply)
				{
					if(reply === 0)
					{
						// 로그인 하지 않음
						res.writeHead(200, {'type': 'text', 'src' : typeextradata,'order':typedata});
						res.end("ENOTLOGIN");
						console.log("-status : ENOTLOGIN");
					}
					else
					{
						// 방 여부 체크
						imdb.hget(uniqueNumber,function(err,loginstatus){
							if(loginstatus === "false")
							{
								// 로그인 아님
								res.writeHead(200, {'type': 'text', 'src' : typeextradata,'order':typedata});
								res.end("ENOTLOGIN");
								console.log("-status : ENOTLOGIN");
							}
							else
							{
								var userRoomexistId = roomexistingprefix+uniqueNumber; 
								imdb.exists(userRoomexistId,function(err,reply){
									if(reply === 0)
									{
										// 대상 여부 체크
										if(typeextradata === undefined)
										{
											// 대상이 없음
											res.writeHead(200, {'type': 'text', 'src' : typeextradata,'order':typedata});
											res.end("ENOTARGET");
											console.log("-status : ENOTARGET");
										}
										else
										{
											var targetuserId = typeextradata.toString();
											var targetRoomexistId = roomexistingprefix+targetuserId;
											// 방을 검색
											imdb.exists(targetRoomexistId,function(err,reply){
												if(reply === 1)
												{
													// 해당 사용자의 방을 얻어옴
													imdb.get(targetRoomexistId,function(err,reply){
														var roomId = reply;
														console.log("room "+roomId+" exists!");
														
														// 현 사용자를 방에 추가
														imdb.set(userRoomexistId,roomId,function(err,reply){
															// 현 사용자를 방 리스트에 추가
															imdb.rpush(roomId,uniqueNumber,function(err,reply)
																	{
																console.log("current room"+roomId+" users : "+reply);
																res.writeHead(200, {'type': 'text', 'src' : typeextradata,'order':typedata});
																res.end("SUCCESS");
																console.log("-status : SUCCESS");
																	});
														});	
													});																				
												}
												else
												{
													// 방이 없음
													res.writeHead(200, {'type': 'text', 'src' : typeextradata,'order':typedata});
													res.end("ENOTARGET");
													console.log("-status : ENOTARGET");
												}
											});
										}
									}
									else
									{
										// 이미 방이 있음
										res.writeHead(200, {'type': 'text', 'src' : typeextradata,'order':typedata});
										res.end("EALREADYJOINROOM");
										console.log("-status : EALREADYJOINROOM");
									}
								});
							}
						});
					}
				});
			}
			else if(typedata === "exitroom")
			{
				//나서스 20분 400스택
				// 작업 처리
				ProcessExitRoomRequest(res, uniqueNumber,true);				
			}
			else if(typedata === "login")
			{
				
				// 키가 있는지 확인
				imdb.exists(uniqueNumber,function(err,reply){					
					
					// 해시 셋이 없는 경우
					if(reply === 0)
					{
						// 로그인 요청 처리 새 해시셋을 만듬
						imdb.hmset(uniqueNumber,hfname,"",hfimage,"",hflogin,"true",function(err,reply){
							// 로그인 큐에 추가
							imdb.rpush(loginkey,uniqueNumber,function(err,reply){
								// 로그인 성공
								// 유저 큐에 추가
								imdb.hset(userkey,uniqueNumber,"",function(err,reply){
									renewExpireTime(uniqueNumber);
									res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
									res.end("SUCCESS");
									console.log("login queue : "+reply);
									console.log("-status : SUCCESS");
								});
							});	
						});
					}
					else
					{
						// 로그인 요청 처리
						imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
							if(loginstatus === "true")
							{
								// 이미 로그인
								// 로그인 실패
								res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
								res.end("EALREADYLOGIN");
								console.log("-status : EALREADYLOGIN");
								
							}
							else
							{
								// loginstatus를 true로 세팅
								imdb.hset(uniqueNumber,hflogin,"true",function(err,loginstatus){
									// 로그인 큐에 추가
									imdb.rpush(loginkey,uniqueNumber,function(err,reply){
										// 성공
										renewExpireTime(uniqueNumber);
										res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
										res.end("SUCCESS");
									});	
								});
							}
						});						
					}
				});
			}
			else if(typedata === "logout")
			{
				console.log("logout request");
				
				// 먼저 방이 있으면 나간다.
				ProcessExitRoomRequest(res, uniqueNumber,false);
				// 그다음 로그아웃
				ProcessLogoutRequest(res, uniqueNumber,true);
			}
			else if(typedata === "inviteroom")
			{
				// 유효한 파라미터가 없음
				if(fields.hasOwnProperty('extradata') ===  false)
				{
					// 실패
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var targetNumber = fields.extradata;
				
				ProcessInviteroomRequest(res, uniqueNumber, targetNumber,typedata);
			}
			else if(typedata === "addfriend")
			{
				// 유효한 파라미터가 없음
				if(fields.hasOwnProperty('extradata') ===  false)
				{
					// 실패
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var targetNumber = fields.extradata;
				
				ProcessAddfriendRequest(res, uniqueNumber, targetNumber, typedata);
			}
			else if(typedata === "delfriend")
			{
				// 유효한 파라미터가 없음
				if(fields.hasOwnProperty('extradata') ===  false)
				{
					// 실패
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var targetNumber = fields.extradata;
				ProcessDelfriendRequest(res, uniqueNumber, targetNumber, typedata);
			}
			else if(typedata === "friendlist")
			{
				var targetNumber = "";
				
				ProcessFriendlistRequest(res, uniqueNumber, targetNumber, typedata);
			}
			else if(typedata === "getname")
			{
				// 유효한 파라미터가 없음
				if(fields.hasOwnProperty('extradata') ===  false)
				{
					// 실패
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var targetNumber = fields.extradata;
				
				ProcessGetnameRequest(res, uniqueNumber, targetNumber, typedata);
			}
			else if(typedata === "getimage")
			{
				// 유효한 파라미터가 없음
				if(fields.hasOwnProperty('extradata') ===  false)
				{
					// 실패
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var targetNumber = fields.extradata;
	    		
	    		ProcessGetimageRequest(res, uniqueNumber, targetNumber, typedata);
			}
			else if(typedata === "update")
			{
				imdb.exists(uniqueNumber,function(err,reply){
					if(reply === 0)
					{
						res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
						res.end("ENOTLOGIN");
						console.log("-status : ENOTLOGIN");
					}
					else
					{
						imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
							if(loginstatus === "false")
							{
								res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
								res.end("ENOTLOGIN");
								console.log("-status : ENOTLOGIN");
							}
							else
							{
								var isNameMod = false;
								var isImageMod = false;
								var filedata, receivedPath, filename="", userName="";
								
								if(files.hasOwnProperty('data')  === true)
								{
									// 사진 데이터 존재
									isImageMod = true;
									filedata = files.data[0];
									receivedPath = filedata.path;
									filename = receivedPath.replace(/^.*[\\\/]/, '');
									
								}
								
								if(fields.hasOwnProperty('extradata') === true)
								{
									// 이름 존재
									isNameMod = true;
									userName = fields.extradata[0].toString();
								}
								
								if(isImageMod === true)
								{
									var previmagefilePath;
									imdb.hget(uniqueNumber,hfimage,function(err,reply){
										// 이전 경로를 저장
										previmagefilePath = 'tempfile/'+reply.toString();
										var isDeleted = false;
										
										if(reply.toString() !== "")
										{
											isDeleted = true;
										}
										// 새 경로 설정
										imdb.hset(uniqueNumber,hfimage,filename,function(err,reply){
											//이전 경로의 파일 삭제
											// console.log(previmagefilePath);
											if(isDeleted === true)
											{
												fs.unlinkSync(previmagefilePath);
											}
											if(isNameMod === true)
											{
												console.log(userName);
												// 이름도 바뀌는 경우
												imdb.hset(uniqueNumber,hfname,userName,function(err,reply){
													// 성공
													// userkey의 이름도 변경
													imdb.hset(userkey,uniqueNumber,userName,function(err,reply){
														res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
														res.end("SUCCESS");
														// console.log("login queue : "+reply);
														 console.log("-status : SUCCESS");
													});													
												});
											}
											else
											{
												// 성공
												res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
												res.end("SUCCESS");
												console.log("-status : SUCCESS");
											}
										});
									});
									
								}
								else
								{
									if(isNameMod === true)
									{
										console.log(userName);
										imdb.hset(uniqueNumber,hfname,userName,function(err,reply){
											// 성공
											// userkey의 이름도 변경
											imdb.hset(userkey,uniqueNumber,userName,function(err,reply){
												res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
												res.end("SUCCESS");
												// console.log("login queue : "+reply);
												 console.log("-status : SUCCESS");
											});	
										});
									}
									else
									{
										res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
										res.end("ENOUPDATE");
										console.log("-status : NOUPDATE");
									}
								}
							}
						});
					}
				});
			}
			else if(typedata === "search")
			{
				// 유효한 파라미터가 없음
				if(fields.hasOwnProperty('extradata') ===  false)
				{
					// 실패
					res.writeHead(200, {'type' : 'text','src' : "NONE",'order' : typedata});
					res.end('EMISSING');
					console.log("-status : EMISSING");
					return;
				}
				var searchName = fields.extradata.toString();
	    		
	    		imdb.exists(uniqueNumber,function(err,reply){
	    			if(reply === 0)
	    			{
	    				// 로그인 하지 않음
	    				res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
						res.end("ENOTLOGIN");
						console.log("-status : ENOTLOGIN");
	    			}
	    			else
	    			{
	    				imdb.hget(uniqueNumber,hflogin,function(err,loginstatus){
	    					if(loginstatus === "false")
	    					{
	    						res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
								res.end("ENOTLOGIN");
								console.log("-status : ENOTLOGIN");
	    					}
	    					else
	    					{
	    						var userList;
	    						var response;
	    						var matchedcount = 0;
	    						var totalcount;
	    						var keys;
	    						
	    						// 모든 유저의 리스트를 받아옴
	    						
	    						imdb.hgetall(userkey,function(err,reply){
	    							userList = reply;
	    							
	    							// 전체 유저의 수를 받아옴
	    							imdb.hlen(userkey,function(err,reply){
	    								totalcount = reply;
	    								
	    								// 전체 키를 받아옴
	    								imdb.hkeys(userkey,function(err,reply){
	    									keys = reply;
	    									
	    									var subList="";
	    									var i;
	    									// 찾기
	    									
	    									for(i=0;i<totalcount;i++)
	    									{
	    										var userName = userList[keys[i]].toString();
	    										
	    										if(userName === searchName)
	    										{
	    											// 이름이 같으면
	    											matchedcount++;
	    											
	    											// 서브 리스트에 추가
	    											
	    											subList+=keys[i].toString()+";"+userName + ";";
	    										}
	    									}
	    									
	    									response = matchedcount.toString() + ";" + subList;
	    									
	    									if(matchedcount === 0)
	    									{
	    										res.writeHead(200, {'type': 'text', 'src' : "NONE",'order':typedata});
												res.end("ENOSEARCHDATA");
												console.log("-status : ENOSEARCHDATA");
	    									}
	    									else
	    									{
	    										// 반환
		    									res.writeHead(200, {'type': 'string', 'src' : "NONE",'order':typedata});
												res.end(response);
												console.log("-status : SUCCESS");
	    									}	    									
	    								});
	    								
	    							});
	    						});
	    					}
	    				});
	    			}
	    		});
			}
			else
			{
				// 정의되지 않은 요청
				res.writeHead(200, {'Content-Type': 'text/plain'});
	    		res.end('wrong request');
			}
		}
		else
		{
			// 정의되지 않은 요청
			res.writeHead(200, {'Content-Type': 'text/plain'});
    		res.end('wrong request');
		}
	}
}

// functions
function HttpsEventProcessCallback(req,res)
{
	//console.log(req);
	var date = new Date();
	console.log("\r\nhttps request arriving!" + date.toUTCString()+"\r\n");
	
	if (req.method === 'POST')
	{
		// use multiparty module
		var form = new multiparty.Form();

		form.uploadDir = 'C:/eclipse/SSM Project/MulticastServer/tempfile';

	    form.parse(req, function(err, fields, files){
	    	ProcessPostRequest(res, err, fields, files);
	    });
    }
	else
	{
		// 포스트가 아니면 에러
		res.writeHead(200, {'Content-Type': 'text/plain'});
		res.end('wrong request');
	}
}

function checkTTL()
{

	imdb.lrange(loginkey,"0","-1",function(err,reply){
		//console.log(reply);
		var i;
		for(i=0;i<reply.length;i++)
		{
			var expiredKeyId = expiredKeyprefix+reply[i];
			var uniqueNumber = reply[i];
			
			// 이 키가 존재하지 않으면 로그아웃 된 것으로 판명
			
			imdb.exists(expiredKeyId,function(err,reply){
				if(reply === 0)
				{
					// logout!
					console.log("expired key detected,"+uniqueNumber+ " logout");
					ProcessExitRoomRequest("null", uniqueNumber, false);
					ProcessLogoutRequest("null", uniqueNumber, false);
				}
			});
		}
	});
}

//httpServer = http.createServer(HttpEventProcessCallback);
httpsServer = https.createServer(options,HttpsEventProcessCallback);

//httpServer.listen(port);
httpsServer.listen(sslport);

console.log("server started");

setInterval(checkTTL, 4000);