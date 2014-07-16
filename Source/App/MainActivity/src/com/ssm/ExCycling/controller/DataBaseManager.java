package com.ssm.ExCycling.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.model.CycleData;
import com.ssm.ExCycling.model.SettingsData;

import twitter4j.GeoLocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class DataBaseManager extends SQLiteOpenHelper {

	static String TAG = DataBaseManager.class.getSimpleName();
	final int NUM 		= 0;
	final int LATITUDE 	= 1;
	final int LONGITUDE = 2;

	final int COLOR 	= 1;
	
	final int DATE 		= 1;
	final int VELOCITY 	= 2;
	final int ALTITUDE 	= 3;
	final int DISTANCE 	= 4;
	final int CALORY	= 5;
	final int LATITUDE_CRUISEDATA = 6;
	final int LONGITUDE_CRUISEDATA = 7;
	
	static DataBaseManager manager;
	SQLiteDatabase db;
	Context context;

	private DataBaseManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.manager = this;
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE last_location("+
				"num INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"latitude TEXT,"+
				"longitude TEXT);";
		db.execSQL(sql);
		
		String sql2 = "CREATE TABLE settings("+
				"num INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"color TEXT);";
		db.execSQL(sql2);
		
		String sql3 = "CREATE TABLE cruise_data("+
				"num INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"date TEXT," +
				"velocity TEXT," +
				"altitude TEXT," +
				"distance TEXT," +
				"calory TEXT," +
				"lattitude TEXT," +
				"longitude TEXT);";
		db.execSQL(sql3);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXSITS last_location");
		onCreate(db);
		db.execSQL("DROP TABLE IF EXSITS settings");
		onCreate(db);
		db.execSQL("DROP TABLE IF EXSITS cruise_data");
		onCreate(db);
	}
	
	public void updateLastLocation(Location loc){

		ContentValues row = new ContentValues();
		row.put("latitude",String.valueOf(loc.getLatitude()));
		row.put("longitude",String.valueOf(loc.getLongitude()));
		
		db = manager.getWritableDatabase();
		
		Cursor c = db.query("last_location", null,null,null,null,null,null);
		
		int count = c.getCount();
		c.moveToNext();
		if(c.getCount()==0){
			db.insert("last_location",null,row);
		}
		else{
			db.update("last_location",row,"num="+c.getInt(NUM),null);
		}
	}
	
	public GeoLocation selectLastLocation(){
		
		db = manager.getReadableDatabase();
		
		Cursor c = db.query("last_location", null,null,null,null,null,null);

		c.moveToNext();
		if(c.getCount()==0) return null;
		GeoLocation loc = new GeoLocation(Double.valueOf(c.getString(LATITUDE)),Double.valueOf(c.getString(LONGITUDE)));
		
		return loc;
	}
	
	public void updateSettingInfo(){

		ContentValues row = new ContentValues();
		row.put("color",SettingsData.getInstance().getThemeColor());
		
		db = manager.getWritableDatabase();
		
		Cursor c = db.query("settings", null,null,null,null,null,null);
		
		int count = c.getCount();
		c.moveToNext();
		if(c.getCount()==0){
			db.insert("settings",null,row);
		}
		else{
			int ret = db.update("settings",row,"num="+c.getInt(NUM),null);
			Log.d(TAG, "Database affected row : "+String.valueOf(ret));
		}
	}
	
	public String selectSettingInfo(){
		
		db = manager.getReadableDatabase();
		
		Cursor c = db.query("settings", null,null,null,null,null,null);

		c.moveToNext();
		if(c.getCount()==0) return null;
		return c.getString(COLOR);
	}
	
	public void insertCruiseData(String date,String velocity,String altitude,String distance,String calory){
		ContentValues row = new ContentValues();
		row.put("date",date);
		row.put("velocity",velocity);
		row.put("altitude",altitude);
		row.put("distance",distance);
		row.put("calory",calory);
		
		db = manager.getWritableDatabase();
		db.insert("settings",null,row);
		
	}
	

	
	public void close(){
		db.close();
	}
	
	static public DataBaseManager getInstance(){
		if(manager == null) manager = new DataBaseManager(MainActivity.getInstasnce(),"CyclistsDB.sqlite",null,1);
		return manager;
	}

}
