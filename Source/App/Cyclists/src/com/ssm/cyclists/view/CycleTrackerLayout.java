package com.ssm.cyclists.view;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.CycleTrackerFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;



public class CycleTrackerLayout extends BaseFragmentLayout {
	
	public CycleTrackerLayout(CycleTrackerFragment instance) {
		super(instance);
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container){
		view = inflater.inflate(R.layout.fragment_cycle_tracker, container, false);
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