package com.ssm.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.cyclists.R;
import com.ssm.controller.CycleMateFragment;
import com.ssm.controller.CycleTrackerFragment;
import com.ssm.controller.HomeFragment;
import com.ssm.controller.MainActivity;
import com.ssm.controller.MapFragment;
import com.ssm.controller.SettingsFragment;

import net.simonvt.menudrawer.MenuDrawer;

public class MainLayout{
	
	public interface Listener{
	}
	
	public static final String STATE_MENUDRAWER = "com.ssm.view.menuDrawer";
	public static final String STATE_ACTIVE_VIEW_ID = "com.ssm.view.activeViewId";
	  
	private MenuDrawer mMenuDrawer;
	private int mActiveViewId;
	private int view = R.layout.activity_main;
	
	MainActivity activity;
	
	Button menu_button;
	
	public MainLayout(MainActivity instance) {
		activity = instance;
	}
	
	public void init(){
		
		 mMenuDrawer = MenuDrawer.attach(activity, MenuDrawer.MENU_DRAG_WINDOW);
		 
	     mMenuDrawer.setContentView(R.layout.activity_main);
	     mMenuDrawer.setMenuView(R.layout.menu_scrollview);

	     menu_button = (Button)activity.findViewById(R.id.menu_btn);
	     menu_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				open_button(v);
				
			}
	    	 
	     });
	     
	     activity.findViewById(R.id.home_menu).setOnClickListener(buildClickListenr());
	     
	     activity.findViewById(R.id.cycle_tracker_menu).setOnClickListener(buildClickListenr());
	     activity.findViewById(R.id.cycle_mate_menu).setOnClickListener(buildClickListenr());
	     activity.findViewById(R.id.settings_menu).setOnClickListener(buildClickListenr());
	     activity.findViewById(R.id.map_menu).setOnClickListener(buildClickListenr());
	     activity.findViewById(R.id.item6).setOnClickListener(buildClickListenr());
	     activity.findViewById(R.id.menu_btn).setOnClickListener(buildClickListenr());
	    
	     TextView activeView = (TextView)activity.findViewById(mActiveViewId);
	     if (activeView != null) {
	          mMenuDrawer.setActiveView(activeView);
	     }
	 
	     replaceFragment(R.layout.fragment_home);
	}
	
	public void replaceFragment(int resID){
		
		Fragment newFragment = null;
		
		newFragment = getFragment(resID);
		
		final FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment,newFragment);
		
		transaction.commit();
	}
	
	public Fragment getFragment(int resID){
		Fragment newFragment = null;
		
		switch(resID)
		{
		case R.layout.fragment_home:
			newFragment = new HomeFragment();
			break;
		case R.layout.fragment_cycle_tracker:
			newFragment = new CycleTrackerFragment();
			break;
		case R.layout.fragment_cycle_mate:
			newFragment = new CycleMateFragment();
			break;
		case R.layout.fragment_map:
			newFragment = new MapFragment();
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
			     case R.id.menu_btn:
			    	 activity.open_button(v);
			    	 break;
			     case R.id.home_menu:
			    	 replaceFragment(R.layout.fragment_home);
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
