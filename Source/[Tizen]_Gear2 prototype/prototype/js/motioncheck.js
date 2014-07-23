function MotionCheckStart()
{
	var motionqueue = [];
	var avg_motion_count = 38;
	var isMotionProceed = false;
	
	function MotionSuccess(i)
	{
		MotionSensorStop();		
		console.log("YES Motion detected "+ i);
		myevent.fire('audio.onAudioRecordingOperation');
		setTimeout(MotionSensorStart,1000);
	}
	function onWRIST_UP()
	{
		//audiorecord.isRecording() === true && 
		var accelobj = extract(false);
		console.log("WRIST_UP EXPECTED");
		console.log(accelobj);
		console.log(accelobj.va_avg,+","+accelobj.hq_avg+","+accelobj.va_SD+","+accelobj.hq_SD);
		if(isMotionProceed === false)
		{		
			//console.log("WRIST_UP EXPECTED");
			//console.log(accelobj.va_avg,+","+accelobj.hq_avg+","+accelobj.va_SD+","+accelobj.hq_SD);
			if(audiorecord.isRecording() === true ||(0.8 < accelobj.va_avg && accelobj.va_avg < 3.0) && (1.0 < accelobj.va_SD && accelobj.va_SD < 4.0))
			{
				isMotionProceed = true;
				MotionSuccess("WRIST_UP");
			}					
		}
	}
	
	function MotionSensorStop()
	{
		webapis.motion.stop("WRIST_UP");
		window.removeEventListener("devicemotion",onAccelerationCallback,true);
		motionqueue = [];
	}
	
	function MotionSensorStart()
	{
		isMotionProceed = false;
		window.addEventListener("devicemotion",onAccelerationCallback,true);
		webapis.motion.start("WRIST_UP", onWRIST_UP);
	}
	
	function MotionCheck(va_avg,va_SD,hq_avg,hq_SD)
	{
		if(0.71<va_avg && va_avg < 2.0)// || va_avg <=0.22)
		{
			if(hq_avg <= 0.88)
			{
				if(1.1<va_SD && va_SD < 2.0)
				{
					if(hq_SD > 0.5)
					{
						if(isMotionProceed === false)
						{
							isMotionProceed = true;
							console.log(va_avg +","+ hq_avg +","+ va_SD+","+hq_SD);
							MotionSuccess("up");
						}
					}
				}
			}
		}
	}
	function onAccelerationCallback(e)
	{
		var ax,ay,az,gx,gy,gz;
		
		ax = parseFloat(e.acceleration.x.toFixed(1));
		ay = parseFloat(e.acceleration.y.toFixed(1));
		az = parseFloat(e.acceleration.z.toFixed(1));
		
		var count = motionqueue.push({x:ax,y:ay,z:az});
		
		if(count == avg_motion_count)
		{
			extract(true);
			motionqueue.shift();
		}
	}
	function extract(isAutoMotionChecking)
	{
		var len;
		if(isAutoMotionChecking === true)
		{
			len = avg_motion_count;
		}
		else
		{
			len = motionqueue.length;
		}
		var i = 0;
		var avg_vector = {x:0,y:0,z:0};
		var norm_vector = {x:0,y:0,z:0};

		for(;i<len;i++)
		{
			avg_vector.x += motionqueue[i].x;
			avg_vector.y += motionqueue[i].y;
			avg_vector.z += motionqueue[i].z;
		}
		
		// 평균 벡터 계산
		avg_vector.x/= len;
		avg_vector.y/= len;
		avg_vector.z/= len;
		
		// 평균 벡터의 노멀라이즈 벡터 계산
		
		norm_vector = avg_vector;
		
		var quantity = Math.sqrt(Math.pow(avg_vector.x,2)+Math.pow(avg_vector.y,2)+Math.pow(avg_vector.z,2));
		
		norm_vector.x /= quantity;
		norm_vector.y /= quantity;
		norm_vector.z /= quantity;
		
		var vectical_amplitude;
		var horizontal_quantity;
		
		var va_avg,va_SD,hq_avg,hq_SD;
		
		va_avg = va_SD = hq_avg = hq_SD = 0;
		
		for(i=0;i<len;i++)
		{
			var pIn = (motionqueue[i].x*norm_vector.x) + (motionqueue[i].y*norm_vector.y) +(motionqueue[i].z*norm_vector.z);
			// 수직 성분
			vectical_amplitude = pIn;
			
			var pi={x:pIn*norm_vector.x,y:pIn*norm_vector.y,z:pIn*norm_vector.z};
			var horizon_unit = {x:motionqueue[i].x-pi.x,y:motionqueue[i].y-pi.y,z:motionqueue[i].z-pi.z};
			
			horizontal_quantity = Math.sqrt(Math.pow(horizon_unit.x,2)+Math.pow(horizon_unit.y,2)+Math.pow(horizon_unit.z,2));
			
			va_avg += pIn;
			hq_avg += horizontal_quantity;
			
			va_SD += Math.pow(pIn,2);
			hq_SD += Math.pow(horizontal_quantity,2);
		}
		
		va_avg/= len;
		hq_avg/= len;
		va_SD/=len;
		hq_SD/=len;
		
		va_SD -= Math.pow(va_avg,2);
		hq_SD -= Math.pow(hq_avg,2);
		
		va_SD = Math.sqrt(va_SD);
		hq_SD = Math.sqrt(hq_SD);
	
		if(isAutoMotionChecking === true)
		{
			MotionCheck(va_avg,va_SD,hq_avg,hq_SD);
		}
		else
		{
			console.log(va_avg +","+ hq_avg +","+ va_SD+","+hq_SD);
		}
		
		
		return {'va_avg':va_avg,'hq_avg':hq_avg,'va_SD':va_SD,'hq_SD':hq_SD};
	}
	myevent.listeners({
	    	'motionsensor.start':MotionSensorStart,
	    	'motionsensor.stop':MotionSensorStop
	});
}