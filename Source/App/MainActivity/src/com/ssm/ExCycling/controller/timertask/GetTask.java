package com.ssm.ExCycling.controller.timertask;

import java.util.TimerTask;

import android.util.Log;

import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.communication.https.HttpsCommunication;
import com.ssm.ExCycling.controller.communication.https.Protocol;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;

public class GetTask extends TimerTask { 

	static String TAG = GetTask.class.getSimpleName();
	boolean state = true;
	@Override
	public void run() {

		if(state){
			HttpsCommunication httpsCommunication = new HttpsCommunication(Protocol.getInstance().getHttpsCallback());
			httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
			httpsCommunication.setUniqueNumber(SettingsDataManager.getInstance().getMe().getUniqueID());
			httpsCommunication.setStringData("get");
			Log.i(TAG, "get");
			
			if(!httpsCommunication.ExecuteRequest()){
				Log.e(TAG, "get Failed");
			}
		}
		else{
			this.cancel();
		}
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	
}
