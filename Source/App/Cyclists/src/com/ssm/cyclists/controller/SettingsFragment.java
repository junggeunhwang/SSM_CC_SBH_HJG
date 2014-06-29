package com.ssm.cyclists.controller;

import com.ssm.cyclists.view.CycleTrackerLayout;
import com.ssm.cyclists.view.SettingsLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {

	SettingsLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new SettingsLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}
}