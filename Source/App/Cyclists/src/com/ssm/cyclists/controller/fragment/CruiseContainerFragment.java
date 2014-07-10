package com.ssm.cyclists.controller.fragment;


import com.ssm.cyclists.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CruiseContainerFragment extends Fragment {

	static String TAG = CruiseContainerFragment.class.getSimpleName();
	
	private static HomeFragment	  mHomeFragment = new HomeFragment();
	private static CruiseFragment mCruiseFragment = new CruiseFragment();
	private static CycleRoomFragment mCruiseTwoFragment = new CycleRoomFragment();
	private static MapViewFragment	mMapViewFragment = new MapViewFragment();

	
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
				
				return mHomeFragment;
			case 1:
				return mCruiseFragment;
			case 2:
				return mCruiseTwoFragment;
			case 3:
				return mMapViewFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 4;
		}

	}
	
	public void updateColor(){
		int current_item = mPager.getCurrentItem();
		
		switch(current_item)
		{
		case 0:
			mHomeFragment.getLayout().updateColor();
			mCruiseFragment.getLayout().updateColor();
			break;
		case 1:
			mHomeFragment.getLayout().updateColor();
			mCruiseFragment.getLayout().updateColor();
			mCruiseTwoFragment.getLayout().updateColor();
			break;
		case 2:
			mCruiseFragment.getLayout().updateColor();
			mCruiseTwoFragment.getLayout().updateColor();
			mMapViewFragment.getLayout().updateColor();
			break;
		case 3:
			mCruiseTwoFragment.getLayout().updateColor();
			mMapViewFragment.getLayout().updateColor();
			break;
			
		}
	}
	
	public static CruiseFragment getmCruiseFragment() {
		return mCruiseFragment;
	}
	

	public static CycleRoomFragment getmCruiseTwoFragment() {
		return mCruiseTwoFragment;
	}


	public static HomeFragment getmHomeFragment() {
		return mHomeFragment;
	}


	public static MapViewFragment getmMapViewFragment() {
		return mMapViewFragment;
	}


}
