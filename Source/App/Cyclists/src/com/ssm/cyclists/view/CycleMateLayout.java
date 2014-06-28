package com.ssm.cyclists.view;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.CycleMateFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class CycleMateLayout extends BaseFragmentLayout {

	public CycleMateLayout(CycleMateFragment instance) {
		super(instance);
	}
	
	public void init(){
		
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cycle_mate, container, false);
	}
	
	private OnClickListener buildButtonClickListner(){
		return new OnClickListener(){
				
					@Override
					public void onClick(View v) {
						
					}
				};
	}

	
}
