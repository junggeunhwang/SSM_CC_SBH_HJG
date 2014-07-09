function MotionCheckStart()
{
	var motionqueue = [];
	var avg_motion_count = 45;
	
	window.addEventListener("devicemotion",onAccelerationCallback,true);
	
	function MotionSuccess(i)
	{
		MotionSensorStop();		
		console.log("YES Motion detected "+ i);		
		setTimeout(MotionSensorStart,250);
	}
	
	function MotionSensorStop()
	{
		window.removeEventListener("devicemotion",onAccelerationCallback,true);
		motionqueue = [];
	}
	
	function MotionSensorStart()
	{
		window.addEventListener("devicemotion",onAccelerationCallback,true);
	}
	
	function MotionCheck(va_avg,va_SD,hq_avg,hq_SD)
	{
		//console.log(va_avg+" "+va_SD+" "+hq_avg + " " + hq_SD);
		/*if(hq_SD > 0.69)
		{
			if(va_avg <= 0.16)
			{
				if(hq_SD <= 1.59)
				{
					if(va_SD > 1.81)
					{
						if(hq_avg <= 2.02)
						{
							//MotionSuccess(1);
						}
					}
				}
				else
				{
					if(va_SD <= 2.3)
					{
						//MotionSuccess(2);
					}
				}
			}
			else
			{
				if(hq_avg<1.23)
				{
					if(hq_avg>1)
					{
						if(va_SD <=1.86)
						{
							if(va_avg <= 0.32)
							{
								if(va_avg > 0.21)
								{
									//MotionSuccess(3);
								}
							}
						}
						else
						{
							console.log("up motion detected!");
							//MotionSuccess(4);
						}
					}
				}
			}
		}*/
		//console.log(va_avg+ ","+hq_SD);
		/*if(va_avg>0.79)
		{
			if(hq_SD <= 0.87)
			{
				MotionSuccess("up");
				//myevent.fire('audio.onAudioRecordingOperation');
			}
		}*/
		
		/*if(va_avg > 0.79)
		{
			if(hq_SD <=0.64)
			{
				if(hq_SD <=0.56)
				{
					MotionSuccess("up");
				}
			}
			else
			{
				if(hq_avg<=1.41)
				{
					if(va_avg<=1.22)
					{
						if(hq_avg <= 0.96)
						{
							if(va_SD > 1.47)
							{
								//MotionSuccess("up-8");
							}
						}
					}
					else
					{
						MotionSuccess("down");
					}
				}
			}
		}*/
		/*if(va_avg>1.12)
		{
			if(hq_avg<=1.41)
			{
				MotionSuccess("down");
			}
		}*/
		
		if(va_avg >0.77)
		{
			if(hq_SD <=0.99)
			{
				MotionSuccess("up");
				//myevent.fire('audio.onAudioRecordingOperation');
				//return;
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
			//console.log("extract");
			extract();
			motionqueue.shift();
		}
	}
	function extract()
	{
		var len = avg_motion_count;
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
	
		MotionCheck(va_avg,va_SD,hq_avg,hq_SD);
	}
	myevent.listeners({
	    	'motionsensor.start':MotionSensorStart,
	    	'motionsensor.stop':MotionSensorStop
	});
}