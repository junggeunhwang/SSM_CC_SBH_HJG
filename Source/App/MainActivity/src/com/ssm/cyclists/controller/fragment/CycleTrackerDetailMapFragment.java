package com.ssm.cyclists.controller.fragment;



import com.ssm.cyclists.view.layout.CycleTrackerDetailMapLayout;
import com.ssm.cyclists.view.layout.MapViewLayout;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CycleTrackerDetailMapFragment extends Fragment {

	static String TAG = CycleTrackerDetailMapFragment.class.getSimpleName();
	
	private CycleTrackerDetailMapLayout layout = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"onCreateView");
		if(layout==null){
			layout = new CycleTrackerDetailMapLayout(this);
			layout.createView(inflater, container);
		}
		layout.init();	
		return layout.getView();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
	}
	public void moveMapCamenra(Location location){
		layout.moveMapCamenra(location);
	}

	public CycleTrackerDetailMapLayout getLayout() {
		assert(layout!=null);
		return layout;
	}
	
	
	@Override
	public void onDestroyView() {
		Log.d(TAG,"onDestroyView");
		layout.clean();
		super.onDestroyView();
	}
	
	public void updateMapViewInfo(){
		layout.updateMapViewInfo();
	}
}