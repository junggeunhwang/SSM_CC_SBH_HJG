package com.ssm.view;

import com.example.cyclists.R;
import com.ssm.controller.SplashActivity;

import android.widget.ImageView;



public class SplashLayout {

	private ImageView splash_image;
	private SplashActivity activity;
	private int view = R.layout.activity_splash;
	
	public SplashLayout(SplashActivity instance) {
		activity = instance;
	}
	
	public void init(){
		splash_image = (ImageView)activity.findViewById(R.id.splash_imageview);
	}
	
	public int getView(){
		return view;
	}
}
