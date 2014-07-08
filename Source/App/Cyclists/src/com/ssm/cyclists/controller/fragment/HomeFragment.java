package com.ssm.cyclists.controller.fragment;





import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.view.layout.HomeLayout;

import android.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

	static String TAG = HomeLayout.class.getSimpleName();
	
	HomeLayout layout;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new HomeLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		CruiseDataManager.getInstance().updateCruiseData();		 
		
		return layout.getView();
	}

		
	
	public void updateHomeInfo(){
		layout.updateHomeInfo();
	}
	
	public HomeLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

}