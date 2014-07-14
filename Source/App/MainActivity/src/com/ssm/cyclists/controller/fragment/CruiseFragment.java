package com.ssm.cyclists.controller.fragment;

import com.ssm.cyclists.view.layout.CruiseLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CruiseFragment extends Fragment {
	
	static String TAG = CruiseFragment.class.getSimpleName();
	
	private CruiseLayout layout;
	Fragment containerFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,"onCreateView");
		layout = new CruiseLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public void updateCruiseInfo(){
		layout.updateCruiseInfo();
	}

	public CruiseLayout getLayout() {
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
