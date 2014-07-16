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
