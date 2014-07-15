package com.ssm.cyclists.controller.timertask;

import java.util.TimerTask;

import android.util.Log;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.communication.https.HttpsCommunication;
import com.ssm.cyclists.controller.communication.https.Protocol;
import com.ssm.cyclists.controller.manager.SettingsDataManager;

public class GetTask extends TimerTask { 

	static String TAG = GetTask.class.getSimpleName();
	@Override
	public void run() {

			HttpsCommunication httpsCommunication = new HttpsCommunication(Protocol.getInstance().getHttpsCallback());
			httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
			httpsCommunication.setUniqueNumber(SettingsDataManager.getInstance().getMe().getUniqueID());
			httpsCommunication.setStringData("get");
			Log.i(TAG, "get");
			
			if(!httpsCommunication.ExecuteRequest()){
				Log.e(TAG, "get Failed");
			}
	}
}
