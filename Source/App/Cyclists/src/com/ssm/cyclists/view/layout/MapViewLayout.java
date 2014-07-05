package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.ssm.cyclists.controller.DataBaseManager;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.MapViewFragment;
import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.model.ResourceManager;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapViewLayout extends BaseFragmentLayout {

	static String TAG = MapViewLayout.class.getSimpleName();
	
	private Button btnMenu;
	private TextView tvFragmentName;
	private TextView tvTitleTopLeft;
	private TextView tvTitleBottomLeft;
	private TextView tvValueTopLeft;
	private TextView tvValueBottomLeft;
	private TextView tvTitleTopRight;
	private TextView tvTitleBottomRight;
	private TextView tvValueTopRight;
	private TextView tvValueBottomRight;
	private TextView tvAppName;
	private GoogleMap mGoogleMap;
//	MapView map_view;
	
	boolean init_map = false;
	
	public MapViewLayout(Fragment instance) {
		super(instance);
		Log.d(TAG,"MapViewLayout");
	}
	
	public void clean(){
		mGoogleMap.setMyLocationEnabled(false);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		Log.d(TAG,"onCreateView");
		view = inflater.inflate(R.layout.fragment_map, container, false);
	}
	
	public void init(){
		Log.d(TAG,"init");
		btnMenu = (Button)getView().findViewById(R.id.menu_button_map);
		btnMenu.setOnClickListener(buildMenuButtonListener());
		
		tvFragmentName = (TextView)getView().findViewById(R.id.fragment_name_map);
		tvTitleTopLeft = (TextView)getView().findViewById(R.id.tv_title_top_left_map);
		tvTitleBottomLeft = (TextView)getView().findViewById(R.id.tv_title_bottom_left_map);
		tvValueTopLeft = (TextView)getView().findViewById(R.id.tv_value_top_left_map);
		tvValueBottomLeft = (TextView)getView().findViewById(R.id.tv_value_bottom_left_map);
		tvTitleTopRight = (TextView)getView().findViewById(R.id.tv_title_top_right_map);
		tvTitleBottomRight = (TextView)getView().findViewById(R.id.tv_title_bottom_right_map);
		tvValueTopRight = (TextView)getView().findViewById(R.id.tv_value_top_right_map);
		tvValueBottomRight = (TextView)getView().findViewById(R.id.tv_value_bottom_right_map);
		tvAppName = (TextView)getView().findViewById(R.id.app_name_map);
		
		
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleTopLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleBottomLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueTopLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueBottomLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleTopRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleBottomRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueTopRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueBottomRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
			
		mGoogleMap = ((MapFragment)fragment.getFragmentManager().findFragmentById(R.id.map_map)).getMap();
		
		mGoogleMap.setMyLocationEnabled(true);
		
		Location lo = mGoogleMap.getMyLocation();
		if(lo==null) {
			lo = CruiseDataManager.getInstance().getCurrent_loc();
			if(lo==null)
			{
				 // Creating a criteria object to retrieve provider
	            Criteria criteria = new Criteria();
	 
	            // Getting the name of the best provider
	            String provider = ((LocationManager) getView().getContext().getSystemService(Context.LOCATION_SERVICE)).getBestProvider(criteria, true);
	            lo = new Location(provider);
				lo.setLatitude(37.523452);
				lo.setLongitude(127.028540);
			}
			
		}
		else{
			moveMapCamenra(lo);
			final Location loc = lo;
			CruiseDataManager.getInstance().setCurrent_loc(loc.getLatitude(),loc.getLongitude());
		}
		
		mGoogleMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			
			@Override
			public boolean onMyLocationButtonClick() {
				Location lo = mGoogleMap.getMyLocation();
				if(lo.hasSpeed())
				{
					Toast.makeText(getView().getContext(),String.valueOf(lo.getSpeed()), Toast.LENGTH_LONG).show();
				}
//				Toast.makeText(getView().getContext(),String.valueOf(lo.getSpeed()), Toast.LENGTH_LONG).show();
				CruiseDataManager.getInstance().setCurrent_loc(lo.getLatitude(),lo.getLongitude());
				DataBaseManager.getInstance().updateLastLocation(lo);
				return false;
			}
		});
	}

	
	public void moveMapCamenra(Location location){
			
		Log.d(TAG,"moveMapCamenra");

		
			// Getting latitude of the current location
		     double latitude = location.getLatitude();
		 
		     // Getting longitude of the current location
		     double longitude = location.getLongitude();
		 
		     // Creating a LatLng object for the current location
		     LatLng latLng = new LatLng(latitude, longitude);
		 
		     // Showing the current location in Google Map
		     mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		     
		     if(!init_map){
		    	 mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		    	 init_map = true;
		     }
		     else	mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(mGoogleMap.getCameraPosition().zoom));
		     // Zoom in the Google Map
		     
		     Log.d(TAG,String.valueOf(mGoogleMap.getCameraPosition().zoom));
	
	}
	
	
	private OnClickListener buildMenuButtonListener(){
	
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				((MainActivity)fragment.getActivity()).open_button(v);
			}
		};
	}
	
	public GoogleMap getMapInstance(){
		assert(mGoogleMap!=null);
		return mGoogleMap;
	}

	public void updateMapViewInfo(){
		moveMapCamenra(CruiseDataManager.getInstance().getCurrent_loc());
	}
}
