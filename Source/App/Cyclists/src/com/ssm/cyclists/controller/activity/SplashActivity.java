package com.ssm.cyclists.controller.activity;

import twitter4j.GeoLocation;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.DataBaseManager;
import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.model.ResourceManager;
import com.ssm.cyclists.view.layout.SplashLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {

	private SplashLayout layout;
	private String theme_color; 
	public SplashActivity() {
	}
	
	@SuppressLint("HandlerLeak") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		
		layout = new SplashLayout(this);
		setContentView(layout.getView());
		theme_color = getIntent().getStringExtra("color");
		
		LinearLayout lyBackground = (LinearLayout)findViewById(R.id.background_splash);
		
		if(theme_color.equals("pink")){
			lyBackground.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.splash_pink));
		}else if(theme_color.equals("green")){
			lyBackground.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.splash_green));
		}else if(theme_color.equals("gray")){
			lyBackground.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.splash_gray));
		}
		
		layout.init();
		
		ResourceManager.getInstance().setAssetManager(getAssets());

		
		GeoLocation location = DataBaseManager.getInstance().selectLastLocation();
		if(location != null){
			CruiseDataManager.getInstance().setCurrent_loc(location.getLatitude(),location.getLongitude());
		}
	
		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				finish();
				overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
				super.handleMessage(msg);
			}
		};

		handler.sendEmptyMessageDelayed(0, 1500);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
//		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
