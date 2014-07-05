package com.ssm.cyclists.controller;

import com.ssm.cyclists.view.layout.CruiseLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CruiseFragment extends Fragment {
	
	private CruiseLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = new CruiseLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public void updateCruiseInfo(){
		layout.updateCruiseInfo();
	}

	public CruiseLayout getLayout() {
		assert(layout!=null);
		return layout;
	}
}
