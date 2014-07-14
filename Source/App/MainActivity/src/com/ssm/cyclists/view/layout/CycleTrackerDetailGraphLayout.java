package com.ssm.cyclists.view.layout;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.manager.ResourceManager;
import com.ssm.cyclists.controller.manager.SettingsDataManager;
import com.ssm.cyclists.model.CycleData;

public class CycleTrackerDetailGraphLayout extends BaseFragmentLayout {

	static String TAG = CycleTrackerDetailGraphLayout.class.getSimpleName();
	
	private Button btnBack;
	
	private RadioButton radioSpeed;
	private RadioButton radioAltitude;
	private LinearLayout lyTopBar;
	private GraphView graphView;
	
	private TextView tvFragmentName;
	private TextView tvAppName;
	private TextView tvDistanceUnit;
	private TextView tvVelocityUnit;
	private GraphViewData[] SpeedData;
	private GraphViewData[] AltitudeData;
	
	private LinearLayout graphLayout;
	private double SpeedMax = 0, AltitudeMax=0, DistMax=0;
	
	public CycleTrackerDetailGraphLayout(Fragment instance) {
		super(instance);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cycle_tracker_detail_graph, container, false);		
		Log.i(TAG, "creatView ȣ��");
	}
	public void init(){
		
		Log.i(TAG, "test init() ȣ��");
		btnBack = (Button)getView().findViewById(R.id.back_button_cycletracker_detail);
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.getInstasnce().getLayout().getmMenuDrawer().setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
				//�ڷΰ���
				backScreen();
			}
		});
		
		lyTopBar = (LinearLayout)getView().findViewById(R.id.top_bar_cycle_tracker_detail);
		radioSpeed = (RadioButton)getView().findViewById(R.id.radio_speed_cycletrackerdetail);
		radioAltitude = (RadioButton)getView().findViewById(R.id.radio_altitude_cycletrackerdetail);
		
		radioSpeed.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		radioAltitude.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		tvFragmentName = (TextView)getView().findViewById(R.id.fragment_name_cycletracker_detail);
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
		tvAppName = (TextView)getView().findViewById(R.id.app_name_cycletrackerdetail);
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		tvVelocityUnit = (TextView)getView().findViewById(R.id.velocity_unit_cycletracker_detail);
		tvVelocityUnit.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvDistanceUnit = (TextView)getView().findViewById(R.id.distance_unit_cycletracker_detail);
		tvDistanceUnit.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
	
		//�׷����׸���
		CycleData cycleData = MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker().getCurrentActivatedData();
		ArrayList<String> speedList = cycleData.getSpeedList();
		ArrayList<String> altitudeList = cycleData.getAltitudeList();
		ArrayList<Double> distList = cycleData.getDistanceList();
		
		SpeedData = new GraphViewData[speedList.size()];
		AltitudeData = new GraphViewData[altitudeList.size()];
		
		
		
		for(int i = 0 ; i < speedList.size();i++){
			if(SpeedMax<Double.valueOf(speedList.get(i))) SpeedMax =Double.valueOf(speedList.get(i)); 
			GraphViewData gvSpeedData = new GraphViewData(distList.get(i),Double.valueOf(speedList.get(i)));
			SpeedData[i] = gvSpeedData;
			
			if(AltitudeMax<Double.valueOf(altitudeList.get(i))) AltitudeMax =Double.valueOf(altitudeList.get(i));
			GraphViewData gvAltitudeData = new GraphViewData(distList.get(i),Double.valueOf(altitudeList.get(i)));
			AltitudeData[i] = gvAltitudeData;
			
			if(DistMax<distList.get(i)) AltitudeMax =distList.get(i);//�Ÿ����ĸ� ���߳�.. ���������ؾ��ϴµ�..
			
		}
		
		generateGraph();
		graphView.setManualYAxisBounds(SpeedMax+5, 0);
		graphView.setViewPort(0,DistMax);
		
		radioSpeed.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					graphView.removeAllSeries();
					graphView.setManualYAxisBounds(SpeedMax+5, 0);
					graphView.setViewPort(0,DistMax);
					if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
						graphView.addSeries(new GraphViewSeries("speed_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy),3),SpeedData));
						((LineGraphView)graphView).setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_opacity));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
						graphView.addSeries(new GraphViewSeries("speed_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy),3),SpeedData));
						((LineGraphView)graphView).setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_opacity));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
						graphView.addSeries(new GraphViewSeries("speed_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy),3),SpeedData));
					}
					tvVelocityUnit.setText("(km/h)");
				}
			}
		});

		radioAltitude.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					graphView.removeAllSeries();
					graphView.setManualYAxisBounds(AltitudeMax+5, 0);
					graphView.setViewPort(0,DistMax);
					if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
						graphView.addSeries(new GraphViewSeries("altitude_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy),3),AltitudeData));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
						graphView.addSeries(new GraphViewSeries("altitude_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy),3),AltitudeData));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
						graphView.addSeries(new GraphViewSeries("altitude_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy),3),AltitudeData));
					}
					
					tvVelocityUnit.setText("(m)");
				}
			}
		});
		updateColor();
		
	}
	
	public Bundle onSaveInstanceState(Bundle outState) {
		
		return null;
	}
	
	public void generateGraph(){
		graphView = new LineGraphView(
			    getView().getContext()
			    , ""
			);
			
			
			((LineGraphView)graphView).setDrawBackground(true);
			((LineGraphView)graphView).setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_opacity));
			
			graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
				
				@Override
				public String formatLabel(double value, boolean isValueX) {
					if(isValueX){
						if(value%2==0){
							return String.valueOf(value);
						}
					}
					return null;
				}
			});
			
			graphView.getGraphViewStyle().setTextSize(20);
			graphView.setManualMaxY(true);
			graphView.setManualMinY(true);
			graphView.setManualYAxisBounds(30, 0);
			
			// add data
			graphView.addSeries(new GraphViewSeries("test_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy),3),SpeedData));
			// set view port, start=0, size=5
			graphView.setViewPort(0, 5);
			graphView.setScrollable(true);
			// optional - activate scaling / zooming
			graphView.setScalable(true);
			 
			graphLayout = (LinearLayout) getView().findViewById(R.id.graph_cycle_tracker_detail);
			graphLayout.addView(graphView);
	}

	public void backScreen(){
		MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cycle_tracker);
		MainActivity.getInstasnce().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public void updateColor(){
		
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			radioSpeed.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			radioAltitude.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioAltitude.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			graphView.removeAllSeries();
			graphView.addSeries(new GraphViewSeries("test_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy),3),SpeedData));
			((LineGraphView)graphView).setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_opacity));
			
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			radioSpeed.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			radioSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			radioAltitude.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			radioAltitude.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			graphView.removeAllSeries();
			graphView.addSeries(new GraphViewSeries("test_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy),3),SpeedData));
			((LineGraphView)graphView).setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_opacity));
			
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			radioSpeed.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			radioSpeed.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			radioAltitude.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			radioAltitude.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));

			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			graphView.removeAllSeries();
			graphView.addSeries(new GraphViewSeries("test_data",new GraphViewSeriesStyle(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy),3),SpeedData));
			((LineGraphView)graphView).setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_opacity));
		}
	}

	public GraphViewData[] getSpeedData() {
		return SpeedData;
	}

	public GraphViewData[] getAltitudeData() {
		return AltitudeData;
	}

	public void setSpeedData(GraphViewData[] speedData) {
		SpeedData = speedData;
	}

	public void setAltitudeData(GraphViewData[] altitudeData) {
		AltitudeData = altitudeData;
	}
}