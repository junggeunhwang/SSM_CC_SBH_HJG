package com.ssm.cyclists.controller;

import java.util.TimerTask;

import android.util.Log;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.communication.https.HttpsCommunication;

public class GetTimer extends TimerTask { 

	static String TAG = GetTimer.class.getSimpleName();
	
	@Override
	public void run() {
		HttpsCommunication httpsCommunication = new HttpsCommunication(MainActivity.getInstasnce().getHttpsCallback());
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setUniqueNumber(MainActivity.getInstasnce().getMyNumber());
		httpsCommunication.setStringData("get");
		Log.d(TAG, "get");
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "get Failed");
		}
	}

}