package com.ssm.ExCycling.controller;

import java.util.TimerTask;

import android.util.Log;

import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.communication.https.HttpsCommunication;
import com.ssm.ExCycling.controller.communication.https.Protocol;

public class GetTask extends TimerTask { 

	static String TAG = GetTask.class.getSimpleName();
	@Override
	public void run() {

			HttpsCommunication httpsCommunication = new HttpsCommunication(Protocol.getInstance().getHttpsCallback());
			httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
			httpsCommunication.setUniqueNumber(MainActivity.getInstasnce().getMyNumber());
			httpsCommunication.setStringData("get");
			Log.d(TAG, "get");
			
			if(!httpsCommunication.ExecuteRequest()){
				Log.e(TAG, "get Failed");
			}
	}
}
