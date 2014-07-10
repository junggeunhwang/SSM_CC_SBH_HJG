package com.ssm.cyclists.model;

import java.util.ArrayList;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.view.ImageViewRounded;

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
import android.widget.TextView;

public class CycleMateListViewAdapter extends BaseAdapter {
	
	static String TAG = CycleMateListViewAdapter.class.getSimpleName();
	
	private Context context;
	private LayoutInflater Inflater;
	private ArrayList<UserData> arSrc;
	private int layout;
	
	public void insert(UserData data,int position){
		arSrc.add(position,data);
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		arSrc.remove(position);		
	}
	
	public CycleMateListViewAdapter(Context context,int alayout, ArrayList<UserData> aarSrc) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = Inflater.inflate(layout,parent,false);
		}
		ImageViewRounded img = (ImageViewRounded)convertView.findViewById(R.id.profile_listview_row);

		if(arSrc.get(position).getProfileImg() == null){
			img.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.profile_sample)).getBitmap());
		}
		else{
			img.setImageBitmap(arSrc.get(position).getProfileImg());	
		}
		
		Button btnDelete = (Button)convertView.findViewById(R.id.delete_cyclemate_listview_row);
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG,"delete "+arSrc.get(position).getUserName());
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.getInstasnce());
			    alt_bld.setMessage("Do you want to delete "+arSrc.get(position).getUserName()+"?").setCancelable(false)
			    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			        	remove(position);
			        	notifyDataSetChanged();
			        }}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
				            // Action for 'NO' Button
				            dialog.cancel();
				        }
			        });
			    AlertDialog alert = alt_bld.create();
			    // Title for AlertDialog
			    alert.setTitle("Deleate");
			    // Icon for AlertDialog
			    alert.setIcon(R.drawable.ic_launcher);
			    alert.show();
				
			}
		});
		
		TextView name = (TextView)convertView.findViewById(R.id.cycle_mate_id_listview_row);
		name.setText(arSrc.get(position).getUserName());
		
		TextView company = (TextView)convertView.findViewById(R.id.cycle_mate_company_listview_row);
		if(SettingsData.getInstance().getThemeColor().equals("pink")){
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			company.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			btnDelete.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.delete_pink));
		}else if(SettingsData.getInstance().getThemeColor().equals("green")){
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			company.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			btnDelete.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.delete_green));
		}else if(SettingsData.getInstance().getThemeColor().equals("gray")){
			name.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			company.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			btnDelete.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.delete_gray));
		}

		return convertView;
	}
}
