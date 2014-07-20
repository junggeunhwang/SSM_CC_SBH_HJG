package com.ssm.ExCycling.controller.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import com.ssm.ExCycling.controller.activity.MainActivity;


public class GoogleLocationManager implements LocationListener ,ConnectionCallbacks,OnConnectionFailedListener{
	
	public static String TAG = GoogleLocationManager.class.getSimpleName();
	
	LocationClient locationClient= null;
    boolean isReconnect = false;

    Context mContext;
    
    public boolean init(Context context){
    	mContext = context;
    	turnGPSOn();
    
    	
    	 // check Google Play service APK is available and up to date.
        // see http://developer.android.com/google/play-services/setup.html
        final int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            Toast.makeText(context, "Google Play service is not available (status=" + result + ")", Toast.LENGTH_LONG).show();
            return false;
        }
        locationClient= new LocationClient(context, this, this);
        LocationManager manager = (LocationManager)MainActivity.getInstasnce().getSystemService(Context.LOCATION_SERVICE);
        boolean state = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(state == false){
        	showSettingsAlert();
        }
        return true;
    }
	
    public void showSettingsAlert(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.getInstasnce());
    	
    	alertDialog.setTitle("GPS is settings");
    	
    	alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
    	
    	alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				MainActivity.getInstasnce().startActivity(intent);
				
			}
		});
    	
    	alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
    	
    	alertDialog.show();
    }
<<<<<<< HEAD
   
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    public void resume(){
    	 locationClient.connect();
    }
    
<<<<<<< HEAD
    
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    public void pause(){
    	isReconnect = false;
           locationClient.disconnect();
    }
    
    private void turnGPSOn(){
        String provider = Settings.Secure.getString(MainActivity.getInstasnce().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            MainActivity.getInstasnce().sendBroadcast(poke);
        }
    }
<<<<<<< HEAD
	
    @Override
=======
	@Override
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	public void onLocationChanged(Location location) {
		
		CruiseDataManager.getInstance().setCurrent_loc(location.getLatitude(),location.getLongitude());
		CruiseDataManager.getInstance().updateCruiseData();
		DataBaseManager.getInstance().updateLastLocation(location);
		
		
		if(MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmHomeFragment().isVisible()){
			MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmHomeFragment().updateHomeInfo();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.e(TAG, "Connection Failed");
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
	     Log.e(TAG, "Connection Success");
	}

	@Override
	public void onDisconnected() {
	}

}
