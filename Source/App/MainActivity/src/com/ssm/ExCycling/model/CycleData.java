package com.ssm.ExCycling.model;

import java.util.ArrayList;

import android.location.Location;

import com.jjoe64.graphview.GraphView.GraphViewData;



public class CycleData {

	private String date;
	private ArrayList<Double> arDistance;
	
	private ArrayList<String> arSpeed;
	private ArrayList<String> arAltitude;
	private ArrayList<String> arConsumeCalrories;
	private ArrayList<Location> arLocation;
	private ArrayList<String> arTimeStamp;
	
<<<<<<< HEAD
	private String temp;
	private String humidity;
	private String wind;
	private String wind_dir;
	private String mem_count;
	
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	public CycleData() {
		arConsumeCalrories = new ArrayList<String>();
	}

	public double getDistance(){
		double sum=0;
		for(int i=0;i<arDistance.size();i++){
			sum+= arDistance.get(i);
		}
		return sum;
	}
	

	
	public String getDate() {
		return date;
	}

	public ArrayList<String> getSpeedList() {
		return arSpeed;
	}

	public ArrayList<String> getAltitudeList() {
		return arAltitude;
	}

	public ArrayList<String> getConsumeCalroriesList() {
		return arConsumeCalrories;
	}
	
	public Double getTotalConsumeCalrories(){
		
		double totalCalory = 0;
		
		for(int i = 0;i < arConsumeCalrories.size() ; i++){
			totalCalory += Double.valueOf(arConsumeCalrories.get(i));
		}
		return totalCalory;
	}

	public ArrayList<Location> getLocationList() {
		return arLocation;
	}

	public ArrayList<Double> getDistanceList() {
		return arDistance;
	}

	public ArrayList<String> getArTimeStampList() {
		return arTimeStamp;
	}

<<<<<<< HEAD
	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getWind_dir() {
		return wind_dir;
	}

	public void setWind_dir(String wind_dir) {
		this.wind_dir = wind_dir;
	}

	public String getMem_count() {
		return mem_count;
	}

	public void setMem_count(String mem_count) {
		this.mem_count = mem_count;
	}

=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	public void setArTimeStampList(ArrayList<String> arTimeStamp) {
		this.arTimeStamp = arTimeStamp;
	}

	public void setDistanceList(ArrayList<Double> arDistance) {
		this.arDistance = arDistance;
	}

	public void setStartTime(String cycle_date) {
		this.date = cycle_date;
	}
	
	public void setSpeedList(ArrayList<String> arSpeed) {
		this.arSpeed = arSpeed;
	}

	public void setAltitudeList(ArrayList<String> arAltitude) {
		this.arAltitude = arAltitude;
	}

	public void setConsumeCalroriesList(ArrayList<String> arConsumeCalrories) {
		this.arConsumeCalrories = arConsumeCalrories;
	}

	public void setLocationList(ArrayList<Location> arLocation) {
		this.arLocation = arLocation;
	}

}
