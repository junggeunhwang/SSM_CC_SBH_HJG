package com.ssm.cyclists.controller;

import com.ssm.cyclists.view.CycleTrackerLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleTrackerFragment extends Fragment {

	CycleTrackerLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new CycleTrackerLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}
	
	
}
