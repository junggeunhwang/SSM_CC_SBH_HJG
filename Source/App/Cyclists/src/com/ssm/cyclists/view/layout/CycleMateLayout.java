package com.ssm.cyclists.view.layout;

import java.util.ArrayList;


import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.CycleMateFragment;
import com.ssm.cyclists.model.CycleMateListViewAdapter;
import com.ssm.cyclists.model.ResourceManager;
import com.ssm.cyclists.model.UserData;
import com.ssm.cyclists.view.EnhancedListView;
import com.ssm.cyclists.view.EnhancedListView.Undoable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class CycleMateLayout extends BaseFragmentLayout{

	static String TAG = CycleMateLayout.class.getSimpleName();
	
	private String theme_color;
	
	private Button btnMenu;
	private Button btnSearch;
	private Button btnRefresh;
	
	private TextView tvFragmentName;
	private TextView tvSearch;
	
	LinearLayout lyTopBar;
	
	private EditText etSearchData;

	private EnhancedListView lvCycleMate;
	
	CycleMateListViewAdapter Adapter;
	
	public CycleMateLayout(CycleMateFragment instance) {
		super(instance);
		theme_color = "gray";
	}
	
	public void init(){

		
		btnMenu 		= (Button)getView().findViewById(R.id.menu_button_cyclemate);
		btnMenu.setOnClickListener(buildMenuButtonListener());
		
		
		btnSearch		= (Button)getView().findViewById(R.id.btn_search_cyclemate);
		btnRefresh 		= (Button)getView().findViewById(R.id.btn_refresh_cyclemate);
		tvFragmentName 	= (TextView)getView().findViewById(R.id.fragment_name_cyclemate);
		tvSearch 		= (TextView)getView().findViewById(R.id.tv_search_cyclemate);
		etSearchData 	= (EditText)getView().findViewById(R.id.et_search_data_cyclemate);
		lvCycleMate		= (EnhancedListView)getView().findViewById(R.id.lv_cyclemate);
		
		lyTopBar		= (LinearLayout)getView().findViewById(R.id.top_bar_cyclemate);
		
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		tvSearch.setTypeface(ResourceManager.getInstance().getFont("helveitica"));
		etSearchData.setTypeface(ResourceManager.getInstance().getFont("helveitica"));		
		
		ArrayList<UserData> arGeneral = new ArrayList<UserData>();
		UserData data = new UserData();
		data.setUserName("Ȳ����");
		arGeneral.add(data);

		Adapter = new CycleMateListViewAdapter(getView().getContext(),R.layout.cyclemate_listview_row,arGeneral);
		
		lvCycleMate.setAdapter(Adapter);
		lvCycleMate.setDismissCallback(buildOnDismissCallback());
				
		lvCycleMate.enableSwipeToDismiss();
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cycle_mate, container, false);
	}
	
	private EnhancedListView.OnDismissCallback buildOnDismissCallback(){
		return new EnhancedListView.OnDismissCallback() {
			
			@Override
			public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
				
				
				final UserData item = (UserData) Adapter.getItem(position);
				Adapter.remove(position);
                
				return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                    	Adapter.insert(item, position);
                    }
                };
			}
		};
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
		if(theme_color.equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			Adapter.setTheme_color("pink");
//			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(theme_color.equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			Adapter.setTheme_color("green");
//			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));

		}else if(theme_color.equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			Adapter.setTheme_color("gray");
//			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			
		}
	}
	
	public String getTheme_color() {
		 return theme_color;
	 }

	public void setTheme_color(String theme_color) {
			this.theme_color = theme_color;
	}
}
