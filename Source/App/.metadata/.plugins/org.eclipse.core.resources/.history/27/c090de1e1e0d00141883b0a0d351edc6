package com.ssm.ExCycling.view.layout;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;



import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.communication.https.Protocol;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.fragment.CheckableCycleMateFragment;
import com.ssm.ExCycling.fragment.CycleMateFragment;
import com.ssm.ExCycling.model.UserData;
import com.ssm.ExCycling.model.adapter.CheckableCycleMateListViewAdapter;
import com.ssm.ExCycling.model.adapter.CycleMateListViewAdapter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;


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
				SettingsDataManager.getInstance().setStart_stopBicycleFlag(false);
				backScreen();
			}
		});
		
		
		btnStart 		= (Button)getView().findViewById(R.id.start_button_checkable_cyclemate);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainLayout.getmCruiseContainerFragment().setViewPagerEnable(true);
				SettingsDataManager.getInstance().setStart_stopBicycleFlag(true);
				
				ArrayList<UserData> checkedUser = new ArrayList<UserData>();
				for(int i = 0 ; i < Adapter.getCount();i++){
					if(((UserData)Adapter.getItem(i)).isChecked()){
						checkedUser.add((UserData)Adapter.getItem(i));
					}
				}

				SettingsDataManager.getInstance().setCurrentRoomFriendList(checkedUser);
				//¹æ »ý¼º
				Protocol.getInstance().MakeRoom(SettingsDataManager.getInstance().getMe().getUniqueID());
			}
		});
		
		tvFragmentName 	= (TextView)getView().findViewById(R.id.fragment_name_checkable_cyclemate);
		tvAppName		= (TextView)getView().findViewById(R.id.app_name_checkable_cycle_mate);
		etSearchData 	= (EditText)getView().findViewById(R.id.et_search_data_checkable_cyclemate);
		etSearchData.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(etSearchData.getText().toString().equals("")) {
					if(Adapter!=null) Adapter.search("");
				}
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
		
		etSearchData.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		etSearchData.setInputType(InputType.TYPE_CLASS_TEXT);
		
		etSearchData.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
				if(actionID == EditorInfo.IME_ACTION_SEARCH) {
					Adapter.search(etSearchData.getText().toString());
				}
				return false;
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
		MainActivity.getInstasnce().getLayout().hideSoftKeyboard(etSearchData);
		MainLayout.getmCruiseContainerFragment().setViewPagerEnable(false);
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
