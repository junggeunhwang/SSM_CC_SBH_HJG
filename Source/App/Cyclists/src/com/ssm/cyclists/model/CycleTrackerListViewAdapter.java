package com.ssm.cyclists.model;

import java.util.ArrayList;
import java.util.Calendar;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CycleTrackerListViewAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<CycleData> arSrc;
	private int layout;
	private String theme_color;
	
	public void insert(CycleData data,int position){
		arSrc.add(position,data);
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		arSrc.remove(position);		
	}
	
	public CycleTrackerListViewAdapter(Context context,int alayout, ArrayList<CycleData> aarSrc) {
		this.context = context;
		Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = aarSrc;
		layout = alayout;
		theme_color = "gray";
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		
		if(convertView == null){
			convertView = Inflater.inflate(layout,parent,false);
		}
		TextView date = (TextView)convertView.findViewById(R.id.date_cycletracker_listview_row);
		
		TextView cal = (TextView)convertView.findViewById(R.id.kcal_data_cycletracker_listview_row);
		TextView cal_unit = (TextView)convertView.findViewById(R.id.kcal_cycletracker_listview_row);
		
		TextView distane = (TextView)convertView.findViewById(R.id.distance_cycletracker_listview_row);
		TextView distance_unit = (TextView)convertView.findViewById(R.id.km_cycletracker_listview_row);
		
		
		date.setText(arSrc.get(position).getDate().toString());
		
		date.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		cal.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		distane.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
		((TextView)convertView.findViewById(R.id.kcal_cycletracker_listview_row)).setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		((TextView)convertView.findViewById(R.id.km_cycletracker_listview_row)).setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	
		cal.setText(String.valueOf(arSrc.get(position).getTotalConsumeCalrories()));
		
		ImageView bicycle = (ImageView)convertView.findViewById(R.id.bicycle_cycletracker_listview_row);
		LinearLayout background = (LinearLayout)convertView.findViewById(R.id.background_cycletracker_listview_row);
		
		if(theme_color.equals("pink")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_pink));
			date.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			cal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			cal_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			distane.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			distance_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			bicycle.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.bicycle_icon_pink));
		}else if(theme_color.equals("green")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_green));
			date.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			cal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			cal_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			distane.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			distance_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			bicycle.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.bicycle_icon_green));
		}else if(theme_color.equals("gray")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_gray));
			date.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			cal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			cal_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			distane.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			distance_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			
			bicycle.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.bicycle_icon_gray));
		}
		
		return convertView;
	}

	public String getTheme_color() {
		return theme_color;
	}

	public void setTheme_color(String theme_color) {
		this.theme_color = theme_color;
		notifyDataSetChanged();
	}

}
