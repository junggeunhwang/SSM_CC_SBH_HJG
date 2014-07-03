package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

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
			}
		});
	}

}
