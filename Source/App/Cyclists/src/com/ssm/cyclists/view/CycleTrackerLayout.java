package com.ssm.cyclists.view;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.CycleTrackerFragment;
import com.ssm.cyclists.controller.MainActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;



public class CycleTrackerLayout extends BaseFragmentLayout {
	
	private Button btnMenu;
	
	public CycleTrackerLayout(CycleTrackerFragment instance) {
		super(instance);
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container){
		view = inflater.inflate(R.layout.fragment_cycle_tracker, container, false);
	}
	
	public void init(){
		btnMenu 		= (Button)getView().findViewById(R.id.menu_button_cycletracker);
		btnMenu.setOnClickListener(buildMenuButtonListener());
	}
	
	private OnClickListener buildMenuButtonListener(){
		
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				((MainActivity)fragment.getActivity()).open_button(v);
			}
		};
	}
	
}
