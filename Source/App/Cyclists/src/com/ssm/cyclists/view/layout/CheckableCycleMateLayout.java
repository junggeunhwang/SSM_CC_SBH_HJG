package com.ssm.cyclists.view.layout;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;


import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.CheckableCycleMateFragment;
import com.ssm.cyclists.controller.fragment.CycleMateFragment;
import com.ssm.cyclists.model.CheckableCycleMateListViewAdapter;
import com.ssm.cyclists.model.CycleMateListViewAdapter;
import com.ssm.cyclists.model.ResourceManager;
import com.ssm.cyclists.model.SettingsData;
import com.ssm.cyclists.model.UserData;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class CheckableCycleMateLayout extends BaseFragmentLayout{

	static String TAG = CheckableCycleMateLayout.class.getSimpleName();
	
	private Button btnCancel;
	private Button btnStart;
		
	private TextView tvFragmentName;
	
	LinearLayout lyTopBar;
	LinearLayout lyMidBar;
	
	private EditText etSearchData;

	private ListView lvCycleMate;
	
	CheckableCycleMateListViewAdapter Adapter;
	
	public CheckableCycleMateLayout(CheckableCycleMateFragment checkableCycleMateFragment) {
		super(checkableCycleMateFragment);
	}
	
	public void init(){
		
		btnCancel = (Button)getView().findViewById(R.id.cancel_button_checkable_cyclemate);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager)MainActivity.getInstasnce().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearchData.getWindowToken(), 0);
					MainActivity.getInstasnce().getLayout().getmMenuDrawer().setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
				backScreen();
			}
		});
		
		
		btnStart 		= (Button)getView().findViewById(R.id.start_button_checkable_cyclemate);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cruise_container);				
			}
		});
		
		tvFragmentName 	= (TextView)getView().findViewById(R.id.fragment_name_checkable_cyclemate);
		etSearchData 	= (EditText)getView().findViewById(R.id.et_search_data_checkable_cyclemate);
		lvCycleMate		= (ListView)getView().findViewById(R.id.lv_checkable_cyclemate);
		
		lyTopBar		= (LinearLayout)getView().findViewById(R.id.top_bar_checkable_cyclemate);
		lyMidBar		= (LinearLayout)getView().findViewById(R.id.mid_bar_checkable_cyclemate);
		
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		etSearchData.setTypeface(ResourceManager.getInstance().getFont("helveitica"));		
		
		ArrayList<UserData> arGeneral = new ArrayList<UserData>();
		UserData data = new UserData();
		data.setUserName("Ȳ����");
		arGeneral.add(data);

		Adapter = new CheckableCycleMateListViewAdapter(getView().getContext(),R.layout.checkable_cyclemate_listview_row,arGeneral);
		
		lvCycleMate.setAdapter(Adapter);

		MainActivity.getInstasnce().getLayout().getmMenuDrawer().setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
		
		updateColor();
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_checkable_cycle_mate, container, false);
	}
	


	public void backScreen(){
		MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cruise_container);
	}
	
	public void updateColor(){
		if(SettingsData.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_mid));
			etSearchData.setCompoundDrawables(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.search_small_pink),null,null,null);
//			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(SettingsData.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_mid));
			etSearchData.setCompoundDrawables(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.search_small_green),null,null,null);
//			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

		}else if(SettingsData.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_mid));
			etSearchData.setCompoundDrawables(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.search_small_gray),null,null,null);
//			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			
		}
	}
}
