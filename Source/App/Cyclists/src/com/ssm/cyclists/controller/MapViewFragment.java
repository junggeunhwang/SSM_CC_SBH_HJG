package com.ssm.cyclists.controller;


import java.io.IOException;
import java.security.spec.MGF1ParameterSpec;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.ssm.cyclists.R;
import com.ssm.cyclists.view.layout.MapViewLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MapViewFragment extends Fragment {

	static String TAG = MapViewFragment.class.getSimpleName();
	
	private MapViewLayout layout;
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG,"onStart");
		super.onAttach(activity);
	}
	
	@Override
	public void onStart() {
		Log.d(TAG,"onStart");
		super.onStart();
	}
	
	@Override
	public void onResume() {
		Log.d(TAG,"onStart");
		super.onResume();
	}
	
	@Override
	public void onPause() {
		Log.d(TAG,"onPause");
		super.onPause();
	}
	@Override
	public void onStop() {
		Log.d(TAG,"onStop");
		super.onStop();
	}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG,"onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"onCreateView");
		layout = new MapViewLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}
	
	public void moveMapCamenra(Location location){
		layout.moveMapCamenra(location);
	}

	public MapViewLayout getLayout() {
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
