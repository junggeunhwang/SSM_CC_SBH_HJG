package com.ssm.ExCycling.controller.timertask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

import android.util.Base64;
import android.util.Log;

import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.communication.https.Protocol;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.DataBaseManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;

public class CruiseInfoTimerTask extends TimerTask {
	
	static String TAG = CruiseInfoTimerTask.class.getSimpleName();
	
	long startTime = System.currentTimeMillis();
	int count = 0;
	ArrayList<Double> speedList = new ArrayList<Double>();
<<<<<<< HEAD
	double prevDist = -1;
	
	public CruiseInfoTimerTask() {

		
		
	}
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	
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
		
<<<<<<< HEAD
		CruiseDataManager.getInstance().setElapsedTime(elapsedTime);
		
		String strDist = String.format("%.5f", CruiseDataManager.getInstance().getDistnace());
		
		if(prevDist == CruiseDataManager.getInstance().getDistnace()){
			strDist = String.valueOf(Double.valueOf(strDist) + 0.000005);
		}
			DataBaseManager.getInstance().insertCruiseData(
					String.valueOf(startTime),
					String.valueOf(CruiseDataManager.getInstance().getCurrent_speed()),	//speed
					String.valueOf(CruiseDataManager.getInstance().getElevation()),		//altitude
					strDist,		//distance
					String.valueOf(CruiseDataManager.getInstance().getCalory()),		//calory
					String.valueOf(CruiseDataManager.getInstance().getCurrent_loc().getLatitude()),
					String.valueOf(CruiseDataManager.getInstance().getCurrent_loc().getLongitude()),
					String.valueOf(now));//timestamp

			prevDist = Double.valueOf(strDist);
=======
		
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
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		
		count++;
		if(count == 5){
			String location = "location,"+
					Base64.encodeToString(SettingsDataManager.getInstance().getMe().getUserName().getBytes(),Base64.DEFAULT) + "," + 
					CruiseDataManager.getInstance().getCurrent_loc().getLatitude() + "," + 
					CruiseDataManager.getInstance().getCurrent_loc().getLongitude()+ "," +
					CruiseDataManager.getInstance().getCurrent_speed();
					
			Protocol.getInstance().SendString(location, SettingsDataManager.getInstance().getMe().getUniqueID());
					
			count = 0;
		}
		
		CruiseDataManager manager = CruiseDataManager.getInstance();
		speedList.add(Double.valueOf(manager.getCurrent_speed()));
		
		Double avgSpeed = 0.0;
		
		for(int i = 0 ; i < speedList.size();i++){
			avgSpeed+=speedList.get(i);
		}
		avgSpeed /= speedList.size();
		String strAvgSpeed = String.format("%.2f", avgSpeed);
		
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().isVisible())
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().updateMapViewInfo();
		
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmCruiseFragment().isVisible())
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmCruiseFragment().updateCruiseInfo();
		
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().isVisible())
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmMapViewFragment().updateMapViewInfo();
		
<<<<<<< HEAD
		
		//send to gear
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		sendFitnessinfo(CruiseDataManager.getInstance().getMaximum_speed(),
				Double.valueOf(strAvgSpeed),
				CruiseDataManager.getInstance().getDistnace(),
				CruiseDataManager.getInstance().getElevation(),
				CruiseDataManager.getInstance().getCalory());

		
<<<<<<< HEAD
		
		
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		Log.d(TAG,"speed : "+ manager.getCurrent_speed() + "\n"+
				"altitude : "+ manager.getElevation()+ "\n"+
				"distance : " + manager.getDistnace()+ "\n"+
				"calory : " + manager.getCalory());
	}
	
<<<<<<< HEAD
	public void sendFitnessinfo(double maxspeed,double avgspeed,double distance,double altitude,double kcal){
		String response = String.format("fitninfo;%.2f;%.2f;%.2f;%.2f;%.2f",maxspeed,avgspeed,distance,altitude,kcal);
		Log.d(TAG,response);
		MainActivity.getInstasnce().getmSAPService().SendByteData(response.getBytes());
	}
	
	@Override
	public boolean cancel() {
		String wind_dir;
		double dir = CruiseDataManager.getInstance().getWind_direction();
		
		if(dir < 90){
			wind_dir = "East";
		}
		else if(dir<180){
			wind_dir = "South";
		}else if(dir<270){
			wind_dir = "West";
		}else{
			wind_dir = "North";
		}
		DataBaseManager.getInstance().insertAtmosphere(
				String.valueOf(startTime),
				String.valueOf(CruiseDataManager.getInstance().getTemperature()),
				String.valueOf(CruiseDataManager.getInstance().getHumidity()), 
				String.valueOf(CruiseDataManager.getInstance().getWind()),
				wind_dir, 
				String.valueOf(SettingsDataManager.getInstance().getCurrentRoomFriendList().size()));
		return super.cancel();
=======
	public void AlertMessage(String sender,String msg){
		String response;
		response = String.format("alertmsg;%s;%s",msg,sender);
		MainActivity.getInstasnce().getmSAPService().SendByteData(response.getBytes());
	}
	
	public void sendFitnessinfo(double maxspeed,double avgspeed,double distance,double altitude,double kcal){
		String response = String.format("fitninfo;%.2f;%.2f;%.2f;%.2f;%.2f",maxspeed,avgspeed,distance,altitude,kcal);
		MainActivity.getInstasnce().getmSAPService().SendByteData(response.getBytes());
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	}
}
