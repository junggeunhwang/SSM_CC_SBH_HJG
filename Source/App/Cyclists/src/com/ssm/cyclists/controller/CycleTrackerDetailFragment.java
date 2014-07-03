package com.ssm.cyclists.controller;

import com.ssm.cyclists.view.layout.CycleTrackerDetailLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleTrackerDetailFragment extends Fragment {

	CycleTrackerDetailLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		layout = new CycleTrackerDetailLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public CycleTrackerDetailLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

}
