package com.ssm.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.cyclists.R;
import com.ssm.controller.HomeFragment;

public class HomeLayout extends BaseFragmentLayout {
	
	Button menubutton;
	public HomeLayout(HomeFragment instance) {
		super(instance);
	}
	
	public void init(){
		
	}
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container){
		view = inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	private OnClickListener buildButtonClickListner(){
		
		return new Button.OnClickListener(){
				
					@Override
					public void onClick(View v) {

					}
				};
	}

}
