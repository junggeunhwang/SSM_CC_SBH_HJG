package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.MainActivity;
import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.model.ResourceManager;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CruiseLayout extends BaseFragmentLayout {

	private Button btnMenu;
	private TextView tvAppName;
	private TextView tvFragmentName;
	
	private TextView tvDistance;
	private TextView tvDistanceData;
	
	private TextView tvAvgVelocityData;
	
	private TextView tvKcal;
	private TextView tvAltitude;
	private TextView tvMaxSpeed;
	
	private TextView tvKcalData;
	private TextView tvAltitudeData;
	private TextView tvMaxSpeedData;
	LinearLayout lyInnerCircle;
	
	public CruiseLayout(Fragment instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}
	
	public void init(){
		
		tvAppName 			= (TextView)getView().findViewById(R.id.app_name_cruise);
		tvFragmentName		= (TextView)getView().findViewById(R.id.fragment_name_cruise);
		tvDistance 			= (TextView)getView().findViewById(R.id.tv_distance_cruise);
		tvDistanceData 		= (TextView)getView().findViewById(R.id.tv_distance_data_cruise);
		tvAvgVelocityData 	= (TextView)getView().findViewById(R.id.tv_average_velocity_data_cruise); 
		tvKcal 				= (TextView)getView().findViewById(R.id.tv_kcal_cruise);
		tvAltitude 			= (TextView)getView().findViewById(R.id.tv_altitude_cruise);
		tvMaxSpeed 			= (TextView)getView().findViewById(R.id.tv_max_speed_cruise);
		tvKcalData 			= (TextView)getView().findViewById(R.id.tv_kcal_data_cruise);
		tvAltitudeData	 	= (TextView)getView().findViewById(R.id.tv_altitude_data_cruise);
		tvMaxSpeedData 		= (TextView)getView().findViewById(R.id.tv_max_speed_data_cruise);
		lyInnerCircle		= (LinearLayout)getView().findViewById(R.id.inner_circle_cruise);
		
		btnMenu 		= (Button)getView().findViewById(R.id.menu_button_cruise);
		btnMenu.setOnClickListener(buildMenuButtonListener());
		
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvDistance.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvDistanceData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvAvgVelocityData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic")); 
		tvKcal.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvAltitude.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvMaxSpeed.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvKcalData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvAltitudeData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvMaxSpeedData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
		Drawable inner_circle = MainActivity.getInstasnce().getResources().getDrawable(R.drawable.inner_cycle_cruise);
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)lyInnerCircle.getLayoutParams();
		
//		lyInnerCircle.setBackground(rotateResource(R.drawable.inner_cycle_cruise,45,(int)(inner_circle.getMinimumWidth()*1.1),(int)(inner_circle.getMinimumHeight()*1.2)));
		
		
	}
	

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cruise, container, false);
	}

	private OnClickListener buildMenuButtonListener(){
		
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				((MainActivity)fragment.getActivity()).open_button(v);
			}
		};
	}
	
	public void updateCruiseInfo(){
		tvAltitudeData.post(new Runnable() {
			
			@Override
			public void run() {
				tvAltitudeData.setText(String.valueOf(CruiseDataManager.getInstance().getElevation())+ "m");
			}
		});
		
		tvAvgVelocityData.post(new Runnable() {
			
			@Override
			public void run() {
				tvAvgVelocityData.setText(String.valueOf(CruiseDataManager.getInstance().getCurrent_speed()) + "Km/h");
			}
		});
	}
	
	public Drawable rotateResource(int resID,float degree,int width,int height){
		Bitmap bitmapOrg = BitmapFactory.decodeResource(MainActivity.getInstasnce().getResources(), resID);
		
		Matrix matrix = new Matrix();
		matrix.postRotate(45);
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapOrg,0,0,width,height,matrix,true);
		return new BitmapDrawable(MainActivity.getInstasnce().getResources(),rotatedBitmap);
	}
}
