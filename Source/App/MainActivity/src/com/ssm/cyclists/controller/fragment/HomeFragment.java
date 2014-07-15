package com.ssm.cyclists.controller.fragment;





import java.util.Date;
import java.util.Timer;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.manager.CruiseDataManager;
import com.ssm.cyclists.controller.manager.SettingsDataManager;
import com.ssm.cyclists.controller.timertask.CruiseInfoTimerTask;
import com.ssm.cyclists.view.layout.HomeLayout;

import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

	static String TAG = HomeLayout.class.getSimpleName();
	
	HomeLayout layout;
	Fragment containerFragment;
	
	public HomeFragment() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new HomeLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		if(SettingsDataManager.getInstance().isStart_stopBicycleFlag()==false){// start to bycycle
			//true update.
			CruiseDataManager.getInstance().updateCruiseData();		 
		}else{	//stop to bycycle
			//false no update and clear;
			CruiseDataManager.getInstance().clear();
		}
		
		return layout.getView();
	}


	
	public void updateHomeInfo(){
		layout.updateHomeInfo();
	}
	
	public HomeLayout getLayout() {
		assert(layout!=null);
		return layout;
	}


	public Fragment getContainerFragment() {
		return containerFragment;
	}


	public void setContainerFragment(Fragment containerFragment) {
		this.containerFragment = containerFragment;
	}

}
