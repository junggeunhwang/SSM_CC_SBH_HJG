package com.ssm.ExCycling.fragment;

import net.simonvt.menudrawer.MenuDrawer;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleTrackerContainerFragment extends Fragment {
	
	static String TAG = CycleTrackerContainerFragment.class.getSimpleName();
	
	public static CycleTrackerDetailGraphFragment mCycleTrackerDetailGraphFragment = new CycleTrackerDetailGraphFragment();
	public static CycleTrackerDetailMapFragment mCycleTrackerDetailMapFragmnet = new CycleTrackerDetailMapFragment();
	
	private ViewPager mPager;
	private PageAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.fragment_cruise_container, container, false);
		
		mAdapter = new PageAdapter(getChildFragmentManager());
		
		mPager = (ViewPager)root.findViewById(R.id.container);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		
		return root;
	}
	private static final class PageAdapter extends FragmentStatePagerAdapter {

		public PageAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			Log.d(TAG,"getItem : "+String.valueOf(position));
			switch (position)
			{
			case 0:
				return mCycleTrackerDetailGraphFragment;
				
			case 1:
				return mCycleTrackerDetailMapFragmnet;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
	
	public void updateColor(){
		int current_item = mPager.getCurrentItem();
		
		switch(current_item)
		{
		case 0:

			break;
		case 1:

			break;			
		}
	}

	public static CycleTrackerDetailGraphFragment getmCycleTrackerDetailGraphFragment() {
		return mCycleTrackerDetailGraphFragment;
	}

	public static CycleTrackerDetailMapFragment getmCycleTrackerDetailMapFragmnet() {
		return mCycleTrackerDetailMapFragmnet;
	}

}
