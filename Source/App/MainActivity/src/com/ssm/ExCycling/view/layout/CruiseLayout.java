package com.ssm.ExCycling.view.layout;

import net.simonvt.menudrawer.MenuDrawer;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

	static String TAG = CruiseLayout.class.getSimpleName();
	
	private Button btnMenu;
	
	private TextView tvAppName;
	private TextView tvFragmentName;
	
	private TextView tvDistance;
	private TextView tvDistanceData;
	private TextView tvDistanceDataUnit;
	
	private TextView tvAvgVelocityData;
	private TextView tvAvgVelocityDataUnit;
	
	private TextView tvKcal;
	private TextView tvKcalUnit;
	private TextView tvKcalData;
	
	private TextView tvAltitude;
	private TextView tvAltitudeUnit;
	private TextView tvAltitudeData;
	
	private TextView tvMaxSpeed;
	private TextView tvMaxSpeedUnit;
	private TextView tvMaxSpeedData;
	
	private LinearLayout lyInnerCircle;
	
	private LinearLayout lyTopBar;
	private LinearLayout lyMidBar;
	
	private LinearLayout lyInnerCycle;
	private LinearLayout lyOuterCycle;	
	
	private View	vLeftBar;
	private View	vRightBar;
	
	public CruiseLayout(Fragment instance) {
		super(instance);
	}
	
	public void init(){
		
		btnMenu = (Button)getView().findViewById(R.id.menu_button_cruise);
		btnMenu.setOnClickListener(buildMenuButtonListener());
		
		tvAppName 				= (TextView)getView().findViewById(R.id.app_name_cruise);
		tvFragmentName			= (TextView)getView().findViewById(R.id.fragment_name_cruise);
		
		tvDistance 				= (TextView)getView().findViewById(R.id.tv_distance_cruise);
		tvDistanceData 			= (TextView)getView().findViewById(R.id.tv_distance_data_cruise);
		tvDistanceDataUnit 		= (TextView)getView().findViewById(R.id.tv_distance_data_unit_cruise);
		
		tvAvgVelocityData 		= (TextView)getView().findViewById(R.id.tv_average_velocity_data_cruise); 
		tvAvgVelocityDataUnit 	= (TextView)getView().findViewById(R.id.tv_average_velocity_data_unit_cruise);
		
		tvKcal 			= (TextView)getView().findViewById(R.id.tv_kcal_cruise);
		tvKcalData 		= (TextView)getView().findViewById(R.id.tv_kcal_data_cruise);
		tvKcalUnit		= (TextView)getView().findViewById(R.id.tv_kcal_data_unit_cruise);
		
		tvAltitude 		= (TextView)getView().findViewById(R.id.tv_altitude_cruise);
		tvAltitudeData	= (TextView)getView().findViewById(R.id.tv_altitude_data_cruise);
		tvAltitudeUnit	= (TextView)getView().findViewById(R.id.tv_altitude_data_unit_cruise);
		
		tvMaxSpeed 		= (TextView)getView().findViewById(R.id.tv_max_speed_cruise);
		tvMaxSpeedData 	= (TextView)getView().findViewById(R.id.tv_max_speed_data_cruise);
		tvMaxSpeedUnit	= (TextView)getView().findViewById(R.id.tv_max_speed_data_unit_cruise);
		
		
		lyInnerCircle	= (LinearLayout)getView().findViewById(R.id.inner_circle_cruise);
		lyTopBar		= (LinearLayout)getView().findViewById(R.id.top_bar_cruise);
		lyMidBar		= (LinearLayout)getView().findViewById(R.id.mid_bar_cruise);
		
		vLeftBar	= (View)getView().findViewById(R.id.left_bar_cruise);
		vRightBar	= (View)getView().findViewById(R.id.right_bar_cruise);
		
		lyOuterCycle = (LinearLayout)getView().findViewById(R.id.outer_circle_cruise);
		lyInnerCycle = (LinearLayout)getView().findViewById(R.id.inner_circle_cruise);
		
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		
		tvDistance.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvDistanceData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvDistanceDataUnit.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		tvAvgVelocityData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvAvgVelocityDataUnit.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		tvKcal.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvKcalData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvKcalUnit.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		tvAltitude.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvAltitudeData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvAltitudeUnit.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		
		tvMaxSpeed.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvMaxSpeedData.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		tvMaxSpeedUnit.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		
		Drawable inner_circle = MainActivity.getInstasnce().getResources().getDrawable(R.drawable.inner_circle_pink);
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)lyInnerCircle.getLayoutParams();
		
//		lyInnerCircle.setBackground(rotateResource(R.drawable.inner_cycle_cruise,45,(int)(inner_circle.getMinimumWidth()*1.1),(int)(inner_circle.getMinimumHeight()*1.2)));
		
		updateCruiseInfo();
		updateColor();
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
				tvAltitudeData.setText(String.valueOf(CruiseDataManager.getInstance().getElevation()));
			}
		});
		
		tvAvgVelocityData.post(new Runnable() {
			
			@Override
			public void run() {
				tvAvgVelocityData.setText(String.valueOf(CruiseDataManager.getInstance().getCurrent_speed()));
			}
		});
		
		tvKcalData.post(new Runnable() {
			
			@Override
			public void run() {
				tvKcalData.setText(String.valueOf(CruiseDataManager.getInstance().getCalory()));
			}
		});
		
		tvDistanceData.post(new Runnable() {
			
			@Override
			public void run() {
				
				String dist = String.format("%.2f", CruiseDataManager.getInstance().getDistnace());
				
				tvDistanceData.setText(dist);
			}
		});
		
		tvMaxSpeedData.post(new Runnable() {
			
			@Override
			public void run() {
				tvMaxSpeedData.setText(String.valueOf(CruiseDataManager.getInstance().getMaximum_speed()));
			}
		});

		tvKcalData.post(new Runnable() {
			
			@Override
			public void run() {
				tvKcalData.setText(String.valueOf(CruiseDataManager.getInstance().getCalory()));
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
	
	public void updateColor(){
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_mid));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
						
			tvDistance.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDistanceData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvDistanceDataUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			tvAvgVelocityData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvAvgVelocityDataUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			tvKcal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvKcalUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvKcalData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		
			tvAltitude.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvAltitudeUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvAltitudeData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			tvMaxSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvMaxSpeedUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvMaxSpeedData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			vLeftBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			vRightBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			
			lyInnerCycle.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.inner_circle_pink));
			lyOuterCycle.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.outer_circle_pink));
			
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_mid));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
						
			tvDistance.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDistanceData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvDistanceDataUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			tvAvgVelocityData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvAvgVelocityDataUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			tvKcal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvKcalUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvKcalData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
		
			tvAltitude.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvAltitudeUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvAltitudeData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			tvMaxSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvMaxSpeedUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvMaxSpeedData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			vLeftBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			vRightBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			
			lyInnerCycle.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.inner_circle_green));
			lyOuterCycle.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.outer_circle_green));
			
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_mid));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
						
			tvDistance.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDistanceData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvDistanceDataUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			tvAvgVelocityData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvAvgVelocityDataUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			tvKcal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvKcalUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvKcalData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
		
			tvAltitude.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvAltitudeUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvAltitudeData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			tvMaxSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvMaxSpeedUnit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvMaxSpeedData.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));

			vLeftBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			vRightBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			
			lyInnerCycle.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.inner_circle_gray));
			lyOuterCycle.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.outer_circle_gray));
		}
	}

}
