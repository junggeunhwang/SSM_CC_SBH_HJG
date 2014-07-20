package com.ssm.ExCycling.view.layout;

import java.util.ArrayList;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
<<<<<<< HEAD
import com.ssm.ExCycling.controller.manager.DataBaseManager;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
=======
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;

>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
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
<<<<<<< HEAD
	private TextView tvAppName;
	
	private TextView tvTitleTotalDistance;
	private TextView tvTitleTotalCalories;
	private TextView tvTitleAverageSpeed;
	private TextView tvTitleNumberofPeople;
	private TextView tvTitleAtmosphere;
	private TextView tvTitleWind;
	private TextView tvDataTotalDistance;
	private TextView tvDataTotalCalories;
	private TextView tvDataAverageSpeed;
	private TextView tvDataNumberofPeople;
	private TextView tvDataAtmosphere;
	private TextView tvDataWind;
	
	private GoogleMap mGoogleMap;
	
	private LinearLayout lyTopBar;
=======
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
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	
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
<<<<<<< HEAD
		DataBaseManager.getInstance().selectAtmosphere(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getDate());
		
		tvFragmentName = (TextView)getView().findViewById(R.id.fragment_name_cycle_tracker_detail_map);
		tvAppName = (TextView)getView().findViewById(R.id.app_name_cycle_tracker_detail_map);
		
		tvTitleTotalDistance = (TextView)getView().findViewById(R.id.tv_title_total_distance_cycletracker_detail);
		tvTitleTotalCalories = (TextView)getView().findViewById(R.id.tv_title_total_calories_cycletracker_detail);
		tvTitleAverageSpeed = (TextView)getView().findViewById(R.id.tv_title_avgspeed_cycletracker_detail);
		tvTitleNumberofPeople = (TextView)getView().findViewById(R.id.tv_title_number_of_people_cycletracker_detail);
		tvTitleAtmosphere = (TextView)getView().findViewById(R.id.tv_title_atmosphere_cycletracker_detail);
		tvTitleWind = (TextView)getView().findViewById(R.id.tv_title_wind_cycletracker_detail);
		tvDataTotalDistance = (TextView)getView().findViewById(R.id.tv_data_total_distance_cycletracker_detail);
		tvDataTotalCalories = (TextView)getView().findViewById(R.id.tv_data_total_calories_cycletracker_detail);
		tvDataAverageSpeed = (TextView)getView().findViewById(R.id.tv_data_avgspeed_cycletracker_detail);
		tvDataNumberofPeople = (TextView)getView().findViewById(R.id.tv_data_number_of_people_cycletracker_detail);
		tvDataAtmosphere = (TextView)getView().findViewById(R.id.tv_data_atmosphere_cycletracker_detail);
		tvDataWind = (TextView)getView().findViewById(R.id.tv_data_wind_cycletracker_detail);
		
		tvTitleTotalDistance.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleTotalCalories.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleAverageSpeed.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleNumberofPeople.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleAtmosphere.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleWind.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvDataTotalDistance.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvDataTotalCalories.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvDataAverageSpeed.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvDataNumberofPeople.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvDataAtmosphere.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvDataWind.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
		
		lyTopBar = (LinearLayout)getView().findViewById(R.id.top_bar_cycle_tracker_detail_map);
=======
		
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
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		
		ivLocationIcon = (ImageView)getView().findViewById(R.id.location_icon_cycle_tracker_detail_map);
		
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
<<<<<<< HEAD
=======
		tvTitleTopLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleBottomLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueTopLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueBottomLeft.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleTopRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTitleBottomRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueTopRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvValueBottomRight.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
			
		MapFragment mf =(MapFragment)MainActivity.getInstasnce().getFragmentManager().findFragmentById(R.id.map_cycle_tracker_detail_map); 
		
 		mGoogleMap = mf.getMap();
		mGoogleMap.setMyLocationEnabled(true);
		
<<<<<<< HEAD
//		ArrayList<LatLng> input = new ArrayList<LatLng>();
//		input.add(new LatLng(37.560431,127.037295));
//		input.add(new LatLng(37.558168,127.038883));
//		input.add(new LatLng(37.553915,127.038947));
//		input.add(new LatLng(37.551414,127.035471));
		ArrayList<Location> locationList = MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getLocationList();
		ArrayList<LatLng> locationList_latlng = new ArrayList<LatLng>();
		for(int i = 0 ; i < locationList.size();i++){
			LatLng newLoc = new LatLng(locationList.get(i).getLatitude(),locationList.get(i).getLongitude());
			locationList_latlng.add(newLoc);
		}
		
		addPolyline(locationList_latlng);
