package com.ssm.cyclists.controller;

import com.ssm.cyclists.R;
import com.ssm.cyclists.view.SplashLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	private SplashLayout layout;
	
	public SplashActivity() {
	}
	
	@SuppressLint("HandlerLeak") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		layout = new SplashLayout(this);
		setContentView(layout.getView());
		layout.init();

		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				finish();
				overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
				super.handleMessage(msg);
			}
		};
		
		handler.sendEmptyMessageDelayed(0, 2500);
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
