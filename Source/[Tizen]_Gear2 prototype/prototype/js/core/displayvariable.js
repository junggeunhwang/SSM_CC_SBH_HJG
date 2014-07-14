// 출력 변수
var mainPeerstatus = document.getElementById("deviceinfo");

// 속도 관련 변수
var mainDistance = document.getElementById("distance");
var mainMaxspeed = document.getElementById('maxspeed');
var mainAvgspeed = document.getElementById('avgspeed');
var mainAltitude = document.getElementById('altitude');

// 칼로리

var secondKcalinfo = document.getElementById("kcalinfo");

// alertpage 관련 출력 변수
var alertsender = document.getElementById("sender");
var alertmsg = document.getElementById("alertmsg");

// voicerecordingpage 관련 출력 변수
var voicerecordStatus = document.getElementById("recordinfo");

// 디바이스 통신 관련
var msg_devicedetached = 'Device detached';//'디바이스와 분리됨';
var msg_deviceattached = 'Device attached';//'디바이스와 연결됨';
var msg_peernotfound = 'No peer app';//'피어 앱을 찾지 못함';
var msg_peerfound = 'Find peer app!';//'피어 앱을 발견!';
var msg_sapavailable = 'Available!!';//'디바이스와 통신 가능!';
var msg_sapunavailable = 'Transmission invalid';//'디바이스와 통신 불가';
var msg_peernoresponse = 'Peer Srv is invalid';//'피어 서비스 사용 불가';

// 추가 정보 관련

// 오디오 녹음 관련
var msg_audiorecording = 'Voice Recording...';
var msg_audiorecordingend = 'Recording Done!';
var msg_audiosendingsuccess = 'Sending Voice File Done!';
var msg_audiosendingerror = 'Fail to send file.';

// 알림 관련
var msg_voicereceived = 'Voice Received!';
var msg_toofarfromgroup = '그룹과 너무 떨어져 있습니다.';

// 모션 인식 관련
var isMotionCheckUsed = false;


// 이벤트 관련 작업

function onChangetoMainpage()
{
	tau.changePage("#mainpage");
}

function onChangetoSecondpage()
{
	tau.changePage("#secondpage");
}

function onChangetoAlertpage()
{
	tau.changePage("#alertpage");
}

function onChangetoVoicerecordpage()
{
	tau.changePage("#voicerecordpage");
}

function onToggleMainPageAnimation()
{
	var ironmanheart = document.getElementById("ironmanheart");
	var currentState = ironmanheart.style.webkitAnimationPlayState;
	
	if(currentState === "running")
	{
		ironmanheart.style.webkitAnimationPlayState = "paused";
	}
	else
	{
		ironmanheart.style.webkitAnimationPlayState = "running";
	}
}
onToggleMainPageAnimation();

// 타이머에 의해 페이지 변경
var pagetimer;

function onChangePageByTimer()
{
	// 페이지 아이디를 겟
	var page = document.getElementsByClassName( 'ui-page-active' )[0],
	pageid = page ? page.id : "";
	
	if(pageid === "mainpage")
	{
		myevent.fire('page.secondpage');
	}
	else if(pageid === "secondpage")
	{
		myevent.fire('page.mainpage');
	}else
	{
		// 나머지 페이지는 특별한 작업을 하지 않음
	}
}

// 5초마다 페이지 전환
pagetimer = new mytimer.Timer(5000,onChangePageByTimer);

// 수신된 파일 처리 큐

var multicastedqueue = [];

var queueprocessingtimer;

function onVoiceFilePlay(data)
{
	var params = {};
	var endevent = {};
	params.volume = 15;
	params.loop = false;
	params.file = data.path;
	
	// 한번 더
	myevent.fire('page.alertpage');
	
	endevent.event = 'ended';
	endevent.callback = function()
	{
		myevent.fire('page.mainpage');
		audioplay.removeAudioCallback(endevent);
		// 재생이 완료된 파일 삭제
		filesystem.deleteNode(data.path,function(){
			console.log(data.path+"file deleted!");
		});
		// 타이머 재개
		queueprocessingtimer.run();
		// 모션 처리 재개
		if(isMotionCheckUsed === true)
		{
			myevent.fire('motionsensor.start');
		}
	};
	audioplay.addAudioCallback(endevent);
	
	navigator.vibrate([500,250,500]);	
	
	audioplay.play(params);
}

function onQueueprocessingtimer()
{
	if(audiorecord.isRecording() !== true && multicastedqueue.length !== 0)
	{
		var data = multicastedqueue.shift();
		
		console.log(data);
		
		if(data.type === "file")
		{
			// 알림 송신자 이름을 세팅
			alertsender.innerHTML = data.sender;
			alertmsg.innerHTML = msg_voicereceived;
			// 	처리 타이머 중지
			queueprocessingtimer.pause();
			myevent.fire('page.alertpage');
			// 	모션 처리 중지
			if(isMotionCheckUsed === true)
			{
				myevent.fire('motionsensor.stop');
			}
			onVoiceFilePlay(data);
		}
		else
		{
			// 이 경우 일반적인 alert message 로 처리
			alertsender.innerHTML = data.sender;
			alertmsg.innerHTML = data.message;
			
			queueprocessingtimer.pause();
			
			if(isMotionCheckUsed === true)
			{
				myevent.fire('motionsensor.stop');
			}
			
			// 4초간 alertmessage
			initAlertMessagepage(4000);			
		}
	}
}

// 1초마자 체크, 및 파일 재생

queueprocessingtimer = new mytimer.Timer(1500,onQueueprocessingtimer);

// 이벤트 등록
myevent.listeners({
	'page.mainpage': onChangetoMainpage,
	'page.secondpage': onChangetoSecondpage,
	'page.alertpage': onChangetoAlertpage,
	'page.voicerecordpage':onChangetoVoicerecordpage
});

//alert message 사용

function initAlertMessagepage(timeout)
{
	myevent.fire('page.alertpage');
	navigator.vibrate([500,250,500]);
	setTimeout(function(){
		myevent.fire('page.mainpage');
		queueprocessingtimer.run();
		// 모션 처리 재개
		if(isMotionCheckUsed === true)
		{
			myevent.fire('motionsensor.start');
		}
	},timeout);
}


// 타이머 시작
queueprocessingtimer.run();
pagetimer.run();