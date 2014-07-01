package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.CruiseFragment;
import com.ssm.cyclists.controller.CycleMateFragment;
import com.ssm.cyclists.controller.CycleTrackerFragment;
import com.ssm.cyclists.controller.HomeFragment;
import com.ssm.cyclists.controller.MainActivity;
import com.ssm.cyclists.controller.MapViewFragment;
import com.ssm.cyclists.controller.SettingsFragment;
import com.ssm.cyclists.model.ResourceManager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import net.simonvt.menudrawer.MenuDrawer;

public class MainLayout{
	
	HomeFragment 			mFragmentHome;
	CruiseFragment			mFragmentCruise;
	CycleMateFragment 		mFragmentCycleMate;
	CycleTrackerFragment 	mFragmentCycleTracker;
	MapViewFragment 		mMapViewFragment;
	SettingsFragment 		mSettingsFragment;
	
	public interface Listener{
	}
	
	public static final String STATE_MENUDRAWER = "com.ssm.view.menuDrawer";
	public static final String STATE_ACTIVE_VIEW_ID = "com.ssm.view.activeViewId";
	  
	private MenuDrawer mMenuDrawer;
	private int mActiveViewId;
	private int view = R.layout.activity_main;
	
	private TextView tvHomeMenu;
	private TextView tvCruiseMenu;
	private TextView tvCycleTrackerMenu;
	private TextView tvCycleMateMenu;
	private TextView tvMapMenu;
	private TextView tvSettingsMenu;
	
	MainActivity activity;
	
	Fragment activated_fragment;
	
	public MainLayout(MainActivity instance) {
		activity = instance;
	}
	
	public void init(){
		
		 mMenuDrawer = MenuDrawer.attach(activity, MenuDrawer.MENU_DRAG_WINDOW);
		 
	     mMenuDrawer.setContentView(R.layout.activity_main);
	     mMenuDrawer.setMenuView(R.layout.menu_scrollview);

	     tvHomeMenu = (TextView)activity.findViewById(R.id.home_menu);
	     tvCruiseMenu = (TextView)activity.findViewById(R.id.cruise_menu);
	     tvCycleTrackerMenu = (TextView)activity.findViewById(R.id.cycle_tracker_menu);
	     tvCycleMateMenu = (TextView)activity.findViewById(R.id.cycle_mate_menu);
	     tvMapMenu = (TextView)activity.findViewById(R.id.map_menu);
	     tvSettingsMenu = (TextView)activity.findViewById(R.id.settings_menu);
	     
	     tvHomeMenu.setOnClickListener(buildClickListenr());
	     tvCruiseMenu.setOnClickListener(buildClickListenr());
	     tvCycleTrackerMenu.setOnClickListener(buildClickListenr());
	     tvCycleMateMenu.setOnClickListener(buildClickListenr());
	     tvMapMenu.setOnClickListener(buildClickListenr());
	     tvSettingsMenu.setOnClickListener(buildClickListenr());
	     
	     tvHomeMenu.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	     tvCruiseMenu.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	     tvCycleTrackerMenu.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	     tvCycleMateMenu.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	     tvMapMenu.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	     tvSettingsMenu.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	     
//	     activity.findViewById(R.id.home_menu).setOnClickListener(buildClickListenr());
//	     activity.findViewById(R.id.cycle_tracker_menu).setOnClickListener(buildClickListenr());
//	     activity.findViewById(R.id.cycle_mate_menu).setOnClickListener(buildClickListenr());
//	     activity.findViewById(R.id.settings_menu).setOnClickListener(buildClickListenr());
//	     activity.findViewById(R.id.map_menu).setOnClickListener(buildClickListenr());
//	     activity.findViewById(R.id.cruise_menu).setOnClickListener(buildClickListenr());
	     
	    
	     TextView activeView = (TextView)activity.findViewById(mActiveViewId);
	     if (activeView != null) {
	          mMenuDrawer.setActiveView(activeView);
	     }
	     
	     mFragmentHome = new HomeFragment();
		 mFragmentCruise = new CruiseFragment();
         mFragmentCycleMate = new CycleMateFragment();
		 mFragmentCycleTracker = new CycleTrackerFragment();
		 mMapViewFragment = new MapViewFragment();
		 mSettingsFragment = new SettingsFragment();
	     
		 FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
		 transaction.add(R.id.fragment, mFragmentHome);
		 transaction.add(R.id.fragment, mFragmentCruise);
		 transaction.add(R.id.fragment, mFragmentCycleMate);
		 transaction.add(R.id.fragment, mFragmentCycleTracker);
		 transaction.add(R.id.fragment, mMapViewFragment);
		 transaction.add(R.id.fragment, mSettingsFragment);
		 
		 transaction.show(mFragmentHome);
		 transaction.hide(mFragmentCruise);
		 transaction.hide(mFragmentCycleMate);
		 transaction.hide(mFragmentCycleTracker);
		 transaction.hide(mMapViewFragment);
		 transaction.hide(mSettingsFragment);
		 
		 transaction.commit();
		 
		 activated_fragment = mFragmentHome;
	    
	}
	
