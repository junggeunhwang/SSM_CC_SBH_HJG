package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.SplashActivity;
import com.ssm.cyclists.model.ResourceManager;

import android.widget.TextView;

public class SplashLayout {

	static String TAG = SplashLayout.class.getSimpleName();
	
	
	private SplashActivity activity;
	private int view = R.layout.activity_splash;
	
	public SplashLayout(SplashActivity instance) {
		activity = instance;
	}
	
	public void init(){
		
		((TextView)activity.findViewById(R.id.app_name_splash))
		.setTypeface(ResourceManager.getInstance().getFont("helvetica"));		
	}
	
	public int getView(){
		return view;
	}
}
