
(function main(){

	
	var ToggleButton,AudioRecordButton,AudioPlayButton;
	var togglepageReleaseFunc;
	var RecordedAudioPath;
	var ERROR_FILE_WRITE = 'FILE_WRITE_ERR',
    NO_FREE_SPACE_MSG = 'No free space.',
    CANNOT_ACCESS_AUDIO_MSG = 'Cannot access audio stream. ' +
    'Please close all applications that use the audio stream and ' +
    'open the application again.',

    stream = null,
    recording = false,
    recordingLock = false;
	
	window.addEventListener( 'tizenhwkey', function( ev ) {
		if( ev.keyName == "back" )
		{
			var page = document.getElementsByClassName( 'ui-page-active' )[0],
				pageid = page ? page.id : "";
			if( pageid === "mainpage" ) {
				myevent.fire('application.exit');
				tizen.application.getCurrentApplication().exit();
			} 
			else 
			{
				if(pageid === "directiontictok")
				{
					console.log("directiontictok invoked");
					togglepageReleaseFunc();
					tau.changePage("#mainpage");
				}
				else
				{
					window.history.back();
				}
			}
		}
	} );
	 function toggleRecording(forceValue) {
         if (forceValue !== undefined) {
             recording = !!forceValue;
         } else {
             recording = !recording;
         }
     }
	// 마무으리
	function onApplicationExit() 
	{
        if (audiorecord.isReady()) {
        	audiorecord.release();
            audiostream.stop();
        }
    }
	
	// 음성 녹음 시작
	
    function startRecording() 
    {
   		recordingLock = true;
   		audiorecord.startRecording();
    }

    // 음성 녹음 종료
    function stopRecording() 
    {
        recordingLock = true;
        audiorecord.stopRecording();
    }

     /**
      * Handles models.stream.ready event.
      * @param {event} ev
      */
     function onStreamReady(ev) {
         stream = ev.detail.stream;
         audiorecord.registerStream(stream);
     }

     /**
      * Handles models.stream.cannot.access.audio event.
      */
     function onStreamCannotAccessAudio()
     {
         if (document.visibilityState === 'visible') 
         {
             showExitAlert(CANNOT_ACCESS_AUDIO_MSG);
         }
     }

     function initStream() 
     {
    	 audiostream.getStream();
     }

     /**
      * Handles audio.ready event.
      */
     function onAudioReady()
     {
         console.log('onAudioReady()');
     }

     /**
      * Handles audio.error event.
      */
     function onAudioError()
     {
         console.log('onAudioError()');
     }

     /**
      * Handles audio.recording.start event.
      */
     function onRecordingStart() 
     {
         toggleRecording(true);
         recordingLock = false;
     }

     /**
      * Handles audio.recording.done event.
      * @param {event} ev
      */
     function onRecordingDone(ev) 
     {
    	RecordedAudioPath = ev.detail.path;
    	recordingLock = false;
        toggleRecording(false);
     }

     /**
      * Handles audio.recording.error event.
      * @param {CustomEvent} ev
      */
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
	
	// toggle
	function onTogglePage()
	{
		tau.changePage("#togglepage");
		togglepageReleaseFunc = togglemain();
	}
	//audio
	
	function onAudioRecordingOperation()
	{
		if(recordingLock === true)
		{
			console.log("operation is not complete");
			return;
		}
		if (recording === false) 
        {
			AudioRecordButton.innerHTML = "stop record";
            startRecording();
        }
        else 
        {
        	AudioRecordButton.innerHTML = "start record";
            stopRecording();
        }		
	}
	
	function onAudioPlayingOperation()
	{
		if(RecordedAudioPath === undefined)
		{
			console.log("path is not defined");
			return;
		}
		console.log("path is "+RecordedAudioPath);
		var params = {};
		params.volume = 5;
		params.loop = false;
		params.file = RecordedAudioPath;
		
		if(audioplay.play(params) !== true)
		{
			console.log("fail to play audio");
		}
	}
	
	myevent.listeners({
        'application.exit': onApplicationExit,

        'models.stream.ready': onStreamReady,
        'models.stream.cannot.access.audio': onStreamCannotAccessAudio,

        'audio.ready': onAudioReady,
        'audio.error': onAudioError,

        'audio.recording.start': onRecordingStart,
        'audio.recording.done': onRecordingDone,
        'audio.recording.error': onRecordingError,
    });
	
	initStream();
	
	// bind event		
	ToggleButton = document.getElementById("toggle");
	AudioRecordButton = document.getElementById("start_record");
	AudioPlayButton = document.getElementById("play_record");
	
	ToggleButton.addEventListener("click",onTogglePage);
	AudioRecordButton.addEventListener("click",onAudioRecordingOperation);
	AudioPlayButton.addEventListener("click",onAudioPlayingOperation);
	
	motionlogInit();
	//MotionCheckStart();
})();