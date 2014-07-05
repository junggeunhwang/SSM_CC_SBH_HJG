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
var queueprefix = "q"; // 사용자 큐를 의미하는 키에 붙이는 접미사
var roomexistingprefix = "r"; // 사용자 방이 있는지 여부를 체크하는 키에 붙이는 접미사
var roomprefix = "rm"; // 사용자 방을 의미하는 리스트 키에 붙이는 접미사

// ttl 관련

var expiredTime = 15; // 기본 15초


function HttpEventProcessCallback(req,res)
{
	// http request from port 8010
	
	res.writeHead(200, {'Content-Type': 'text/plain'});
	res.end('wrong request');
}

function renewExpireTime(uniqueNumber)
{
	// 구현
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
				if(userList[i] !== uniqueNumber)
				{
					imdb.rpush(queueId,postStr);
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
				res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
				res.end("ENOTLOGIN");
			}			
		}
		else
		{
			var userRoomexistId = roomexistingprefix+uniqueNumber;
			var userQueueId = queueprefix+uniqueNumber;
			// 방이 있는지 여부
			imdb.exists(userRoomexistId,function(err,reply){
				if(reply === 0)
				{
					// 방이 없음
					if(isDirect === true)
					{
						res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
						res.end("ENOTJOINROOM");
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
											res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
											res.end("SUCCESS");
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

function ProcessLogoutRequest(res, uniqueNumber)
{
	// 로그인 여부 확인
	imdb.exists(uniqueNumber,function(err,reply){
		if(reply === 0)
		{
			// 로그인 하지 않음
			res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
			res.end("ENOTLOGIN");
		}
		else
		{
			// 로그인 키 삭제 및 로그인 리스트 삭제
			// 리스트에서 삭제
			
			imdb.lrem(loginkey,"0",uniqueNumber,function(err,reply){
				// 로그인 키 삭제
				imdb.del(uniqueNumber,function(err,reply){
					// 로그아웃 성공
					res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
					res.end("SUCCESS");
				});
			});
		}
	});
}

function ProcessPostRequest(res,err,fields,files)
{
	var uniqueNumber = fields.uniqueNumber.toString();
	var type = fields.type.toString();
	
	console.log("uniqueNumber : " + uniqueNumber);
	console.log("type : " + type);
	// 잘못된 요청
	if(uniqueNumber === undefined || type === undefined)
	{
		res.writeHead(200, {'Content-Type': 'text/plain'});
		res.end('wrong request');
	}
	else
	{
		if(type === "string" || type === "file")
		{
			// 로그인 여부
			imdb.exists(uniqueNumber,function(err,reply){
				if(reply === 0)
				{
					res.writeHead(200, {'type': 'text', 'src' : '01012345678'});
			    	res.end("ENOTLOGIN");
				}
				else
				{
					// 방 있는지 여부
					var userRoomexistId = roomexistingprefix+uniqueNumber;
					
					imdb.exists(userRoomexistId,function(err,reply){
						if(reply === 0)
						{
							// 방이 없음
							res.writeHead(200, {'type': 'text', 'src' : '01012345678'});
					    	res.end("ENOTJOINROOM");
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
								var filedata = files.data[0];
								var receivedPath = filedata.path;
								var filename = receivedPath.replace(/^.*[\\\/]/, '');
								
								postData = filename;
								
								console.log("received path : "+filedata.path);
								console.log("file name : "+filename);    			
							}
							postMulticastData(uniqueNumber, type , postData);
							res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
					    	res.end('SUCCESS');
						}
					});
				}
			});
		}
		else if(type === "request")
		{
			// 요청 데이터 프로세싱
			var typedata = fields.data.toString();
			var typeextradata = fields.extradata;
			
			if(typedata === "get")
			{
				// 로그인 검사
				// 방 포함 검사
				// 큐 검사
				
				imdb.exists(uniqueNumber,function(err,reply){
					// 로그인 확인
					if(reply === 0)
					{
						res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
				    	res.end('ENOTLOGIN');
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
								res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
								res.end('ENODATA');
							}
							else
							{
								var multicastedObj = JSON.parse(result);
								
								console.log(multicastedObj);
								
								if(multicastedObj.type === "string")
								{
									// 이 경우 단순 스트링 전송
									res.writeHead(200, {'type': 'string', 'src' : multicastedObj.uniqueNumber});
									res.end(multicastedObj.data);
								}
								else if(multicastedObj.type === "file")
								{
									res.writeHead(200, {'type': 'file', 'src' : multicastedObj.uniqueNumber});
									var readStream = fs.createReadStream('tempfile/'+multicastedObj.data);
									
									readStream.on('data', function(data) {
								        res.write(data);
								    });
								    
								    readStream.on('end', function() {
								        res.end();        
								    });
								}
								else
								{
									res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
									res.end('EUNEXPECTED');
								}
							}
						});
					}
				});
				
				// file or string transfer
				/*res.writeHead(200, {'type': 'file', 'src' : '01012345678'});
				var readStream = fs.createReadStream('sample.amr');
				
				readStream.on('data', function(data) {
			        res.write(data);
			    });
			    
			    readStream.on('end', function() {
			        res.end();        
			    });*/
				//res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
				//res.end("success");
				
			}
			else if(typedata === "mkroom")
			{
				console.log("mkroom request");
				
				// 로그인 체크
				
				imdb.exists(uniqueNumber,function(err,reply)
						{
							if(reply === 0)
							{
								// 로그인 하지 않음
								res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
								res.end("ENOTLOGIN");
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
														console.log("room "+roomId+" users : "+reply);
														res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
														res.end("SUCCESS");
													});
										});
									}
									else
									{
										// 이미 방이 있음
										res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
										res.end("EALREADYJOINROOM");
									}
								});
							}
						});			
				
			}
			else if(typedata === "joinroom")
			{
				console.log("joinroom request");
				// 로그인 체크
				
				imdb.exists(uniqueNumber,function(err,reply)
				{
					if(reply === 0)
					{
						// 로그인 하지 않음
						res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
						res.end("ENOTLOGIN");
					}
					else
					{
						// 방 여부 체크
						var userRoomexistId = roomexistingprefix+uniqueNumber; 
						imdb.exists(userRoomexistId,function(err,reply){
							if(reply === 0)
							{
								// 대상 여부 체크
								if(typeextradata === undefined)
								{
									// 대상이 없음
									res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
									res.end("ENOTARGET");
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
														res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
														res.end("SUCCESS");
															});
												});	
											});																				
										}
										else
										{
											// 방이 없음
											res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
											res.end("ENOTARGET");
										}
									});
								}
							}
							else
							{
								// 이미 방이 있음
								res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
								res.end("EALREADYJOINROOM");
							}
						});
					}
				});
			}
			else if(typedata === "exitroom")
			{
				console.log("exitroom request");
				
				// 작업 처리
				ProcessExitRoomRequest(res, uniqueNumber,true);				
			}
			else if(typedata === "login")
			{
				// 로그인 요청 처리
				console.log("login request");
				
				// 키가 있는지 확인
				imdb.exists(uniqueNumber,function(err,reply){
					if(reply === 0)
					{
						// 로그인 요청 처리
						imdb.set(uniqueNumber,"login",function(err,reply){
							// 로그인 큐에 추가
							imdb.rpush(loginkey,uniqueNumber,function(err,reply){
								// 로그인 성공
								res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
								res.end("SUCCESS");
								console.log("login queue : "+reply);
							});													
						});
					}
					else
					{
						// 로그인 실패
						res.writeHead(200, {'type': 'text', 'src' : uniqueNumber});
						res.end("EALREADYLOGIN");
					}
				});
			}
			else if(typedata === "logout")
			{
				console.log("logout request");
				
				// 먼저 방이 있으면 나간다.
				ProcessExitRoomRequest(res, uniqueNumber,false);
				// 그다음 로그아웃
				ProcessLogoutRequest(res, uniqueNumber);
			}
			else
			{
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
	console.log("https request arriving!\r\n");
	
	if (req.method === 'POST')
	{
		// use multiparty module
		var form = new multiparty.Form();

		form.uploadDir = 'D:/node.js/workspace/MulticastServer/tempfile';

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
	console.log("checkTTL called");
	imdb.lrange(loginkey,"0","-1",function(err,reply){
		console.log(reply);
	});
	
	imdb.get
}

httpServer = http.createServer(HttpEventProcessCallback);
httpsServer = https.createServer(options,HttpsEventProcessCallback);

httpServer.listen(port);
httpsServer.listen(sslport);

console.log("server started");

setInterval(checkTTL, 4000);