package com.ssm.cyclists.controller;

import java.sql.SQLException;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.model.CruiseDataManager;

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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXSITS last_location");
		onCreate(db);
		db.execSQL("DROP TABLE IF EXSITS settings");
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
			db.update("last_location",row,"num="+c.getInt(0),null);
		}
	}
	
	public GeoLocation selectLastLocation(){
		
		db = manager.getReadableDatabase();
		
		Cursor c = db.query("last_location", null,null,null,null,null,null);

		c.moveToNext();
		if(c.getCount()==0) return null;
		GeoLocation loc = new GeoLocation(Double.valueOf(c.getString(1)),Double.valueOf(c.getString(2)));
		
		return loc;
	}
	
	public void updateSettingInfo(){

		ContentValues row = new ContentValues();
		row.put("color",MainActivity.getInstasnce().getLayout().getmSettingsFragment().getLayout().getTheme_color());
		
		db = manager.getWritableDatabase();
		
		Cursor c = db.query("settings", null,null,null,null,null,null);
		
		int count = c.getCount();
		c.moveToNext();
		if(c.getCount()==0){
			db.insert("settings",null,row);
		}
		else{
			int ret = db.update("settings",row,"num="+c.getInt(0),null);
			Log.d(TAG, "Database affected row : "+String.valueOf(ret));
		}
	}
	
	public String selectSettingInfo(){
		
		db = manager.getReadableDatabase();
		
		Cursor c = db.query("settings", null,null,null,null,null,null);

		c.moveToNext();
		if(c.getCount()==0) return null;
		return c.getString(1);
	}
	
	public void close(){
		db.close();
	}
	
	static public DataBaseManager getInstance(){
		if(manager == null) manager = new DataBaseManager(MainActivity.getInstasnce(),"CyclistsDB.sqlite",null,1);
		return manager;
	}

}
