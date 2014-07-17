package com.ssm.cyclists.controller.timertask;

import java.util.TimerTask;

import android.util.Log;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.communication.https.HttpsCommunication;
import com.ssm.cyclists.controller.communication.https.Protocol;
import com.ssm.cyclists.controller.manager.SettingsDataManager;

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
