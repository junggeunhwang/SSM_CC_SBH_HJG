package com.ssm.cyclists.controller.fragment;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.view.layout.CycleTrackerDetailLayout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleTrackerDetailFragment extends Fragment {

	CycleTrackerDetailLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity.getInstasnce().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		layout = new CycleTrackerDetailLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}

	public CycleTrackerDetailLayout getLayout() {
		assert(layout!=null);
		return layout;
	}

	
	@Override
	public void onDestroy() {
		
		FragmentTransaction transaction = MainActivity.getInstasnce().getFragmentManager().beginTransaction();
		transaction.hide(this);
		transaction.show(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker());
		MainActivity.getInstasnce().getLayout().setActivated_fragment(MainActivity.getInstasnce().getLayout().getmFragmentCycleTracker());
		transaction.commit();

		super.onDestroy();
	}
	
	
}
