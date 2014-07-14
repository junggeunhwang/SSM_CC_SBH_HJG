package com.ssm.cyclists.view.layout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.CycleTrackerContainerFragment;
import com.ssm.cyclists.controller.fragment.CycleTrackerDetailGraphFragment;
import com.ssm.cyclists.controller.fragment.CycleTrackerFragment;
import com.ssm.cyclists.controller.manager.CruiseDataManager;
import com.ssm.cyclists.controller.manager.ResourceManager;
import com.ssm.cyclists.controller.manager.SettingsDataManager;
import com.ssm.cyclists.model.CycleData;
import com.ssm.cyclists.model.UserData;
import com.ssm.cyclists.model.adapter.CycleMateListViewAdapter;
import com.ssm.cyclists.model.adapter.CycleTrackerListViewAdapter;
import com.ssm.cyclists.view.EnhancedListView;

import android.app.FragmentTransaction;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class CycleTrackerLayout extends BaseFragmentLayout {
	
	static String TAG = CycleTrackerLayout.class.getSimpleName();
	
	
	
	private Button btnMenu;
	private ListView lvCycleList;
	
	private LinearLayout lyTopBar;
	private LinearLayout background;
	private CycleTrackerListViewAdapter Adapter;
	
	private TextView tvAppName;
	private TextView tvFragmentName;
	
	
	
	public CycleTrackerLayout(CycleTrackerFragment instance) {
		super(instance);
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container){
		view = inflater.inflate(R.layout.fragment_cycle_tracker, container, false);
	}
	
	public void init(){
		btnMenu 		= (Button)getView().findViewById(R.id.menu_button_cycletracker);
		btnMenu.setOnClickListener(buildMenuButtonListener());
		
		tvAppName = (TextView)getView().findViewById(R.id.app_name_cycletracker);
		tvFragmentName = (TextView)getView().findViewById(R.id.fragment_name_cycletracker);
		
		lvCycleList		= (ListView)getView().findViewById(R.id.lv_cycletracker);
		lyTopBar		= (LinearLayout)getView().findViewById(R.id.top_bar_cycle_tracker);
		background		= (LinearLayout)getView().findViewById(R.id.background_cycle_tracker);
		
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		Adapter = new CycleTrackerListViewAdapter(getView().getContext(),R.layout.cycletracker_listview_row,CruiseDataManager.getInstance().getCycle_data_list());
		
//		if(Adapter.getCount()==0)
//			lvCycleList.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.bk_no_result));
		lvCycleList.setAdapter(Adapter);
		
		lvCycleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent , View view, int position,long id) {
				((CycleTrackerFragment)fragment).setCurrentActivatedData((CycleData)Adapter.getItem(position));
				MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cycle_tracker_detail_container);
			}
		});
		
		updateColor();
	}
	
	
	private OnClickListener buildMenuButtonListener(){
		
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				((MainActivity)fragment.getActivity()).open_button(v);
			}
		};
	}
	
	public void updateColor(){
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			Adapter.notifyDataSetChanged();
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			Adapter.notifyDataSetChanged();
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			Adapter.notifyDataSetChanged();
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
		}
	}
}