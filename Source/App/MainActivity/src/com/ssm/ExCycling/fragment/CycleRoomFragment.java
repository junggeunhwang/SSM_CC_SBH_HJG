package com.ssm.ExCycling.fragment;

import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.view.layout.CycleRoomLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleRoomFragment extends Fragment {

	private CycleRoomLayout layout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = new CycleRoomLayout(this);
		layout.createView(inflater, container);
		layout.init();

		if(SettingsDataManager.getInstance().isStart_stopBicycleFlag()==false){// start to bycycle
			//true update.
			CruiseDataManager.getInstance().updateCruiseData();	
		}else{	//stop to bycycle
			//false no update and clear;
			CruiseDataManager.getInstance().clear();
			layout.updateRoomInfo();
		}
		
		return layout.getView();
	}
	
	public CycleRoomLayout getLayout() {
		assert(layout!=null);
		return layout;
	}
}