=======
		ArrayList<LatLng> input = new ArrayList<LatLng>();
		input.add(new LatLng(37.560431,127.037295));
		input.add(new LatLng(37.558168,127.038883));
		input.add(new LatLng(37.553915,127.038947));
		input.add(new LatLng(37.551414,127.035471));
		
		addPolyline(input);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		
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
<<<<<<< HEAD
				lo.setLatitude(37.2391647);
				lo.setLongitude(131.8682344);
=======
				lo.setLatitude(37.523452);
				lo.setLongitude(127.028540);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
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
	
<<<<<<< HEAD
	public void updateDetailMapLayout(){
		tvDataTotalDistance.post(new Runnable() {
			
			@Override
			public void run() {
				tvDataTotalDistance.setText(String.valueOf(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getDistance()) + " Km");
			}
		});
		tvDataTotalCalories.post(new Runnable() {
			
			@Override
			public void run() {
				tvDataTotalCalories.setText(String.valueOf(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getTotalConsumeCalrories()) + "Kcal");
			}
		});
		tvDataAverageSpeed.post(new Runnable() {
			
			@Override
			public void run() {
				double sum = 0;
				for(int i = 0 ; i < MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getSpeedList().size() ; i++){
					sum += Double.valueOf(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getSpeedList().get(i));
				}
				
				tvDataAverageSpeed.setText(String.valueOf( 
						String.format("%.2f", sum/MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getSpeedList().size())) +" Km/h");
				
			}
		});
		tvDataNumberofPeople.post(new Runnable() {
			
			@Override
			public void run() {
				if(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getMem_count() == null)tvDataNumberofPeople.setText("1 Person");
				else tvDataNumberofPeople.setText(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getMem_count() + " Person");
			}
		});
		tvDataAtmosphere.post(new Runnable() {
			
			@Override
			public void run() {
				tvDataAtmosphere.setText(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getTemp()
						+ " / " + MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getHumidity() + " %");
				
				
			}
		});
		tvDataWind.post(new Runnable() {
			
			@Override
			public void run() {
				tvDataWind.setText(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getWind_dir() + " "+ 
						MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData().getWind() + " m/s");
				
			}
		});
	}
	
	
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	
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
<<<<<<< HEAD
		updateDetailMapLayout();
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	}
	
	public void updateColor(){
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
<<<<<<< HEAD
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvTitleTotalDistance.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleTotalCalories.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleAverageSpeed.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleNumberofPeople.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleAtmosphere.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			tvTitleWind.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			
			tvDataTotalDistance.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
			tvDataTotalCalories.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
			tvDataAverageSpeed.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
			tvDataNumberofPeople.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
			tvDataAtmosphere.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
			tvDataWind.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
			
			tvDataTotalDistance.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDataTotalCalories.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDataAverageSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDataNumberofPeople.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDataAtmosphere.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDataWind.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			
			ivLocationIcon.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.location_icon_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvTitleTotalDistance.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleTotalCalories.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleAverageSpeed.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleNumberofPeople.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleAtmosphere.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			tvTitleWind.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			
			tvDataTotalDistance.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
			tvDataTotalCalories.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
			tvDataAverageSpeed.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
			tvDataNumberofPeople.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
			tvDataAtmosphere.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
			tvDataWind.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
			
			tvDataTotalDistance.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDataTotalCalories.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDataAverageSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDataNumberofPeople.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDataAtmosphere.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDataWind.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			ivLocationIcon.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.location_icon_green));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvTitleTotalDistance.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleTotalCalories.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleAverageSpeed.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleNumberofPeople.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleAtmosphere.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			tvTitleWind.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			
			tvDataTotalDistance.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
			tvDataTotalCalories.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
			tvDataAverageSpeed.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
			tvDataNumberofPeople.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
			tvDataAtmosphere.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
			tvDataWind.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
			
			tvDataTotalDistance.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDataTotalCalories.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDataAverageSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDataNumberofPeople.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDataAtmosphere.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDataWind.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
=======
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
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
			ivLocationIcon.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.location_icon_gray));
		}
	}
	
	
}
