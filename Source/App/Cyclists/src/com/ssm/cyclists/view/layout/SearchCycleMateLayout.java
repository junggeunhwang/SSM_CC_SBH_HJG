package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.model.ResourceManager;
import com.ssm.cyclists.model.SettingsData;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchCycleMateLayout extends BaseFragmentLayout {

	static String TAG = SearchCycleMateLayout.class.getSimpleName();
	
	private Button btnCancel; 
	private Button btnSearch;
	
	private TextView fragmentName;
	private TextView comment;
	
	private EditText search_target;
	
	private LinearLayout topBar;
	private LinearLayout background;
	
	
	public SearchCycleMateLayout(Fragment instance) {
		super(instance);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_search_cyclemate, container, false);
	}
	
	public void init(){

		topBar = (LinearLayout)getView().findViewById(R.id.top_bar_search_cyclemate);
		background = (LinearLayout)getView().findViewById(R.id.background_search_cyclemate);
		
		btnCancel = (Button)getView().findViewById(R.id.cancel_button_search_cyclemate);
		btnSearch = (Button)getView().findViewById(R.id.search_button_search_cyclemate);
		
		fragmentName = (TextView)getView().findViewById(R.id.fragment_name_search_cyclemate);
		comment	 = (TextView)getView().findViewById(R.id.tv_comment_search_cyclemate);
		
		search_target = (EditText)getView().findViewById(R.id.search_target_search_cyclemate);
		
		btnCancel.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		btnSearch.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		fragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		comment.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		search_target.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager)MainActivity.getInstasnce().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(search_target.getWindowToken(), 0);
				backScreen();
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		updateColor();
	}
	
	public void updateColor(){
		if(SettingsData.getInstance().getThemeColor().equals("pink")){
			topBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			background.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_light));
		}else if(SettingsData.getInstance().getThemeColor().equals("green")){
			topBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			background.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_light));
		}else if(SettingsData.getInstance().getThemeColor().equals("gray")){
			topBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			background.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_light));
		}
	}
	
	public void backScreen(){
		FragmentTransaction transaction = MainActivity.getInstasnce().getFragmentManager().beginTransaction();
		transaction.hide(fragment);
		transaction.show(MainActivity.getInstasnce().getLayout().getmFragmentCycleMate());
		MainActivity.getInstasnce().getLayout().setActivated_fragment(MainActivity.getInstasnce().getLayout().getmFragmentCycleMate());
		transaction.commit();
		
		MainActivity.getInstasnce().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
