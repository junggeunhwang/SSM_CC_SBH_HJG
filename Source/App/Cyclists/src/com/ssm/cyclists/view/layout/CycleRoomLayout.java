package com.ssm.cyclists.view.layout;

import java.util.ArrayList;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.model.CycleRoomListViewAdapter;
import com.ssm.cyclists.model.SettingsData;
import com.ssm.cyclists.model.UserData;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CycleRoomLayout extends BaseFragmentLayout {
	
	static String TAG = CycleRoomLayout.class.getSimpleName();
	
	private Button btnMenu;
	
	private LinearLayout lyTopBar; 
	private ListView lvCycleRoomMember;
	
	private CycleRoomListViewAdapter Adapter;
	
	public CycleRoomLayout(Fragment instance) {
		super(instance);
	}

	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_cycle_room, container, false);
		
	}
	
	public void init(){
		
		btnMenu = (Button)getView().findViewById(R.id.menu_button_cycle_room);
		btnMenu.setOnClickListener(buildMenuButtonListener());
		
		lyTopBar		= (LinearLayout)getView().findViewById(R.id.top_bar_cycle_room);
		
		lvCycleRoomMember = (ListView)getView().findViewById(R.id.cruise_mate_list_cycle_room);
		ArrayList<UserData> arGeneral = new ArrayList<UserData>();
		UserData data = new UserData();
		data.setUserName("Ȳ����");
		arGeneral.add(data);
		
		Adapter = new CycleRoomListViewAdapter(getView().getContext(),R.layout.cycle_room_listview_row,arGeneral);
		lvCycleRoomMember.setAdapter(Adapter);
		
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
		if(SettingsData.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			
		}else if(SettingsData.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			
		}else if(SettingsData.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
		}
	}
}
