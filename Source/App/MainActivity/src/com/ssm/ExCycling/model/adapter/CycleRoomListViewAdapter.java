package com.ssm.ExCycling.model.adapter;

import java.util.ArrayList;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.model.UserData;
import com.ssm.ExCycling.view.ImageViewRounded;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CycleRoomListViewAdapter extends BaseAdapter {
	
	static String TAG = CycleRoomListViewAdapter.class.getSimpleName();
	
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<UserData> arSrc;
	private int layout;
	
	public void insert(UserData data,int position){
		arSrc.add(position,data);
		notifyDataSetChanged();
	}
	
	public void add(UserData data){
		arSrc.add(data);
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		arSrc.remove(position);		
		notifyDataSetChanged();
	}
	
	public void removeAll(){
		arSrc.clear();
		notifyDataSetChanged();
	}
	
	public void reset(){
		arSrc = SettingsDataManager.getInstance().getCurrentRoomFriendList();
		MainActivity.getInstasnce().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
	
	public CycleRoomListViewAdapter(Context context,int alayout, ArrayList<UserData> aarSrc) {
		this.context = context;
		Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = SettingsDataManager.getInstance().getCurrentRoomFriendList();
		layout = alayout;
	}
	
	@Override
	public int getCount() {
		return arSrc.size();
	}

	@Override
	public Object getItem(int position) {
		return arSrc.get(position);
	}

	@Override
	public long getItemId(int position){ 
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		arSrc = SettingsDataManager.getInstance().getCurrentRoomFriendList();
		if(convertView == null){
			convertView = Inflater.inflate(layout,parent,false);
		}
		ImageViewRounded img = (ImageViewRounded)convertView.findViewById(R.id.profile_cyclroom_listview_row);

		if(arSrc.get(position).getProfileImg() == null){
			img.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.default_profile)).getBitmap());
		}
		else{
			img.setImageBitmap(arSrc.get(position).getProfileImg());	
		}
		
				
		TextView name = (TextView)convertView.findViewById(R.id.cycle_mate_id_cyclroom_listview_row);
		name.setText(arSrc.get(position).getUserName());
		
		TextView company = (TextView)convertView.findViewById(R.id.cycle_mate_company_cyclroom_listview_row);
		LinearLayout background = (LinearLayout)convertView.findViewById(R.id.backgorund_cycle_room_listview_row);
		
		name.setText(String.valueOf(arSrc.get(position).getUserName()));
		company.setText(String.valueOf(arSrc.get(position).getUniqueID()));
		
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_pink));
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			company.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_green));
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			company.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_gray));
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			company.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
		}

		return convertView;
	}
}