	public void replaceFragment(int resID){
		
		Fragment newFragment = null;
		final FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
		
		switch(resID)
		{
		case R.layout.fragment_home:
			newFragment = mFragmentHome;
			transaction.show(newFragment);
			if(!newFragment.equals(activated_fragment)) 
				transaction.hide(activated_fragment);
			activated_fragment = mFragmentHome;
			break;
		case R.layout.fragment_cruise:
			newFragment = mFragmentCruise;
			transaction.show(newFragment);
			if(!newFragment.equals(activated_fragment)) transaction.hide(activated_fragment);
			activated_fragment = mFragmentCruise;
			break;
		case R.layout.fragment_cycle_tracker:
			newFragment = mFragmentCycleTracker;
			transaction.show(newFragment);
			if(!newFragment.equals(activated_fragment)) transaction.hide(activated_fragment);
			activated_fragment = mFragmentCycleTracker;
			break;
		case R.layout.fragment_cycle_mate:
			newFragment = mFragmentCycleMate;
			transaction.show(newFragment);
			if(!newFragment.equals(activated_fragment)) transaction.hide(activated_fragment);
			activated_fragment = mFragmentCycleMate;
			break;
		case R.layout.fragment_map:
			newFragment = mMapViewFragment;
			transaction.show(newFragment);
			if(!newFragment.equals(activated_fragment)) transaction.hide(activated_fragment);
			activated_fragment = mMapViewFragment;
			break;
		case R.layout.fragment_settings:
			newFragment = mSettingsFragment;
			transaction.show(newFragment);
			if(!newFragment.equals(activated_fragment)) transaction.hide(activated_fragment);
			activated_fragment = mSettingsFragment;
			break;
		}

		transaction.commit();
	}
	
	public Fragment getFragment(int resID){
		Fragment newFragment = null;
		
		switch(resID)
		{
		case R.layout.fragment_home:
			newFragment = new HomeFragment();
			break;
		case R.layout.fragment_cruise:
			newFragment = new CruiseFragment();
			break;
		case R.layout.fragment_cycle_tracker:
			newFragment = new CycleTrackerFragment();
			break;
		case R.layout.fragment_cycle_mate:
			newFragment = new CycleMateFragment();
			break;
		case R.layout.fragment_map:
			newFragment = new MapViewFragment();
			break;
		case R.layout.fragment_settings:
			newFragment = new SettingsFragment();
			break;
		}
		return newFragment;
	}
	
	private OnClickListener buildClickListenr(){
		return new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				 mMenuDrawer.setActiveView(v);
			     mMenuDrawer.closeMenu();
			     mActiveViewId = v.getId();
			     switch(mActiveViewId){
			     case R.id.home_menu:
			    	 replaceFragment(R.layout.fragment_home);
			    	 break;
			     case R.id.cruise_menu:
			    	 replaceFragment(R.layout.fragment_cruise);
			    	 break;
			     case R.id.cycle_tracker_menu:
			    	 replaceFragment(R.layout.fragment_cycle_tracker);
			    	 break;
			     case R.id.cycle_mate_menu:
			    	 replaceFragment(R.layout.fragment_cycle_mate);
			    	 break;
			     case R.id.map_menu:
			    	 replaceFragment(R.layout.fragment_map);
			    	 break;
			     case R.id.settings_menu:
			    	 replaceFragment(R.layout.fragment_settings);
			    	 break;
			     }
			     
			}
		};
	}
	
	public void open_button(View v){
    	mMenuDrawer.openMenu();
    }
	
	public void toggle_menu(){
		 mMenuDrawer.toggleMenu();
	}
		
	public void onBackBtnPressed(){
		final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }
	}
	
	public void onRestoreInstanceState(Bundle inState){
		mMenuDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
	}
	
	public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
    }
	    
	public int getView(){
		return view;
	}

	public void setActiveViewID(int activeViewId){
		mActiveViewId = activeViewId;
	}
}