package com.ssm.controller;

import com.ssm.view.MapLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

	MapLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = new MapLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}
}
