package com.ssm.cyclists.view;

import com.ssm.cyclists.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.ssm.cyclists.controller.MainActivity;
import com.ssm.cyclists.model.ResourceManager;

import android.app.Dialog;
import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MapViewLayout extends BaseFragmentLayout {

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
	
	public MapViewLayout(Fragment instance) {
		super(instance);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_map, container, false);
	}
	
	public void init(){
		
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
//		map_view = (MapView)((MapFragment)fragment.getFragmentManager().findFragmentById(R.id.map_map)).getView();
		
		mGoogleMap.setMyLocationEnabled(true);
		Location lo = mGoogleMap.getMyLocation();
		
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
		return mGoogleMap;
	}
//	public MapView getMapView(){
//		return map_view;
//	}
}