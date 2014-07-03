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
import com.ssm.cyclists.controller.MainActivity;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class CruiseDataManager {
	static CruiseDataManager instance;
	static String TAG = CruiseDataManager.class.getSimpleName();
	
	private int weather_resID;
	private int temperature;
	private int humidity;
	private Location current_loc;
	private double elevation;
	private double current_speed;
	private String curarent_address;
	
	private Geocoder geoCoder;
	
	private CruiseDataManager(){
		geoCoder = new Geocoder(MainActivity.getInstasnce(),Locale.KOREA);
		humidity = 0;
		elevation = 0;		
		temperature = 0;
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
//		updateLocation();
		updateWeather(current_loc);
		updateElevation(current_loc);
		updateAddress(current_loc);
//		MainActivity.getInstasnce().getLayout().getmFragmentHome().updateHomeInfo();
//		MainActivity.getInstasnce().getLayout().getmFragmentCruise().updateCruiseInfo();
	}
	
	public void updateLocation(){
		
//		current_loc = MainActivity.getInstasnce().getLayout().getmMapViewFragment().getLayout().getMapInstance().getMyLocation();
		
		if(current_loc==null){
			current_loc = MainActivity.getInstasnce().getLayout().getmMapViewFragment().getLayout().getMapInstance().getMyLocation();
			if(current_loc == null){
				Log.d(TAG,"null location");
			 	// Creating a criteria object to retrieve provider
	            Criteria criteria = new Criteria();
	     
	            // Getting the name of the best provider
	            String provider = ((LocationManager) MainActivity.getInstasnce().getSystemService(Context.LOCATION_SERVICE)).getBestProvider(criteria, true);
	            current_loc = new Location(provider);
	            current_loc.setLatitude(37.561372);
	            current_loc.setLongitude(127.037171);
			}
			 	
		}
	}
	
	public void updateWeather(final Location loc){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				String strURL = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f",loc.getLatitude(),loc.getLongitude()); 
				String jsonString = DownloadHtml(strURL);
				try {
					JSONObject jsonObj = new JSONObject(jsonString);
					JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
					Log.d(TAG, "id : "+weather.getInt("id") +" weather : " + weather.getString("main"));

					final JSONObject main = jsonObj.getJSONObject("main");
						
					int weather_id = weather.getInt("id");
					
					if(weather_id >=200 && weather_id < 300)		//Thunderstorm
					{
						weather_resID = R.drawable.thunder;
					}
					else if(weather_id >=300 && weather_id < 400)	//Drizzle
					{
						weather_resID = R.drawable.rainy;
					}
					else if(weather_id >=400 && weather_id < 500)	//empty
					{
					
					}
					else if(weather_id >=500 && weather_id < 600)	//Rain
					{
						weather_resID = R.drawable.rainy;
					}
					else if(weather_id >=600 && weather_id < 700)	//Snow
					{
						weather_resID = R.drawable.rainy;
					}
					else if(weather_id >=700 && weather_id < 800)	//Atmosphere
					{
						weather_resID = R.drawable.cloudy;
					}
					else if(weather_id >=800 && weather_id < 900)	//Clouds
					{
						if(weather_id == 800){
							weather_resID = R.drawable.sunny;
						}
						else{
							weather_resID = R.drawable.cloudy;
						}
					}
					else
					{
						weather_resID = R.drawable.sunny;
					}

					humidity = main.getInt("humidity");
					temperature = ((int)Math.round((main.getDouble("temp")/10)));
										
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
}
	
	public void updateElevation(final Location loc){
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				double elevation = 0;
				String strURL = String.format("http://maps.googleapis.com/maps/api/elevation/json?locations=%f,%f&sensor=true_or_false",loc.getLatitude(),loc.getLongitude()); 
				String jsonString = DownloadHtml(strURL);
				
				try {
					JSONObject jsonObj = new JSONObject(jsonString);
					JSONArray results = jsonObj.getJSONArray("results");
					elevation = results.getJSONObject(0).getDouble("elevation");
					elevation = Double.valueOf(String.format("%.2f", elevation));
					Log.d(TAG,"elevation ("+loc.getLatitude() +", "+loc.getLongitude()+") : " + elevation);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public void updateAddress(Location loc){
  		StringBuffer juso = new StringBuffer();
  		boolean ok = Geocoder.isPresent();
 		if(!ok) Log.e(TAG,"Geocoder is not present. please check network.");
		List<Address> addresses;
		
		try {
 			addresses = geoCoder.getFromLocation(loc.getLatitude(), loc.getLongitude(),1);
   			for(Address addr: addresses){
 				int index = addr.getMaxAddressLineIndex();
  				for(int i = 0 ; i <= index ; i++){   
					juso.append(addr.getAddressLine(i));
					juso.append(" ");
				}
			}
			int space=0;
			
 			for(int i = 0;i<2;i++)
				space= juso.indexOf(" ",space) + 1;
			curarent_address = juso.subSequence(space, juso.indexOf(" ",space)).toString(); 
 			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String DownloadHtml(String addr){
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

