package com.ssm.ExCycling.model.adapter;

import java.util.ArrayList;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.model.UserData;
import com.ssm.ExCycling.view.ImageViewRounded;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckableCycleMateListViewAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<UserData> arSrc;
	private int layout;
	
	public CheckableCycleMateListViewAdapter() {
	}
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
	
	public void search(String target){
		reset();
		ArrayList<UserData> searchResult = new ArrayList<UserData>();
		for(int i = 0 ; i < arSrc.size();i++){
			if(arSrc.get(i).getUniqueID().contains(target) || arSrc.get(i).getUserName().contains(target)){
				searchResult.add(arSrc.get(i));
			}
		}
		
		arSrc = searchResult;
		notifyDataSetChanged();
	}

	public void reset(){
		arSrc = SettingsDataManager.getInstance().getFriendList();
<<<<<<< HEAD
		MainActivity.getInstasnce().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
=======
		notifyDataSetChanged();
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	}
	
	public CheckableCycleMateListViewAdapter(Context context,int alayout, ArrayList<UserData> aarSrc) {
		this.context = context;
		Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = SettingsDataManager.getInstance().getFriendList();
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
		
		if(convertView == null){
			convertView = Inflater.inflate(layout,parent,false);
		}
		ImageViewRounded img = (ImageViewRounded)convertView.findViewById(R.id.profile_listview_row);
		
		final CheckBox chkBox = (CheckBox)convertView.findViewById(R.id.checkbox_cyclemate_listview_row);
		chkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					arSrc.get(position).setChecked(true);
				}
				else{
					arSrc.get(position).setChecked(false);
				}
			}
		});
		
		TextView tvCompany =(TextView)convertView.findViewById(R.id.cycle_mate_company_checkable_listview_row); 
		tvCompany.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	
		LinearLayout background = (LinearLayout)convertView.findViewById(R.id.background_checkable_cyclemate_listview_row);
		background.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				chkBox.setChecked(!chkBox.isChecked());
			}
		});
		
		TextView name = (TextView)convertView.findViewById(R.id.cycle_mate_id_listview_row);
		name.setText(arSrc.get(position).getUserName());
		
		if(arSrc.get(position).getProfileImg() == null){
			img.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.default_profile)).getBitmap());
		}
		else{
			img.setImageBitmap(arSrc.get(position).getProfileImg());	
		}
		
		if(arSrc.get(position).getUserName()==null){
			name.setText(arSrc.get(position).getUniqueID());
			tvCompany.setText("");
		}		
		else{
			name.setText(arSrc.get(position).getUserName());
			tvCompany.setText(arSrc.get(position).getUniqueID());
		}
		
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_pink));
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			chkBox.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			tvCompany.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_green));
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			chkBox.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			tvCompany.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_gray));
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			chkBox.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			tvCompany.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
		}
		
		return convertView;
	}
}
