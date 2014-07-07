var g_SAAgent;
var g_peerAgent;
var ANDROID_APP_NAME = "SAPInterface";
var isSAPValidate = false;
var peerConnectionTimer;
//var processingQueue = [];

function SAPInit()
{
	var peeragentfindcallback = { 
			onpeeragentfound : onpeeragentfound, 
			onpeeragentupdated : onpeeragentupdated, 
			onerror : onerror 
	};
	
	function onPeerConnectionTimer()
	{
		if(isSAPValidate === false)
		{
			g_SAAgent.findPeerAgents();
		}
		else
		{
			try
			{
				g_SAPStringSock.sendData(g_SAAgent.channelIds[0],"KEEPALIVE");
			}catch(e)
			{
				console.log(e);
				isSAPValidate = false;
				mainPeerstatus.innerHTML = msg_sapunavailable;
			}		
		}
	}

	// init g_SAAgent
	function onsuccess(agents) {
		g_SAAgent = agents[0];
		for ( var i = 0; i < agents.length; i++) 
		{
			console.log(i + ". " + agents[i].name);
			/* Process the SA Agents */
		}
		try 
		{
			SAPStringInit();
			g_SAAgent.setPeerAgentFindListener(peeragentfindcallback); 
			g_SAAgent.findPeerAgents();
			
			// 3초간 재시도
			peerConnectionTimer = new mytimer.Timer(3000,onPeerConnectionTimer);
			peerConnectionTimer.run();
			
		} catch(e) 
		{ 
			console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
		}
		SAPFileInit();
	}

	function onRequestError(e) 
	{
		console.log("Error name: " + e.name + ", Error message : " + e.message);
		console.log("FAIL TO INIT SAP");
	}
	try 
	{
		webapis.sa.requestSAAgent(onsuccess, onRequestError);
	} catch (e) {
		console.log("Error Exception, error name : " + e.name
				+ ", error message : " + e.message);
	}

	function ondevicestatus(type, status)
	{
		// 이는 디바이스가 호스트 디바이스랑 블루투스로 연결되었을 때 호출되는 콜벡매서드
		if (status === "ATTACHED") 
		{
			console.log("Attached remote peer device. : " + type);
			g_SAAgent.findPeerAgents();
		}
		else if (status === "DETACHED") 
		{
			console.log("Detached remote peer device. : " + type);
			isSAPValidate = false;
		}
	} 
	try 
	{ 
		webapis.sa.setDeviceStatusListener(ondevicestatus); 
	}catch(e) 
	{ 
		console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
	}
	
	// init g_peerAgent
	function onpeeragentfound(peerAgent) 
	{
		//console.log(peerAgent.appName);
		if(peerAgent.appName === ANDROID_APP_NAME) 
		{ 
			mainPeerstatus.innerHTML = msg_peerfound;
			g_SAAgent.requestServiceConnection(peerAgent); 
			g_peerAgent = peerAgent;
		} 
	} 
	function onpeeragentupdated(peerAgent, status) { 
		if(status == "AVAILABLE") 
		{ 
			try
			{ 
				g_SAAgent.requestServiceConnection(peerAgent); 
				g_peerAgent = peerAgent;
			} catch(e) 
			{ 
				console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
			} 
		}
		else
		{ 
			//console.log("Uninstalled application package of peerAgent on remote device.");
			alert('어플리케이션의 상태가 변경되었습니다.\r\n'+status);
		} 
	} 

	function onerror(errorCode) { 
		console.log("Error code : " + errorCode); 
		if(errorCode === "PEER_NOT_FOUND") 
		{ 
			//alert('연결된 디바이스에서 실행중인 적절한 어플리케이션을 찾을 수 없습니다. \r\n'+errorCode);
			mainPeerstatus.innerHTML = msg_peernotfound;
		}else if(errorCode === "DEVICE_NOT_CONNECTED")
		{
			//alert('디바이스와 연결되어있지 않습니다. 이 어플리케이션은 디바이스와 연결되어있어야 합니다.\r\n'+errorCode);
			mainPeerstatus.innerHTML = msg_devicedetached;
		}
	} 	
}