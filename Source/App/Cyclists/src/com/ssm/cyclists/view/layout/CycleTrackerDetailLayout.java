package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class CycleTrackerDetailLayout extends BaseFragmentLayout {

	
	public CycleTrackerDetailLayout(Fragment instance) {
		super(instance);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cycle_tracker_detail, container, false);		
	}
	public void init(){
	}

}
