package com.ssm.cyclists.controller;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.LatLng;
import com.ssm.cyclists.R;
import com.ssm.cyclists.view.MapLayout;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MapViewFragment extends Fragment {

	MapLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = new MapLayout(this);
		layout.createView(inflater, container);
		layout.init();
		init_map_service();
		
		return layout.getView();
	}
	
	private void init_map_service(){
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
 
        }else { // Google Play Services are available
 
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
 
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location!=null){
//                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, buildLocationChangediListener());
        }
	}
	
	private LocationListener buildLocationChangediListener(){
		return new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				 			 
			     // Getting latitude of the current location
			     double latitude = location.getLatitude();
			 
			     // Getting longitude of the current location
			     double longitude = location.getLongitude();
			 
			     // Creating a LatLng object for the current location
			     LatLng latLng = new LatLng(latitude, longitude);
			 
			     // Showing the current location in Google Map
			     layout.getMapInstance().moveCamera(CameraUpdateFactory.newLatLng(latLng));
			 
			     // Zoom in the Google Map
			     layout.getMapInstance().animateCamera(CameraUpdateFactory.zoomTo(15));
			 
			     // Setting latitude and longitude in the TextView tv_location
			     Toast.makeText(getActivity(), "Latitude:" +  latitude  + ", Longitude:"+ longitude ,Toast.LENGTH_LONG).show();	
			}
		};
	}
}
