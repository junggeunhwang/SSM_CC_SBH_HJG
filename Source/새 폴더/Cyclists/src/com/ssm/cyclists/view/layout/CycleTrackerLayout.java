package com.ssm.cyclists.view.layout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.CycleTrackerContainerFragment;
import com.ssm.cyclists.controller.fragment.CycleTrackerDetailGraphFragment;
import com.ssm.cyclists.controller.fragment.CycleTrackerFragment;
import com.ssm.cyclists.model.CycleData;
import com.ssm.cyclists.model.CycleMateListViewAdapter;
import com.ssm.cyclists.model.CycleTrackerListViewAdapter;
import com.ssm.cyclists.model.ResourceManager;
import com.ssm.cyclists.model.SettingsData;
import com.ssm.cyclists.model.UserData;
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
		
		
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		ArrayList<CycleData> arGeneral = new ArrayList<CycleData>();
		CycleData data = new CycleData();
		
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String tmpDate = dayTime.format(new Date(time));

		data.setDate(tmpDate);
//		data.setConsume_calrories(127);
		arGeneral.add(data);
		arGeneral.add(data);

		Adapter = new CycleTrackerListViewAdapter(getView().getContext(),R.layout.cycletracker_listview_row,arGeneral);
		
		lvCycleList.setAdapter(Adapter);
		
		lvCycleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CycleTrackerContainerFragment containerFragment = new CycleTrackerContainerFragment();
				FragmentTransaction transaction = MainActivity.getInstasnce().getFragmentManager().beginTransaction();
				transaction.add(R.id.fragment,containerFragment);
				transaction.hide(MainActivity.getInstasnce().getLayout().getActivated_fragment());
				transaction.show(containerFragment);
				MainActivity.getInstasnce().getLayout().setActivated_fragment(containerFragment);
				transaction.commit();
			}
		});
		
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
		if(SettingsData.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			Adapter.notifyDataSetChanged();
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(SettingsData.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			Adapter.notifyDataSetChanged();
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

		}else if(SettingsData.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			Adapter.notifyDataSetChanged();
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
		}
	}
}
