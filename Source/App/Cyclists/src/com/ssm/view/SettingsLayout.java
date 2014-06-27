package com.ssm.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.example.cyclists.R;
import com.ssm.controller.MainActivity;
import com.ssm.controller.SettingsFragment;

public class SettingsLayout extends BaseFragmentLayout {

	public SettingsLayout(SettingsFragment instance) {
		super(instance);
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_settings, container, false);
	}
	
	public void init(){
		
	}
		
	private OnClickListener buildButtonClickListner(){
		return new OnClickListener(){
				
					@Override
					public void onClick(View v) {
						
					}
				};
	}

}