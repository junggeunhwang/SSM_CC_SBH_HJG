package com.ssm.ExCycling.model.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
<<<<<<< HEAD
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.model.CycleData;
import com.ssm.ExCycling.model.SettingsData;
=======
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.model.CycleData;
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CycleTrackerListViewAdapter extends BaseAdapter {
	
	private LayoutInflater Inflater;
	private ArrayList<CycleData> arSrc;
	private int layout;
	
	public void insert(CycleData data,int position){
		arSrc.add(position,data);
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		arSrc.remove(position);		
	}
	
	public CycleTrackerListViewAdapter(Context context,int alayout, ArrayList<CycleData> aarSrc) {
		Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
<<<<<<< HEAD
		arSrc = CruiseDataManager.getInstance().getCycle_data_list();
		layout = alayout;
	}
	
	public void reset(){
		arSrc = CruiseDataManager.getInstance().getCycle_data_list();
		notifyDataSetChanged();
	}
	
=======
		arSrc = aarSrc;
		layout = alayout;
	}
	
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
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
		
		
		distane.setText(String.valueOf(String.format("%.2f", arSrc.get(position).getDistance())));
		
		//�ð����
		Date d = new Date(Long.valueOf(arSrc.get(position).getDate()));
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		String strDayOfWeek = "";
		switch(c.get(Calendar.DAY_OF_WEEK)){
		case Calendar.MONDAY:
			strDayOfWeek = "MON";
			break;
		case Calendar.TUESDAY:
			strDayOfWeek = "TUES";
			break;
		case Calendar.WEDNESDAY:
			strDayOfWeek = "WED";
			break;
		case Calendar.THURSDAY:
			strDayOfWeek = "WED";
			break;
		case Calendar.FRIDAY:
			strDayOfWeek = "FRI";
			break;
		case Calendar.SATURDAY:
			strDayOfWeek = "SAT";
			break;
		case Calendar.SUNDAY:
			strDayOfWeek = "SUN";
			break;
		}
		
		String AM_PM="";
		if(c.get(Calendar.AM_PM) == Calendar.AM){
			AM_PM = "AM";
		}else{
			AM_PM = "PM";
		}
		
		String dateFormat = String.format("%d/%02d/%02d  %s %02d:%02d:%02d %s",
								c.get(Calendar.YEAR),
								c.get(Calendar.MONTH),
								c.get(Calendar.DATE),
								strDayOfWeek,
								c.get(Calendar.HOUR),
								c.get(Calendar.MINUTE),
								c.get(Calendar.SECOND),
								AM_PM);
		
		date.setText(dateFormat);
		
		date.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		cal.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		distane.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
		((TextView)convertView.findViewById(R.id.kcal_cycletracker_listview_row)).setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		((TextView)convertView.findViewById(R.id.km_cycletracker_listview_row)).setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	
		cal.setText(String.valueOf(arSrc.get(position).getTotalConsumeCalrories()));
		
		ImageView bicycle = (ImageView)convertView.findViewById(R.id.bicycle_cycletracker_listview_row);
		LinearLayout background = (LinearLayout)convertView.findViewById(R.id.background_cycletracker_listview_row);
		
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_pink));
			date.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			cal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			cal_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			distane.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			distance_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			bicycle.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.bicycle_icon_pink));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			background.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.md__list_selector_holo_green));
			date.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			cal.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			cal_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			distane.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			distance_unit.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			bicycle.setImageDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.bicycle_icon_green));
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
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
}
