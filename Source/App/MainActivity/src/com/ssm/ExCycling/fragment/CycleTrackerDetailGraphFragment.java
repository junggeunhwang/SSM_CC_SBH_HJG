package com.ssm.ExCycling.fragment;

import com.ssm.ExCycling.view.layout.CycleTrackerDetailGraphLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleTrackerDetailGraphFragment extends Fragment {

	static String TAG = CycleTrackerDetailGraphFragment.class.getSimpleName();
	
	private CycleTrackerDetailGraphLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		MainActivity.getInstasnce().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		layout = new CycleTrackerDetailGraphLayout(this);
		layout.createView(inflater, container);
		layout.init();
		Log.d(TAG, "onCreateView");
		
		
		return layout.getView();
	}

	public CycleTrackerDetailGraphLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

	@Override
	public void onStart() {
		layout.updateColor();
		super.onStart();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}
}
