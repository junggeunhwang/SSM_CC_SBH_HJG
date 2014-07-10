package com.ssm.cyclists.controller.fragment;

import com.ssm.cyclists.view.layout.CheckableCycleMateLayout;
import com.ssm.cyclists.view.layout.CycleMateLayout;
import com.ssm.cyclists.view.layout.HomeLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CheckableCycleMateFragment extends Fragment {

	private CheckableCycleMateLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new CheckableCycleMateLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public CheckableCycleMateLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

}
