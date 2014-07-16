package com.ssm.ExCycling.fragment;

import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.view.layout.CruiseLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CruiseFragment extends Fragment {
	
	static String TAG = CruiseFragment.class.getSimpleName();
	
	private CruiseLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"onCreateView");
		layout = new CruiseLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		if(SettingsDataManager.getInstance().isStart_stopBicycleFlag()==false){// start to bycycle
			//true update.
			layout.updateCruiseInfo();
		}else{	//stop to bycycle
			//false no update and clear;
			CruiseDataManager.getInstance().clear();
			layout.updateCruiseInfo();
		}
		
		return layout.getView();
	}

	public void updateCruiseInfo(){
		layout.updateCruiseInfo();
	}

	public CruiseLayout getLayout() {
		assert(layout!=null);
		return layout;
	}
	
}
