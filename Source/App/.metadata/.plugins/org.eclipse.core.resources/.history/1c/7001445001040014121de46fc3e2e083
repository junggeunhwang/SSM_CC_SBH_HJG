package com.ssm.cyclists.controller;

import com.ssm.cyclists.view.layout.CycleMateLayout;
import com.ssm.cyclists.view.layout.HomeLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleMateFragment extends Fragment {

	private CycleMateLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new CycleMateLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public CycleMateLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

}
