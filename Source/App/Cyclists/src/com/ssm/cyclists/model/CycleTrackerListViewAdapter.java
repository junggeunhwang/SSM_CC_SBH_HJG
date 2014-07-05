package com.ssm.cyclists.model;

import java.util.ArrayList;
import java.util.Calendar;

import com.ssm.cyclists.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CycleTrackerListViewAdapter extends BaseAdapter {
	
	Context context;
	LayoutInflater Inflater;
	ArrayList<CycleData> arSrc;
	int layout;
	
	public void insert(CycleData data,int position){
		arSrc.add(position,data);
	}
	
	public void remove(int position){
		arSrc.remove(position);		
	}
	
	public CycleTrackerListViewAdapter(Context context,int alayout, ArrayList<CycleData> aarSrc) {
		this.context = context;
		Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = aarSrc;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = Inflater.inflate(layout,parent,false);
		}
		TextView date = (TextView)convertView.findViewById(R.id.date_cycletracker_listview_row);
		TextView cal = (TextView)convertView.findViewById(R.id.kcal_data_cycletracker_listview_row);
		TextView distane = (TextView)convertView.findViewById(R.id.distance_cycletracker_listview_row);
		
		date.setText(arSrc.get(position).getDate().toString());
		
		date.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		cal.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		distane.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
		((TextView)convertView.findViewById(R.id.kcal_cycletracker_listview_row)).setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		((TextView)convertView.findViewById(R.id.km_cycletracker_listview_row)).setTypeface(ResourceManager.getInstance().getFont("helvetica"));
	
		cal.setText(String.valueOf(arSrc.get(position).getConsume_calrories()));
		
		return convertView;
	}

}
