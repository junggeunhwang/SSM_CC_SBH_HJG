package com.ssm.cyclists.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.asynctask.LocationInfoUpdateAsyncTask;
import com.ssm.cyclists.controller.asynctask.WeatherUpdateAsyncTask;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

public class CruiseDataManager {
	static CruiseDataManager instance;
	static String TAG = CruiseDataManager.class.getSimpleName();
	
	private int weather_resID;
	private int temperature;
	private int humidity;
	private int calory;
	private int distnace;
	private int maximum_speed;
	private Location current_loc;
	private double elevation;
	private double current_speed;
	private String curarent_address;
	
	
	private Geocoder geoCoder;
	
	private CruiseDataManager(){
		geoCoder 	  = new Geocoder(MainActivity.getInstasnce(),Locale.KOREA);
		humidity 	  = 0;
		elevation 	  = 0;		
		temperature   = 0;
		calory		  = 0;
		distnace	  = 0;
		maximum_speed = 0;
		curarent_address = "";
		
		
		// Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
 
        // Getting the name of the best provider
        String provider = ((LocationManager) MainActivity.getInstasnce().getSystemService(Context.LOCATION_SERVICE)).getBestProvider(criteria, true);
        current_loc = new Location(provider);
        current_loc.setLatitude(37.561372);
        current_loc.setLongitude(127.037171);
	}
	
	public static CruiseDataManager getInstance(){
 		if(instance == null){
			instance = new CruiseDataManager();
		}
		return instance;
	}
	
	public void updateCruiseData(){
		WeatherUpdateAsyncTask weather_update_task = new WeatherUpdateAsyncTask();
		weather_update_task.execute(current_loc);
		
		LocationInfoUpdateAsyncTask location_info_update_task = new LocationInfoUpdateAsyncTask();
		location_info_update_task.execute(current_loc);
	}
	
	public String DownloadHtml(String addr){
		StringBuilder html = new StringBuilder();
		
		try{
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if(conn != null){
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					for(;;){
						String line = br.readLine();
						if(line == null)break;
						html.append(line + '\n');
					}
					br.close();
				}
				conn.disconnect();
			}
		
		}catch(Exception e){
			Log.e(TAG,e.getMessage());
		}
		return html.toString();
	}
	
	public int getWeather() {
		return weather_resID;
	}

	public int getHumidity() {
		return humidity;
	}

	public Location getCurrent_loc() {
		return current_loc;
	}

	public double getElevation() {
		return elevation;
	}

	public double getCurrent_speed() {
		return current_speed;
	}

	public int getTemperature() {
		return temperature;
	}
	
	public String getAddress() {
		return curarent_address;
	}

	public Geocoder getGeoCoder() {
		return geoCoder;
	}

	public int getCalory() {
		return calory;
	}

	public int getDistnace() {
		return distnace;
	}

	public int getMaximum_speed() {
		return maximum_speed;
	}

	public void setDistnace(int distnace) {
		this.distnace = distnace;
	}

	public void setMaximum_speed(int maximum_speed) {
		this.maximum_speed = maximum_speed;
	}

	public void setCalory(int calory) {
		this.calory = calory;
	}

	public void setAddress(String address) {
		this.curarent_address = address;
	}

	public void setWeather(int weather) {
		this.weather_resID = weather;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public void setCurrent_loc(double latitude,double longitude) {
		this.current_loc.setLatitude(latitude);
		this.current_loc.setLongitude(longitude);
		
		MainActivity.getInstasnce().getLayout().getmFragmentHome().updateHomeInfo();
		MainActivity.getInstasnce().getLayout().getmFragmentCruise().updateCruiseInfo();
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	public void setCurrent_speed(double current_speed) {
		this.current_speed = current_speed;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	
}

