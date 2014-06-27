package com.ssm.view;

import com.example.cyclists.R;
import com.ssm.controller.SplashActivity;

import android.widget.ImageView;



public class SplashLayout {

	private SplashActivity activity;
	private int view = R.layout.activity_splash;
	
	public SplashLayout(SplashActivity instance) {
		activity = instance;
	}
	
	public void init(){
		
	}
	
	public int getView(){
		return view;
	}
}
