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
			imagetag.src = "/images/background.PNG";
		}
		else
		{
			is_background = false;
			imagetag.src = "/images/left.PNG";
		}
	}
	function onRighttoggle()
	{
		if(is_background === false)
		{
			is_background = true;
			imagetag.src = "/images/background.PNG";
		}
		else
		{
			is_background = false;
			imagetag.src = "/images/right.PNG";
		}
	}
	function onLeftClicked()
	{
		tau.changePage("#directiontictok");
		timerevent = new mytimer.Timer(500,onLefttoggle);
		timerevent.run();
	}
	function onRightClicked()
	{
		tau.changePage("#directiontictok");
		timerevent = new mytimer.Timer(500,onRighttoggle);
		timerevent.run();
	}
	function Release()
	{
		if(timerevent === undefined)
		{
			
		}
		else
		{
			imagetag.src = "/images/background.PNG";
			timerevent.stop();
			delete timerevent;
		}
	}
	left.addEventListener("click", onLeftClicked);
	right.addEventListener("click", onRightClicked);
	return Release;
}