package com.ssm.ExCycling.fragment;

import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.view.layout.CycleMateLayout;
import com.ssm.ExCycling.view.layout.HomeLayout;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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

	@Override
	public void onPause() {
		layout.onPause();
		super.onPause();
	}
}
