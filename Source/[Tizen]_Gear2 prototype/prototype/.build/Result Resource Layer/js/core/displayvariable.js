// 출력 변수
var mainPeerstatus = document.getElementById("peerstatus");
var mainExtrastatus = document.getElementById("extrastatus");

var thirdFilesender = document.getElementById("filesender");

// 디바이스 통신 관련
var msg_devicedetached = '디바이스와 분리됨';
var msg_deviceattached = '디바이스와 연결됨';
var msg_peernotfound = '피어 앱을 찾지 못함';
var msg_peerfound = '피어 앱을 발견!';
var msg_sapavailable = '디바이스와 통신 가능!';
var msg_sapunavailable = '디바이스와 통신 불가';
var msg_peernoresponse = '피어 서비스 사용 불가';

// 추가 정보 관련

// 오디오 녹음 관련
var msg_audiorecording = '녹음 중 ...';
var msg_audiorecordingend = '녹음 완료!';
var msg_audiosendingsuccess = '음성파일 송신 완료!';
var msg_audiosendingerror = '음성 파일 송신 실패';


// 이벤트 관련 작업

function onChangetoMainpage()
{
	tau.changePage("#mainpage");
}

function onChangetoSecondpage()
{
	tau.changePage("#secondpage");
}

function onChangetoThirdpage()
{
	tau.changePage("#thirdpage");
}

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
	}else if(pageid === "thirdpage")
	{
		// 세번째 페이지는 사용자가 돌리기 전 까지 작업 하지 않음
	}
}

// 5초마다 페이지 전환
pagetimer = new mytimer.Timer(5000,onChangePageByTimer);

// 수신된 파일 처리 큐

var multicastedfilequeue = [];

var queueprocessingtimer;

function onVoiceFilePlay(data)
{
	var params = {};
	var endevent = {};
	params.volume = 15;
	params.loop = false;
	params.file = data.path;
	
	// 한번 더
	myevent.fire('page.thirdpage');
	
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
	};
	audioplay.addAudioCallback(endevent);
	
	navigator.vibrate([750,250,500]);
	
	
	audioplay.play(params);
}

function onQueueprocessingtimer()
{
	if(multicastedfilequeue.length !== 0)
	{
		var data = multicastedfilequeue.shift();
		
		console.log(data);
		
		thirdFilesender.innerHTML = data.name;		
		// 처리 타이머 중지
		queueprocessingtimer.pause();
		myevent.fire('page.thirdpage');
		onVoiceFilePlay(data);
	}
}

// 1초마자 체크, 및 파일 재생

queueprocessingtimer = new mytimer.Timer(1500,onQueueprocessingtimer);

// 이벤트 등록
myevent.listeners({
	'page.mainpage': onChangetoMainpage,
	'page.secondpage': onChangetoSecondpage,
	'page.thirdpage': onChangetoThirdpage
});

// 타이머 시작

queueprocessingtimer.run();
pagetimer.run();