package com.ssm.cyclists.controller.asynctask;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.manager.CruiseDataManager;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class WeatherUpdateAsyncTask extends AsyncTask<Location, Integer, Long> {

	public static String TAG = WeatherUpdateAsyncTask.class.getSimpleName();
	// src : http://www.openweathermap.org/weather-conditions
	
	@Override
	protected Long doInBackground(Location... loc) {
		String strURL = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f",loc[0].getLatitude(),loc[0].getLongitude()); 
		String jsonString = CruiseDataManager.getInstance().DownloadHtml(strURL);
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
			Log.d(TAG, "id : "+weather.getInt("id") +" weather : " + weather.getString("main"));

			final JSONObject wind = jsonObj.getJSONObject("wind");
			CruiseDataManager.getInstance().setWind(wind.getDouble("speed"));
			Log.d(TAG,"wind : "  + CruiseDataManager.getInstance().getWind());
			CruiseDataManager.getInstance().setWind_direction(wind.getDouble("deg"));
			Log.d(TAG,"wind dir : " + CruiseDataManager.getInstance().getWind_direction());
			
			final JSONObject main = jsonObj.getJSONObject("main");
				
			int weather_id = weather.getInt("id");
			
			if(weather_id >=200 && weather_id < 300)		//Thunderstorm
			{
				if(weather_id == 202 || weather_id == 212 ||weather_id == 221 || weather_id == 232 )
					CruiseDataManager.getInstance().setWeather(R.drawable.thunder_night);
				else
					CruiseDataManager.getInstance().setWeather(R.drawable.thunder);
			}
			else if(weather_id >=300 && weather_id < 400)	//Drizzle
			{
				CruiseDataManager.getInstance().setWeather(R.drawable.drizzle);
			}
			else if(weather_id >=400 && weather_id < 500)	//empty
			{
			
			}
			else if(weather_id >=500 && weather_id < 600)	//Rain
			{
				if(weather_id == 502 || weather_id == 503 ||weather_id == 504 || weather_id == 522 || weather_id == 531)
					CruiseDataManager.getInstance().setWeather(R.drawable.rainy);
				else{
					int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
					if(hour > 7 && hour < 18)
						CruiseDataManager.getInstance().setWeather(R.drawable.rainy_noon);
					else
						CruiseDataManager.getInstance().setWeather(R.drawable.rainy_night);
				}
			}
			else if(weather_id >=600 && weather_id < 700)	//Snow
			{
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if(hour > 7 && hour < 18)
					CruiseDataManager.getInstance().setWeather(R.drawable.snow_noon);
				else
					CruiseDataManager.getInstance().setWeather(R.drawable.snow_night);
				
			}
			else if(weather_id >=700 && weather_id < 800)	//Atmosphere
			{
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if(hour > 7 && hour < 18)
					CruiseDataManager.getInstance().setWeather(R.drawable.cloudy);
				else
					CruiseDataManager.getInstance().setWeather(R.drawable.cloudy_night);

			}
			else if(weather_id >=800 && weather_id < 900)	//Clouds
			{
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				
				if(weather_id == 800){
					if(hour > 7 && hour < 18)
						CruiseDataManager.getInstance().setWeather(R.drawable.sunny);
					else
						CruiseDataManager.getInstance().setWeather(R.drawable.clear_moon);
				}
				else if(weather_id == 801 || weather_id == 802){
					CruiseDataManager.getInstance().setWeather(R.drawable.cloudy);
				}
				else if(weather_id == 804){
					CruiseDataManager.getInstance().setWeather(R.drawable.heavy_cloudy);
				}
				else{
					if(hour > 7 && hour < 18)
						CruiseDataManager.getInstance().setWeather(R.drawable.cloudy);
					else
						CruiseDataManager.getInstance().setWeather(R.drawable.cloudy_night);	
				}
			}
			else
			{
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				
				if(weather_id == 901)CruiseDataManager.getInstance().setWeather(R.drawable.rainy);
				else if(weather_id == 902)CruiseDataManager.getInstance().setWeather(R.drawable.heavy_cloudy);
				else if(weather_id == 904)CruiseDataManager.getInstance().setWeather(R.drawable.sunny);
				else if(weather_id == 905)CruiseDataManager.getInstance().setWeather(R.drawable.heavy_cloudy);
				else if(weather_id == 902)CruiseDataManager.getInstance().setWeather(R.drawable.rainy);
				else {
					if(hour > 7 && hour < 18)
						CruiseDataManager.getInstance().setWeather(R.drawable.cloudy);
					else
						CruiseDataManager.getInstance().setWeather(R.drawable.cloudy_night);	
				}
			}
			CruiseDataManager.getInstance().setHumidity(main.getInt("humidity"));
			CruiseDataManager.getInstance().setTemperature(((int)Math.round((main.getDouble("temp")-276.15))));
			
								
		} catch (JSONException e) {
			Log.e(TAG,e.getLocalizedMessage());
			e.printStackTrace();
			return 1L;
		}
	
		return 0L;
	}
	
	@Override
	protected void onPostExecute(Long result) {
		if(result == 1L) Log.e(TAG,"Weather update fail.");
		else{
			Log.d(TAG,"Weather update success.");
			try{
				MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().getmHomeFragment().updateHomeInfo();
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		}
		super.onPostExecute(result);
	}

}
