

function optionInit(){
	// 옵션 버튼들
	var TestButton,AudioRecordButton,AudioPlayButton,SendToHostDeviceButton;
	var OptionButton;
	var togglepageReleaseFunc;

	// toggle
    function onTestPage()
    {
    	/*tau.changePage("#togglepage");
    	togglepageReleaseFunc = togglemain();*/
    	var tztime = tizen.time.getCurrentDateTime();
    	var date = new Date();
    	var filename = date.getHours().toString()+"_" + date.getMinutes().toString()+"_" + date.getSeconds().toString()+"_"+date.getMilliseconds();
    	console.log(filename);
    }
    //audio
    function onAudioRecordingOperation()
    {
    	if(AudioRecordingLock === true)
    	{
    		console.log("operation is not complete");
    		return;
    	}
    	if (AudioRecordingFlag === false) 
    	{
    		preSoundBeforeRecording();
    	}
    	else 
    	{
    		AudioRecordButton.innerHTML = "start record";
    		stopRecording();
    	}		
    }

    // 재생 시 호출
    function onAudioPlayingOperation()
    {
    	if(RecordedAudioPath === undefined)
    	{
    		console.log("path is not defined");
    		return;
    	}
    	console.log("path is "+RecordedAudioPath);
    	var params = {};
    	params.volume = 15;
    	params.loop = false;
    	params.file = RecordedAudioPath;

    	if(audioplay.play(params) !== true)
    	{
    		console.log("fail to play audio");
    	}
    }
    // audio presound
    function preSoundBeforeRecording()
    {
    	var params = {};
    	var endevent = {};
    	params.volume = 15;
    	params.loop = false;
    	params.file = './res/sound/recordstart.mp3';
    	
    	endevent.event = 'ended';
    	endevent.callback = function(){
    		startRecording();
    		navigator.vibrate(250);
    		AudioRecordButton.innerHTML = "stop record";
    		audioplay.removeAudioCallback(endevent);
    	};
    	
    	audioplay.addAudioCallback(endevent);
    	
    	audioplay.play(params);
    }
    
    // send to host
    
    function onSendToHost()
    {
    	//g_SAPStringSock.sendData(g_SAAgent.channelIds[0],"Hello I'm gear 2");
    	tau.changePage("#sappage");
    	transferinit();
    }
    //option
    function onOptionPage()
    {
    	tau.changePage("#optionpage");
    }


    // init stream


    // bind event		
    TestButton = document.getElementById("test");
    AudioRecordButton = document.getElementById("start_record");
    AudioPlayButton = document.getElementById("play_record");
    SendToHostDeviceButton = document.getElementById("send_to_host");
    OptionButton = document.getElementById("hoption");

    TestButton.addEventListener("click",onTestPage);
    AudioRecordButton.addEventListener("click",onAudioRecordingOperation);
    AudioPlayButton.addEventListener("click",onAudioPlayingOperation);
    SendToHostDeviceButton.addEventListener("click", onSendToHost);
    OptionButton.addEventListener("click", onOptionPage);
}