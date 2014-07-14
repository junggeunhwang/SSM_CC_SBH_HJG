var g_SAPStringSock;

function SAPStringInit()
{
	function onReceive(channelId,data)
	{
		/*console.log(channelId + " "  + data);
		alert(data);*/
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
					mainPeerstatus.innerHTML = msg_peernoresponse;
				} 
	}; 
	try { 
		g_SAAgent.setServiceConnectionListener(connectioncallback); 
	} catch (e) 
	{ 
		console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
	} 
}
