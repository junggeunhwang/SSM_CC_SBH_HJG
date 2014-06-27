package com.ssm.view;

import com.example.cyclists.R;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MapLayout extends BaseFragmentLayout {

	public MapLayout(Fragment instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_map, container, false);
	}
	
	public void init(){
		
	}

}
