var isHealthInfoActivated = false;

function onGetHealthInfo(healthinfo)
{
	var kcalinfo = healthinfo.cumulativeCalorie;
	
	if(isSAPValidate === true)
	{
		try
		{
			g_SAPStringSock.sendData(g_SAAgent.channelIds[0],kcalinfo.toString());
		}catch(e)
		{
			console.log(e);
			isSAPValidate = false;
			mainPeerstatus.innerHTML = msg_sapunavailable;
		}
	}
}

function onChangeHealInfoSeneorStatus()
{
	if(isHealthInfoActivated === false)
	{
		isHealthInfoActivated = true;
		webapis.motion.start("PEDOMETER",onPedometerInfo);
	}
	else
	{
		isHealthInfoActivated = false;
		webapis.motion.stop("PEDOMETER");
	}
}

myevent.listeners({
	'healthinfo.changehealthinfostatus': onChangeHealInfoSeneorStatus
});