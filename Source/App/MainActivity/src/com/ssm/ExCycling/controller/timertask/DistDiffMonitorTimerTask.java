package com.ssm.ExCycling.controller.timertask;

import java.util.TimerTask;

import android.util.Log;

import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;

public class DistDiffMonitorTimerTask extends TimerTask {

	static String TAG = DistDiffMonitorTimerTask.class.getSimpleName();
	final int alert_dist_const = 50;
	
	@Override
	public void run() {
		
		//거리 계산 경고
				for(int i = 0;i<SettingsDataManager.getInstance().getCurrentRoomFriendList().size();i++){
					try{
						double dist = CruiseDataManager.getInstance().getCurrent_loc().distanceTo
								(SettingsDataManager.getInstance().getCurrentRoomFriendList().get(i).getCurrentLocation());
						Log.d(TAG,"I to " + SettingsDataManager.getInstance().getCurrentRoomFriendList().get(i).getUserName()+ " : " + String.format("%.2f", dist));
						if(dist > alert_dist_const){
							AlertMessage(SettingsDataManager.getInstance().getCurrentRoomFriendList().get(i).getUserName(),
									"Warning! Difference is more than "+alert_dist_const+" m away.");
						}
					}catch(NullPointerException e){
						
					}
					
				}

	}

	public void AlertMessage(String sender,String msg){
		String response;
		response = String.format("alertmsg;%s;%s",msg,sender);
		Log.d(TAG,response);
		MainActivity.getInstasnce().getmSAPService().SendByteData(response.getBytes());
	}
}
