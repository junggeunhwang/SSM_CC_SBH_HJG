package com.ssm.cyclists.view.layout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.MainActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class CycleTrackerDetailLayout extends BaseFragmentLayout {

	Button btnBack;
	
	public CycleTrackerDetailLayout(Fragment instance) {
		super(instance);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cycle_tracker_detail, container, false);		
	}
	public void init(){
		btnBack = (Button)getView().findViewById(R.id.back_button_cycletracker_detail);
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ڷΰ���
				FragmentTransaction transaction = MainActivity.getInstasnce().getFragmentManager().beginTransaction();
				transaction.hide(fragment);
				transaction.show(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker());
				MainActivity.getInstasnce().getLayout().setActivated_fragment(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker());
				transaction.commit();
				
				MainActivity.getInstasnce().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				
			}
		});
		
		// draw sin curve
		int num = 150;
		GraphViewData[] data = new GraphViewData[num];
		double v=0;
		for (int i=0; i<num; i++) {
		  v += 0.2;
		  data[i] = new GraphViewData(i, Math.sin(v));
		}
		
		GraphViewData[] data2 = new GraphViewData[10];
		data2[0] = new GraphViewData(0,0);
		data2[1] = new GraphViewData(1,13);
		data2[2] = new GraphViewData(2,15);
		data2[3] = new GraphViewData(3,20);
		data2[4] = new GraphViewData(4,23);
		data2[5] = new GraphViewData(5,17);
		data2[6] = new GraphViewData(6,17);
		data2[7] = new GraphViewData(7,20);
		data2[8] = new GraphViewData(8,15);
		data2[9] = new GraphViewData(9,5);
		
		
		
		GraphView graphView = new LineGraphView(
		    getView().getContext()
		    , ""
		);
		
		
		((LineGraphView)graphView).setDrawBackground(true);
		((LineGraphView)graphView).setBackgroundColor(Color.argb(66,252,156,151));
		
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
		graphView.addSeries(new GraphViewSeries("test_data",new GraphViewSeriesStyle(Color.rgb(252, 156, 151),3),data2));
		// set view port, start=0, size=5
		graphView.setViewPort(0, 5);
		graphView.setScrollable(true);
		// optional - activate scaling / zooming
		graphView.setScalable(true);
		 
		LinearLayout layout = (LinearLayout) getView().findViewById(R.id.graph_cycle_tracker_detail);
		layout.addView(graphView);
		
		
	}

}
