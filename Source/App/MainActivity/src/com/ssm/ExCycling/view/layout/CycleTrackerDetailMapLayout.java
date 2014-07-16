package com.ssm.ExCycling.view.layout;

import java.util.ArrayList;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CycleTrackerDetailMapLayout extends BaseFragmentLayout {

	static String TAG = CycleTrackerDetailMapLayout.class.getSimpleName();
	
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
	
	private LinearLayout lyTopBar;
	private LinearLayout lyMidBar;
	
	private ImageView ivLocationIcon;
//	MapView map_view;
	
	boolean init_map = false;
	
	public CycleTrackerDetailMapLayout(Fragment instance) {
		super(instance);
		Log.d(TAG,"MapViewLayout");
	}
	
	public void clean(){
		mGoogleMap.setMyLocationEnabled(false);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		Log.d(TAG,"onCreateView");
		if(view==null)
			view = inflater.inflate(R.layout.fragment_cycle_tracker_detail_map, container, false);
	}
	
	public void init(){
		Log.d(TAG,"init");
		
		tvFragmentName = (TextView)getView().findViewById(R.id.fragment_name_cycle_tracker_detail_map);
		tvTitleTopLeft = (TextView)getView().findViewById(R.id.tv_title_top_left_cycle_tracker_detail_map);
		tvTitleBottomLeft = (TextView)getView().findViewById(R.id.tv_title_bottom_left_cycle_tracker_detail_map);
		tvValueTopLeft = (TextView)getView().findViewById(R.id.tv_value_top_left_cycle_tracker_detail_map);
		tvValueBottomLeft = (TextView)getView().findViewById(R.id.tv_value_bottom_left_cycle_tracker_detail_map);
		tvTitleTopRight = (TextView)getView().findViewById(R.id.tv_title_top_right_cycle_tracker_detail_map);
		tvTitleBottomRight = (TextView)getView().findViewById(R.id.tv_title_bottom_right_cycle_tracker_detail_map);
		tvValueTopRight = (TextView)getView().findViewById(R.id.tv_value_top_right_cycle_tracker_detail_map);
		tvValueBottomRight = (TextView)getView().findViewById(R.id.tv_value_bottom_right_cycle_tracker_detail_map);
		tvAppName = (TextView)getView().findViewById(R.id.app_name_cycle_tracker_detail_map);
		
		lyTopBar = (LinearLayout)getView().findViewById(R.id.top_bar_cycle_tracker_detail_map);
		lyMidBar = (LinearLayout)getView().findViewById(R.id.mid_box_cycle_tracker_detail_map);
		
		ivLocationIcon = (ImageView)getView().findViewById(R.id.location_icon_cycle_tracker_detail_map);
		
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
			
		MapFragment mf =(MapFragment)MainActivity.getInstasnce().getFragmentManager().findFragmentById(R.id.map_cycle_tracker_detail_map); 
		
 		mGoogleMap = mf.getMap();
		mGoogleMap.setMyLocationEnabled(true);
		
		ArrayList<LatLng> input = new ArrayList<LatLng>();
		input.add(new LatLng(37.560431,127.037295));
		input.add(new LatLng(37.558168,127.038883));
		input.add(new LatLng(37.553915,127.038947));
		input.add(new LatLng(37.551414,127.035471));
		
		addPolyline(input);
		
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
		
		
		updateMapViewInfo();
		updateColor();
	}

	public void addPolyline(ArrayList<LatLng> src){
		
		PolylineOptions options = new PolylineOptions();
		
		for(int i = 0 ; i < src.size();i++){
			options.add(src.get(i));
		}
		options.width(10).color(Color.BLUE);
		
		Polyline result = mGoogleMap.addPolyline(options);
		result.setVisible(true);
	}
	
	
	public void moveMapCamenra(Location location){
			
		Log.d(TAG,"moveMapCamenra");

		
		// Getting latitude of the current location
		double latitude = location.getLatitude();
		 
		// Getting longitude of the current location
		double longitude = location.getLongitude();
		 
		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);
		 	     
		if(!init_map){
			// Showing the current location in Google Map
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		    init_map = true;
		}

		// Zoom in the Google Map
	}
	
	public GoogleMap getMapInstance(){
		assert(mGoogleMap!=null);
		return mGoogleMap;
	}

	public void updateMapViewInfo(){
		moveMapCamenra(CruiseDataManager.getInstance().getCurrent_loc());
	}
	
	public void updateColor(){
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_mid));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			tvTitleTopLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleBottomLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvValueTopLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvValueBottomLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleTopRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleBottomRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvValueTopRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvValueBottomRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			ivLocationIcon.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.location_icon_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_mid));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

			tvTitleTopLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleBottomLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvValueTopLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvValueBottomLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleTopRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleBottomRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvValueTopRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvValueBottomRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			ivLocationIcon.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.location_icon_green));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_mid));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			tvTitleTopLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleBottomLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvValueTopLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvValueBottomLeft.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleTopRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleBottomRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvValueTopRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvValueBottomRight.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			ivLocationIcon.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.location_icon_gray));
		}
	}
	
	
}
