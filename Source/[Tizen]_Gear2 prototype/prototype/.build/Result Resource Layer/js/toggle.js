function toggleInit()
{
	var is_background = false;
	var imagetag = document.getElementById("toggleimg");
	var left = document.getElementById("leftToggle");
	var right = document.getElementById("rightToggle");
	var timerevent;
	
	function onLefttoggle()
	{
		if(is_background === false)
		{
			is_background = true;
			imagetag.src = "/image/background.png";
		}
		else
		{
			is_background = false;
			imagetag.src = "/image/left.png";
		}
	}
	function onRighttoggle()
	{
		if(is_background === false)
		{
			is_background = true;
			imagetag.src = "/image/background.png";
		}
		else
		{
			is_background = false;
			imagetag.src = "/image/right.png";
		}
	}
	function onLeftClicked()
	{
		tau.changePage("#directiontictok");
		timerevent = new mytimer.Timer(500,onLefttoggle);
		timerevent.run();
		// 사초간 실행 후 종료
		setTimeout(Release,6000);
	}
	function onRightClicked()
	{
		tau.changePage("#directiontictok");
		timerevent = new mytimer.Timer(500,onRighttoggle);
		timerevent.run();
		// 4초간 실행 후 종료
		setTimeout(Release,6000);
	}
	function Release()
	{
		if(timerevent === undefined)
		{
			
		}
		else
		{
			imagetag.src = "/image/background.png";
			timerevent.stop();
			delete timerevent
			timerevent = undefined;
			tau.changePage("#mainpage");
		}
	}
	left.addEventListener("click", onLeftClicked);
	right.addEventListener("click", onRightClicked);
	
	 myevent.listeners({
	    	'toggle.release': Release,
	    	'toggle.left': onLeftClicked,
	    	'toggle.right': onRightClicked
	    });
}