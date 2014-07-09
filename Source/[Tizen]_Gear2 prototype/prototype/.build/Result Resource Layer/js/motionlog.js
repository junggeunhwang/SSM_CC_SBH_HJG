(function motionlogInit()
{
	var firElem, secElem, thirElem;
	var LogStartStopButton, DeleteLogButton, AccelLogOpenButton;
	var NormalLogStartButton;
	var logFileName = "accel_log.txt";
	var NormallogFileName = "accel_normal_log.txt";
	// 가속도 값 저장되는 디렉터리, 유효 가속도 값 저장되는 위치, 비유효 가속도 값 저장되는 위치
	var accelLogDir,accelLogPath,accelNormalLogPath;
	var writePeriod = 47;
	var isAccelLogStart = false;
	var isNormalLogStart = false;
	
	firElem = document.getElementById("firElem");
	secElem = document.getElementById("secElem");
	
	//motion log
	// deviceMotion
	function onAcceleration (e) {
		var ax,ay,az,gx,gy,gz;
		ax = e.acceleration.x.toFixed(1);
		ay = e.acceleration.y.toFixed(1);
		az = e.acceleration.z.toFixed(1);
		gx = e.accelerationIncludingGravity.x.toFixed(1);
		gy = e.accelerationIncludingGravity.y.toFixed(1);
		gz = e.accelerationIncludingGravity.z.toFixed(1);
	
	    firElem.innerHTML = 'acceleration value' + 'x : ' + ax + 'y : ' + ay + 'z : ' + az;
	    secElem.innerHTML = 'accelerationIncludingGravity value' + 'x : ' + gx + 'y : ' + gy + 'z : ' + gz;
	
	    // using acceleration values
	    var content = ""+ax+','+ay+','+az+'\r\n';
		if(isNormalLogStart === true)
		{
			if(writePeriod === 0)
			{
				writePeriod = 47;
				filesystem.writeFile(accelNormalLogPath,"#stop#\r\n#start#-no\r\n",onErrortoWriteFile,'a');
			}
			writePeriod--;
			filesystem.writeFile(accelNormalLogPath,content,onErrortoWriteFile,'a');
		}
		else
		{
			filesystem.writeFile(accelLogPath,content,onErrortoWriteFile,'a');
		}
	}
	
	// fileoperations
	function onSuccesstoOpenDir(dir)
	{
		accelLogDir = dir;
		// 유효 가속도 값 기록 파일
		try
		{
			accelLogPath = accelLogDir.resolve(logFileName);
		}catch(exc)
		{
			
			accelLogDir.createFile(logFileName);
			
			accelLogPath = accelLogDir.resolve(logFileName);
		}
		// 일반 가속도 값
		try
		{
			accelNormalLogPath = accelLogDir.resolve(NormallogFileName);
		}catch(exc)
		{
			
			accelLogDir.createFile(NormallogFileName);
			
			accelNormalLogPath = accelLogDir.resolve(logFileName);
		}
		
		if(accelLogPath === undefined || accelNormalLogPath === undefined)
		{
			alert("FATAL ERROR");
			tizen.application.getCurrentApplication().exit();
		}
		else
		{
			//alert("File Init Operation Success");
		}
	}
	//error handlings
	function onErrortoOpenDir(e)
	{
		alert("openDir\n"+e);
	}
	function onErrortoWriteFile(e)
	{
		alert("writeFile\n"+e);
	}
	
	
	// logging
	function onAccelLogOperation()
	{
		if(isNormalLogStart === true)
		{
			// 일반 로그를 중지할 때 까지 진행 못함
			return;
		}
		if(isAccelLogStart === false)
		{
			isAccelLogStart = true;
			LogStartStopButton.innerHTML = "stop accel log";
			filesystem.writeFile(accelLogPath,"#start#-yes\r\n",onErrortoWriteFile,'a');
			window.addEventListener("devicemotion",onAcceleration,true);
		}
		else
		{	
			isAccelLogStart = false;
			LogStartStopButton.innerHTML = "start accel log";
			window.removeEventListener("devicemotion",onAcceleration,true);
			filesystem.writeFile(accelLogPath,"#stop#\r\n",onErrortoWriteFile,'a');
		}		
	}
	// normal logging
	function onNormalLogOperation()
	{
		// 시작 못함
		if(isAccelLogStart === true)
		{
			return;
		}
		if(isNormalLogStart === false)
		{
			isNormalLogStart = true;
			NormalLogStartButton.innerHTML = "stop normal log";
			filesystem.writeFile(accelNormalLogPath,"#start#-no\r\n",onErrortoWriteFile,'a');
			window.addEventListener("devicemotion",onAcceleration,true);
		}
		else
		{	
			isNormalLogStart = false;
			NormalLogStartButton.innerHTML = "start normal log";
			window.removeEventListener("devicemotion",onAcceleration,true);
			filesystem.writeFile(accelNormalLogPath,"#stop#\r\n",onErrortoWriteFile,'a');
		}		
	}
	function onAccelLogPage()
	{
		tau.changePage("#accellogpage");
	}
	
	// delete log
	function onDeleteLog()
	{
		//deleteDir(dir, path, onSuccess, onError)
		console.log(accelLogPath.fullPath);
		filesystem.deleteFile(accelLogDir,accelLogPath.fullPath,function()
				{
					alert("deleteDir Success Application will be exit");
					tizen.application.getCurrentApplication().exit();
				},function(e)
				{
					alert("deleteDir "+e);
				});
	}
	
	// init
	function logfileinit()
	{
		filesystem.openDir('documents',onSuccesstoOpenDir,onErrortoOpenDir,'rw');
	}
	
	LogStartStopButton = document.getElementById("log_operation");
	DeleteLogButton = document.getElementById("delete_log");
	AccelLogOpenButton = document.getElementById("accellog");
	NormalLogStartButton = document.getElementById("normal_operation");
	
	LogStartStopButton.addEventListener("click", onAccelLogOperation);
	DeleteLogButton.addEventListener("click",onDeleteLog);
	AccelLogOpenButton.addEventListener("click", onAccelLogPage);
	NormalLogStartButton.addEventListener("click",onNormalLogOperation);
	
	logfileinit();	
})();