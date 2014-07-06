package com.ssm.cyclists.model;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.*;
import com.ssm.cyclists.controller.DataBaseManager;
import com.ssm.cyclists.controller.activity.MainActivity;


public class GoogleLocationManager implements LocationListener ,ConnectionCallbacks,OnConnectionFailedListener{
	
	public static String TAG = GoogleLocationManager.class.getSimpleName();
	
	LocationClient locationClient= null;
    boolean isReconnect = false;

    Context mContext;
    
    public boolean init(Context context){
    	mContext = context;
    	
    	 // check Google Play service APK is available and up to date.
        // see http://developer.android.com/google/play-services/setup.html
        final int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            Toast.makeText(context, "Google Play service is not available (status=" + result + ")", Toast.LENGTH_LONG).show();
            return false;
        }
        locationClient= new LocationClient(context, this, this);
        return true;
    }
	
    public void resume(){
    	 locationClient.connect();
    }
    
    public void pause(){
    	isReconnect = false;
           locationClient.disconnect();
    }
	@Override
	public void onLocationChanged(Location location) {
		if(location.hasSpeed()) Log.d(TAG,"hasSpeed : true");
		else Log.d(TAG,"hasSpeed : false");
		
		
		CruiseDataManager.getInstance().setCurrent_loc(location.getLatitude(),location.getLongitude());
		CruiseDataManager.getInstance().updateCruiseData();
//		Toast.makeText(mContext, String.format("getLatitude,Longitude :%s",
//				 CruiseDataManager.getInstance().getCurrent_loc().getLatitude()+","+CruiseDataManager.getInstance().getCurrent_loc().getLongitude()+"(Speed : "+CruiseDataManager.getInstance().getCurrent_speed()+")"), 
//				Toast.LENGTH_SHORT).show();
		
		DataBaseManager.getInstance().updateLastLocation(location);				
		if(MainActivity.getInstasnce().getLayout().getmMapViewFragment().isVisible()){
			MainActivity.getInstasnce().getLayout().getmMapViewFragment().moveMapCamenra(location);
		}
		if(MainActivity.getInstasnce().getLayout().getmFragmentCruise().isVisible()){
			MainActivity.getInstasnce().getLayout().getmFragmentCruise().updateCruiseInfo();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(mContext, "Connection Failed", Toast.LENGTH_LONG).show();
	}
	 
	@Override
	public void onConnected(Bundle arg0) {
		 isReconnect = true;
		 LocationRequest req = LocationRequest.create();
		 
	     req.setFastestInterval(500);
	     req.setInterval(500);
	     req.setSmallestDisplacement(0);
	     req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	     locationClient.requestLocationUpdates(req, this);
	     Toast.makeText(mContext, "Connection Success", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisconnected() {
//		 if (isReconnect) start();
	}

}
