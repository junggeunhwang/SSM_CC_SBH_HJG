(function main()
{
	var ERROR_FILE_WRITE = 'FILE_WRITE_ERR';
    var NO_FREE_SPACE_MSG = 'No free space.';
    // 좌우 방향 표시등 관련 타이머 제어 함수
    var leftrightToggleStopFunc;
    
    // 오디오 녹음 및 정지 버튼
    var recordOperationImg = document.getElementById("recordOperationImg");
    var recordOperation = document.getElementById("recordOperation");
    
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
			else if(pageid === "thirdpage")
			{
				tau.changePage("#mainpage");
			}else if(pageid === "directiontictok")
			{
				console.log("directiontictok invoked");
				leftrightToggleStopFunc();
				tau.changePage("#mainpage");
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
			g_filetransfer.sendFile(g_peerAgent,'file://'+RecordedAudioPath);
		}
		else
		{
			mainExtrastatus.innerHTML = msg_audiosendingerror;
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
    		// 이 시점에서 페이지 변경 타이머 중지
    		pagetimer.pause();
    		preSoundBeforeRecording();
    	}
    	else 
    	{
    		recordOperationImg.src = "./images/startrecord.PNG";;
    		stopRecording();
    		mainExtrastatus.innerHTML = msg_audiorecordingend;

    		// 타이머 재개
    		pagetimer.run();
    	}		
    }
    
    // 재생 전 호출
    function preSoundBeforeRecording()
    {
    	var params = {};
    	var endevent = {};
    	params.volume = 15;
    	params.loop = false;
    	params.file = './res/sound/recordstart.mp3';
    	
    	endevent.event = 'ended';
    	endevent.callback = function(){
    		// 녹음 시작
    		startRecording();
    		// 진동
    		// 이미지 변경
    		recordOperationImg.src = "./images/stoprecord.PNG";
    		// 알림
    		mainExtrastatus.innerHTML = msg_audiorecording;
    		// 콜백 제거
    		audioplay.removeAudioCallback(endevent);
    	};
    	
    	audioplay.addAudioCallback(endevent);
    	
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
    	toggleRecording(true);
    	AudioRecordingLock = false;
    	navigator.vibrate(250);
    }

    // 오디오 레코딩 종료
    function onRecordingDone(ev) 
    {
    	RecordedAudioPath = ev.detail.path;
    	AudioRecordingLock = false;
    	toggleRecording(false);
		onFileTransfer();
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
    	'audio.onAudioRecordingOperation' : onAudioRecordingOperation
    });
    
    // 오디오 버튼 이벤트 등록
    recordOperation.addEventListener("click", onAudioRecordingOperation);
    // 항상 켜져있게 전원 옵션 해제
    tizen.power.request("SCREEN", "SCREEN_NORMAL");
    tizen.power.request("CPU", "CPU_AWAKE");

    // 오디오 스트림 초기화
    audiostream.getStream();
    // SAP 초기화
    SAPInit();
	// 모션 체크 시작
    //MotionCheckStart();
    // 옵션 페이지 활성화
    optionInit();
    // leftrightToggle 기능 활성화
    leftrightToggleStopFunc = toggleInit();
})();

