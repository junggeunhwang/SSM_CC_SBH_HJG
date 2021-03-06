package com.ssm.cyclists.controller.asynctask;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.cyclists.model.CruiseDataManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class LocationInfoUpdateAsyncTask extends
		AsyncTask<Location,Integer,Long> {
	
	public static String TAG = LocationInfoUpdateAsyncTask.class.getSimpleName();
	
	@Override
	protected Long doInBackground(Location... loc) {
		
		Long result = 1L;
		
		//update elevation
		String strURL = String.format("http://maps.googleapis.com/maps/api/elevation/json?locations=%f,%f&sensor=true_or_false",loc[0].getLatitude(),loc[0].getLongitude()); 
		String jsonString = CruiseDataManager.getInstance().DownloadHtml(strURL);
		
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			JSONArray results = jsonObj.getJSONArray("results");
			CruiseDataManager.getInstance()
				.setElevation(Double.valueOf(String.format("%.2f", results.getJSONObject(0).getDouble("elevation"))));
			
			Log.d(TAG,"elevation ("+loc[0].getLatitude() +", "+loc[0].getLongitude()+") : " + CruiseDataManager.getInstance().getElevation());
		} catch (JSONException e) {
			Log.e(TAG,e.getLocalizedMessage());
			e.printStackTrace();
			result*=-1;
		}
		
		//update address
		StringBuffer juso = new StringBuffer();
  		boolean ok = Geocoder.isPresent();
 		if(!ok) Log.e(TAG,"Geocoder is not present. please check network.");
		List<Address> addresses;
		
		try {
 			addresses = CruiseDataManager.getInstance().getGeoCoder().getFromLocation(loc[0].getLatitude(), loc[0].getLongitude(),1);
   			for(Address addr: addresses){
 				int index = addr.getMaxAddressLineIndex();
  				for(int i = 0 ; i <= index ; i++){   
					juso.append(addr.getAddressLine(i));
					juso.append(" ");
				}
			}
			
			Address addr = addresses.get(0);

			CruiseDataManager.getInstance().setAddress(addr.getThoroughfare());
			
		} catch (IOException e) {
			Log.e(TAG,e.getLocalizedMessage());
			e.printStackTrace();
			result*=2;
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Long result) {
		if(result == -1L){
			Log.e(TAG,"Elevation update fail.");
		}else if(result == -2L){
			Log.e(TAG,"Address & Elevation update fail.");
		}else if(result == 2L){
			Log.e(TAG,"Address update fail.");
		}else{
			Log.d(TAG,"Locationinfo update success.");
		}
		super.onPostExecute(result);
	}
}
