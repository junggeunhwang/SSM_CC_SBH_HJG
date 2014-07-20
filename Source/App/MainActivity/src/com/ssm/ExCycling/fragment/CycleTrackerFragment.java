package com.ssm.ExCycling.fragment;

import com.ssm.ExCycling.model.CycleData;
import com.ssm.ExCycling.view.layout.CycleTrackerLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleTrackerFragment extends Fragment {

	CycleTrackerLayout layout;
	
	CycleData currentActivatedData;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new CycleTrackerLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public CycleTrackerLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

	public CycleData getCurrentActivatedData() {
		return currentActivatedData;
	}

	public void setCurrentActivatedData(CycleData currentActivatedData) {
		this.currentActivatedData = currentActivatedData;
	}

}
