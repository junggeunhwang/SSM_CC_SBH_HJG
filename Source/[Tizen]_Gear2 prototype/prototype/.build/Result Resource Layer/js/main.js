(function main()
{
	var ERROR_FILE_WRITE = 'FILE_WRITE_ERR';
    var NO_FREE_SPACE_MSG = 'No free space.';
    // 모션 인식 활성화 여부
    var MotionRecognitionCheckbox = document.getElementById("motioncheckbox");
    
    // 오디오 녹음 및 정지 버튼
    //var recordOperationImg = document.getElementById("recordOperationImg");
    var recordOperation = document.getElementById("recordOperation");
    var recordbutton = document.getElementById("recordbutton");
    
	// 손 터치 이벤트 등록
	window.addEventListener( 'tizenhwkey', function( ev ) {
		if( ev.keyName == "back" )
		{
			var page = document.getElementsByClassName( 'ui-page-active' )[0],
				pageid = page ? page.id : "";
			if( pageid === "mainpage" ) {
				myevent.fire('application.exit');
			} 
			else if(pageid === "secondpage")
			{
				tau.changePage("#mainpage");
			}
			else if(pageid === "alertpage")
			{
				tau.changePage("#mainpage");
			}else if(pageid === "directiontictok")
			{
				console.log("directiontictok invoked");
				myevent.fire('toggle.release');
			}
			else
			{
				window.history.back();
			}
		}
	} );
	// 마무으리
	function onApplicationExit() 
	{
        if (audiorecord.isReady()) {
        	audiorecord.release();
        	AudioStream.stop();
        }
        //SAP 해제
        if(g_SAPStringSock !== undefined)
        {
        	try
        	{
        		if(isSAPValidate === true)
        		{
        			g_SAPStringSock.close();
        		}
        	}catch(e)
        	{
        		console.log("Error Exception, error name : " + e.name + ", error message : " + e.message);
        	}
        }
        tizen.application.getCurrentApplication().exit();
    }
	
	// 오디오 관련 이벤트
	function onFileTransfer()
	{
		if(RecordedAudioPath !== undefined && isSAPValidate === true)
		{
			console.log("RecordedAudioPath = "+RecordedAudioPath);
			g_filetransfer.sendFile(g_peerAgent,'file://'+RecordedAudioPath);
		}
		else
		{
			voicerecordStatus.innerHTML = msg_audiosendingerror;
		}
	}


	// 오디오 스트림 초기화 완료
    function onStreamReady(ev) 
    {
    	AudioStream = ev.detail.stream;
    	audiorecord.registerStream(AudioStream);
    }

    // 이 이벤트는 사실 사용될 일이 없음
    function onStreamCannotAccessAudio()
    {
    	if (document.visibilityState === 'visible') 
    	{
    		showExitAlert(CANNOT_ACCESS_AUDIO_MSG);
    	}
    }
    
    // 오디오 버튼 눌렸을 때, 혹은 모션 관련하여 작업 시 호출
    function onAudioRecordingOperation()
    {
    	if(AudioRecordingLock === true)
    	{	
    		return;
    	}
    	if (AudioRecordingFlag === false) 
    	{
    		// 녹음 시작
    		// 이 시점에서 페이지 변경 타이머 중지
    		pagetimer.pause();
    		// 녹음 페이지로 변경
    		myevent.fire('page.voicerecordpage');
    		startRecording();
    	}
    	else 
    	{
    		// 녹음 종료
    		recordOperation.src = "./image/voicerecordpage/startrecord.PNG";;
    		stopRecording();
    		voicerecordStatus.innerHTML = msg_audiorecordingend;
    	}		
    }
    
    // 재생 전 호출
    function preSoundPlay(filename)
    {
    	var params = {};
    	
    	params.volume = 15;
    	params.loop = false;
    	params.file = './res/sound/'+filename;
    	
    	audioplay.play(params);
    }

    // 오디오가 준비됨
    function onAudioReady()
    {
    	console.log('onAudioReady()');
    }

    // 이 에러가 발생하면 프로그램을 종료시킴
    function onAudioError()
    {
    	console.log('onAudioError()');
    	alert("오디오 모듈 로드에 실패했습니다. 어플리케이션을 다시시작하십시오.");
    	tizen.application.getCurrentApplication().exit();
    }
    // 오디오 레코딩 시작
    function onRecordingStart() 
    {
    	myevent.fire('page.voicerecordpage');
    	toggleRecording(true);
    	AudioRecordingLock = false;
    	preSoundPlay("recordstart.mp3");
    	navigator.vibrate(250);
    	// 이미지 변경
		recordOperation.src = "./image/voicerecordpage/stoprecord.PNG";
		// 알림
		voicerecordStatus.innerHTML = msg_audiorecording;
    }

    // 오디오 레코딩 종료
    function onRecordingDone(ev) 
    {
    	RecordedAudioPath = ev.detail.path;
    	preSoundPlay("recordend.mp3");
    	AudioRecordingLock = false;
    	toggleRecording(false);
		onFileTransfer();
		
		// 메인 페이지로 변경
		myevent.fire('page.mainpage');
		// 타이머 재개
		pagetimer.run();
    }
    // 오디오 레코딩 중 에러 발생 시 대처
    function onRecordingError(ev)
    {
    	var error = ev.detail.error;

    	if (error === ERROR_FILE_WRITE) {
    		console.error(NO_FREE_SPACE_MSG);
    	} else {
    		console.error('Error: ' + error);
    	}

    	toggleRecording(false);
    }
    
    // 모션 인식 적용 여부
    
    function onMotionRecognitionCheckbox()
    {
    	if(MotionRecognitionCheckbox.checked === true)
    	{
    		isMotionCheckUsed = true;
    		myevent.fire('motionsensor.start');
    	}
    	else
    	{
    		isMotionCheckUsed = false;
    		myevent.fire('motionsensor.stop');
    	}
    }

	
	// 이벤트 등록
    myevent.listeners({
    	'application.exit': onApplicationExit,
    	'models.stream.ready': onStreamReady,
    	'models.stream.cannot.access.audio': onStreamCannotAccessAudio,
    	'audio.ready': onAudioReady,
    	'audio.error': onAudioError,
    	'audio.recording.start': onRecordingStart,
    	'audio.recording.done': onRecordingDone,
    	'audio.recording.error': onRecordingError,
    	'audio.onAudioRecordingOperation' : onAudioRecordingOperation,
    	'audio.onPreSoundPlay' : preSoundPlay
    });
    
    // 오디오 버튼 이벤트 등록
    recordOperation.addEventListener("click", onAudioRecordingOperation);
    recordbutton.addEventListener("click", onAudioRecordingOperation);
    // 항상 켜져있게 전원 옵션 해제
    tizen.power.request("SCREEN", "SCREEN_NORMAL");
    tizen.power.request("CPU", "CPU_AWAKE");

    // 좌우 깜빡이 기능 초기화
    toggleInit();
    // 모션 체크 시작
    MotionCheckStart();
    // 이벤트 등록
    MotionRecognitionCheckbox.addEventListener("click", onMotionRecognitionCheckbox);
    // 오디오 스트림 초기화
    audiostream.getStream();
    // SAP 초기화
    SAPInit();
    // 옵션 페이지 활성화
    optionInit();
})();

