var g_SAPStringSock;

function SAPStringInit()
{
	function onReceive(channelId,data)
	{
		/*console.log(channelId + " "  + data);
		alert(data);*/
		
		var msg = data.toString();
		
		var msginfo = msg.split(';');
		
		if(msginfo === undefined)
		{
			// just ignore
			return;
		}
		
		var type = msginfo[0];
		
		if(type === "fitninfo")
		{
			if(msginfo.length !== 6)
			{
				// ignore;
			}
			else
			{
				var maxspeed = msginfo[1];
				var avgspeed = msginfo[2];
				var distance = msginfo[3];
				var altitude = msginfo[4];
				var kcal = msginfo[5];
				
				mainMaxspeed.innerHTML = maxspeed+" km/h";
				mainAvgspeed.innerHTML = avgspeed+" km/h";
				mainDistance.innerHTML = distance+" km";
				mainAltitude.innerHTML = altitude+" m";
				
				secondKcalinfo.innerHTML = kcal+" kcal";
			}
		}
		else if(type === "alertmsg")
		{
			if(msginfo.length!==3)
			{
				//ignore
			}
			else
			{
				var msg = msginfo[1];
				var sender = msginfo[2];
				
				var data = {};
				
				data.type = "alert";
				data.message = msg;
				data.sender = sender;
				
				multicastedqueue.push(data);
			}
		}
		else
		{
			// invalid msg
		}
	}
	var connectioncallback = { 
			/* when a remote peer agent requests a service connection */ 
			onrequest : function(peerAgent)
				{ 
					g_SAAgent.acceptServiceConnectionRequest(peerAgent);
				}, 
				/* when the connection between provider and consumer is established */ 
			onconnect : function(socket) 
				{
				// 원치 않는 소켓인 경우 해제
					/*if(socket.peerAgent.appName !== ANDROID_APP_NAME)
					{ 
						try
						{ 
							socket.close(); 
						}catch(e)
						{ 
							console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
						} 
					}
					else*/
					{
						// 성공~
						g_SAPStringSock = socket;
						try 
						{ 
							g_SAPStringSock.setDataReceiveListener(onReceive);
						}catch(e) 
						{ 
							console.log("Error Exception, error name : " + e.name + ", error message : " + e.message);
							alert("FATAL ERROR\r\n"+e.name+"\r\nPlease Try Again Later");
							tizen.application.getCurrentApplication().exit();
						}
						// 여기서는 이제 sap 사용가능
						isSAPValidate = true;
						console.log("isSAPValidate = true");
						mainPeerstatus.innerHTML = msg_sapavailable;
					}
				}, 
				/* when an error occurs during connect and request operations */ 
			onerror : function(errorCode) 
				{ 
					console.log("Service connection error. : " + errorCode); 
					
					if(errorCode.toString() === "INVALID_PEERAGENT" )
					{
						mainPeerstatus.innerHTML = msg_peerinvalid;
					}else if(errorCode.toString() === "PEERAGENT_NO_RESPONSE")
					{
						mainPeerstatus.innerHTML = msg_peernoresponse;
					}else if(errorCode.toString() === "PEERAGENT_REJECTED")
					{
						mainPeerstatus.innerHTML = msg_peernotrunning;
					}
				} 
	}; 
	try { 
		g_SAAgent.setServiceConnectionListener(connectioncallback); 
	} catch (e) 
	{ 
		console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
	} 
}
