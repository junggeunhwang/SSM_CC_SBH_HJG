package com.ssm.cyclists.controller.timertask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import android.util.Log;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.communication.https.Protocol;
import com.ssm.cyclists.controller.manager.CruiseDataManager;
import com.ssm.cyclists.controller.manager.DataBaseManager;
import com.ssm.cyclists.controller.manager.SettingsDataManager;

public class CruiseInfoTimerTask extends TimerTask {
	
	static String TAG = CruiseInfoTimerTask.class.getSimpleName();
	
	long startTime = System.currentTimeMillis();
	
	@Override
	public void run() {
		
		long now = System.currentTimeMillis();
		long dif = now-startTime;
		
		dif = dif % 86400000; 

		long hours = (int) (dif / 3600000);//1000(ms)*60(S)*60(M) 
		dif = dif % 3600000;
		
		long mins = (int) (dif / 60000);//1000(ms)*60(S) 
		dif = dif % 60000; 
		long secs = (int) (dif / 1000);//1000(ms) 
		
		String elapsedTime= String.format("%02d : %02d : %02d", hours,mins,secs);
		
		
		CruiseDataManager.getInstance().setElapsedTime(elapsedTime);
		
		DataBaseManager.getInstance().insertCruiseData(
				String.valueOf(startTime),
				String.valueOf(CruiseDataManager.getInstance().getCurrent_speed()),	//speed
				String.valueOf(CruiseDataManager.getInstance().getElevation()),		//altitude
				String.valueOf(CruiseDataManager.getInstance().getDistnace()),		//distance
				String.valueOf(CruiseDataManager.getInstance().getCalory()),		//calory
				String.valueOf(CruiseDataManager.getInstance().getCurrent_loc().getLatitude()),
				String.valueOf(CruiseDataManager.getInstance().getCurrent_loc().getLongitude()),
				String.valueOf(now));						//timestamp
		
		String location = "location,"+
		String.valueOf(SettingsDataManager.getInstance().getMe().getUniqueID())+ "," +
		CruiseDataManager.getInstance().getCurrent_loc().getLatitude() + "," + 
		CruiseDataManager.getInstance().getCurrent_loc().getLongitude();
		
		Protocol.getInstance().SendString(location, SettingsDataManager.getInstance().getMe().getUniqueID());
		
		CruiseDataManager manager = CruiseDataManager.getInstance();
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().isVisible())
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().updateMapViewInfo();
		
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmCruiseFragment().isVisible())
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmCruiseFragment().updateCruiseInfo();
		
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().isVisible())
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().updateMapViewInfo();
		
		Log.d(TAG,"speed : "+ manager.getCurrent_speed() + "\n"+
				"altitude : "+ manager.getElevation()+ "\n"+
				"distance : " + manager.getDistnace()+ "\n"+
				"calory : " + manager.getCalory());
	}
	
	
}