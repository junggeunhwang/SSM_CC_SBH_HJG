package com.ssm.cyclists.view.layout;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;


import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.communication.https.Protocol;
import com.ssm.cyclists.controller.fragment.CheckableCycleMateFragment;
import com.ssm.cyclists.controller.fragment.CycleMateFragment;
import com.ssm.cyclists.controller.manager.ResourceManager;
import com.ssm.cyclists.controller.manager.SettingsDataManager;
import com.ssm.cyclists.model.UserData;
import com.ssm.cyclists.model.adapter.CheckableCycleMateListViewAdapter;
import com.ssm.cyclists.model.adapter.CycleMateListViewAdapter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.TextWatcher;
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
	private TextView tvAppName;
	
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
				MainActivity.getInstasnce().getLayout().getmMenuDrawer().setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
				SettingsDataManager.getInstance().setStart_stopBicycleFlag(false);
				backScreen();
			}
		});
		
		
		btnStart 		= (Button)getView().findViewById(R.id.start_button_checkable_cyclemate);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().setViewPagerEnable(true);
//				MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().setViewPagerBounds(4);
				SettingsDataManager.getInstance().setStart_stopBicycleFlag(true);
				ArrayList<UserData> checkedUser = new ArrayList<UserData>();
				
				for(int i = 0 ; i < Adapter.getCount();i++){
					if(((UserData)Adapter.getItem(i)).isChecked()){
						checkedUser.add((UserData)Adapter.getItem(i));
					}
				}
				

				String friendsUniqueID = "roommember,";
				
				
				//선택된 사용자 초대
				for(int i = 0 ; i < checkedUser.size();i++){
					Protocol.getInstance().InviteFriend(SettingsDataManager.getInstance().getMe().getUniqueID(),checkedUser.get(i).getUniqueID());
					friendsUniqueID += checkedUser.get(i).getUniqueID();
					if((i+1)!=checkedUser.size()) friendsUniqueID += ",";
					SettingsDataManager.getInstance().setCurrentRoomFriendList(checkedUser);
				}
				SettingsDataManager.getInstance().getCurrentRoomFriendList().add(SettingsDataManager.getInstance().getMe());
				
				//방 초대 목록 브로드케스트
				//초대 실패 고료해야하는데... 졸리다....
				Protocol.getInstance().SendString(friendsUniqueID, SettingsDataManager.getInstance().getMe().getUniqueID());
				//뷰페이저  활성화
				MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cruise_container);
				//운항 기록 시작
				MainActivity.getInstasnce().startCruiseInfoRecord();
			}
		});
		
		tvFragmentName 	= (TextView)getView().findViewById(R.id.fragment_name_checkable_cyclemate);
		tvAppName		= (TextView)getView().findViewById(R.id.app_name_checkable_cycle_mate);
		etSearchData 	= (EditText)getView().findViewById(R.id.et_search_data_checkable_cyclemate);
		etSearchData.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(etSearchData.getText().toString().equals("")) Adapter.search("");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lvCycleMate		= (ListView)getView().findViewById(R.id.lv_checkable_cyclemate);
		
		lyTopBar		= (LinearLayout)getView().findViewById(R.id.top_bar_checkable_cyclemate);
		lyMidBar		= (LinearLayout)getView().findViewById(R.id.mid_bar_checkable_cyclemate);
		
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		etSearchData.setTypeface(ResourceManager.getInstance().getFont("helveitica"));		
		
		Adapter = new CheckableCycleMateListViewAdapter(getView().getContext(),R.layout.checkable_cyclemate_listview_row,SettingsDataManager.getInstance().getFriendList());
		
		lvCycleMate.setAdapter(Adapter);

		MainActivity.getInstasnce().getLayout().getmMenuDrawer().setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
		
		updateColor();
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_checkable_cycle_mate, container, false);
	}
	
	public void onPause() {
		MainActivity.getInstasnce().getLayout().hideSoftKeyboard(etSearchData);
	}


	public void backScreen(){
		MainActivity.getInstasnce().getLayout().getmCruiseContainerFragment().setViewPagerEnable(false);
		MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cruise_container);
	}
	
	public void refreshFriends(){
		
		Adapter.removeAll();
		
		ArrayList<UserData> list = SettingsDataManager.getInstance().getFriendList();
		
		for(int i  = 0;i < list.size() ; i++){
			Adapter.add(list.get(i));	
		}
	}
	
	public void updateColor(){
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_mid));
			etSearchData.setCompoundDrawables(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.search_small_pink),null,null,null);
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_mid));
			etSearchData.setCompoundDrawables(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.search_small_green),null,null,null);
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			lyMidBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_mid));
			etSearchData.setCompoundDrawables(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.search_small_gray),null,null,null);
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			
		}
	}
}
