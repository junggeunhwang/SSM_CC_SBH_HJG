package com.ssm.cyclists.controller.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.asynctask.LocationInfoUpdateAsyncTask;
import com.ssm.cyclists.controller.asynctask.WeatherUpdateAsyncTask;
import com.ssm.cyclists.model.CycleData;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class CruiseDataManager {
	static CruiseDataManager instance;
	static String TAG = CruiseDataManager.class.getSimpleName();
	
	private int weather_resID;
	private int temperature;
	private int humidity;
	private int calory;
	private double distnace;
	private double maximum_speed;
	private Location current_loc;
	private double elevation;
	private double current_speed;
	private String curarent_address;
	private double wind;
	private double wind_direction;
	
	//운동 시작 시간
	private String ElapsedTime;
	//마지막 지도 업데이트 시간
	private long last_location_update_time;
	
	//사용자 정보
	private int user_weight;
	private long last_calory_update_time; 
	
	//방 인원수
	private int room_member_count;
	//flag
	private int update_count;
	private boolean firstLocationUpdateFlag; 
	
	private Geocoder geoCoder;
	
	private ArrayList<CycleData> cycle_data_list;
	
	private long startTimemillies;
	
	private CruiseDataManager(){
		geoCoder 	  = new Geocoder(MainActivity.getInstasnce(),Locale.ENGLISH);
		humidity 	  = 0;
		elevation 	  = 0;		
		temperature   = 0;
		calory		  = 0;
		distnace	  = 0;
		maximum_speed = 0;
		update_count  = 15;
		ElapsedTime = "00 : 00 : 00";
		last_location_update_time = 0;
		curarent_address = "Calculating the position...";
		firstLocationUpdateFlag = false;
		
		user_weight = 70;// test
		
		cycle_data_list = new ArrayList<CycleData>();
		
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
		if(update_count%7==0){
			LocationInfoUpdateAsyncTask location_info_update_task = new LocationInfoUpdateAsyncTask();
			location_info_update_task.execute(current_loc);	
		}
		
		
		if(update_count==15){
			WeatherUpdateAsyncTask weather_update_task = new WeatherUpdateAsyncTask();
			weather_update_task.execute(current_loc);
			update_count=1;
		}
		update_count++;
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
	
	public void clear(){
		calory		  = 0;
		distnace	  = 0;
		maximum_speed = 0;
		ElapsedTime = "00 : 00 : 00";
	}
	
	public Double calcCalory(double velocity){
		long current_time = System.currentTimeMillis();
		if(last_calory_update_time == 0) last_calory_update_time = current_time;
		
		long diff = (current_time - last_calory_update_time)/1000;
		last_calory_update_time = current_time;
		
		double unitCalory = 0.01033333333333;
		return unitCalory*user_weight*velocity*diff;
	}
	
	public CycleData getCycleData(String date){

		for(int i = 0 ; i < cycle_data_list.size() ; i++){
			if(cycle_data_list.get(i).getDate().equals(date)){
				return cycle_data_list.get(i);
			}
		}
		
		return null;
	}
	
	public Double getAvgSpeed(){
		
		
		return null;
	}
	
	public ArrayList<CycleData> getCycle_data_list() {
		return cycle_data_list;
	}

	public void setCycle_data_list(ArrayList<CycleData> cycle_data_list) {
		this.cycle_data_list = cycle_data_list;
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

	public double getDistnace() {
		return distnace;
	}

	public double getMaximum_speed() {
		return maximum_speed;
	}


	public String getElapsedTime() {
		return ElapsedTime;
	}

	public double getWind() {
		return wind;
	}

	public void setWind(double wind) {
		this.wind = wind;
	}

	public double getWind_direction() {
		return wind_direction;
	}

	public void setWind_direction(double wind_direction) {
		this.wind_direction = wind_direction;
	}

	public void setElapsedTime(String elapsedTime) {
		ElapsedTime = elapsedTime;
	}

	public void setDistnace(double distnace) {
		this.distnace = distnace;
	}

	public void setMaximum_speed(double maximum_speed) {
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
		
		long current_location_update_time = System.currentTimeMillis(); 
		if(last_location_update_time==0) last_location_update_time = current_location_update_time;
		
		long dif = (current_location_update_time - last_location_update_time)/1000;
		if(dif == 0) dif = 1;
		last_location_update_time = current_location_update_time;
		Log.d(TAG,"dif = "+String.valueOf(dif));
		
		if(firstLocationUpdateFlag == true){//처음 위치정보 무시
			Location loc = new Location("gps");
			loc.setLatitude(latitude);
			loc.setLongitude(longitude);

			//거리계산
			double dist = current_loc.distanceTo(loc)/1000;//단위는 km
			dist = Double.valueOf(String.format("%.2f", dist));
			this.distnace += dist;
			
			//현재 속도 계산 (km/h)
			this.current_speed = Double.valueOf(String.format("%.2f",(current_loc.distanceTo(loc)/dif)*3.6));
			
			//칼로리 계산
			calory += calcCalory(this.current_speed);
			
			//최고 속도 판정
			if(this.maximum_speed < this.current_speed)
				this.maximum_speed = this.current_speed;
		}
		else{
			firstLocationUpdateFlag = true;
		}
	
		this.current_loc.setLatitude(latitude);
		this.current_loc.setLongitude(longitude);
		
//		MainActivity.getInstasnce().getLayout().getmFragmentCruise().updateCruiseInfo();
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

