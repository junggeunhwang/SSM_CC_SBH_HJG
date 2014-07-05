package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.FacebookManager;
import com.ssm.cyclists.controller.TwitterManager;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.SettingsFragment;
import com.ssm.cyclists.model.ResourceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsLayout extends BaseFragmentLayout {

	private Button btnMenu;
	
	private TextView tvFragmentName;
	private TextView tvColor;
	private TextView tvContactSNS;
	private TextView tvFacebook;
	private TextView tvTwitter;
	private TextView tvInstagram;
	private TextView tvAppName;
	
	private LinearLayout llFacebookLayout;
	private LinearLayout llTwitterLayout;
	private LinearLayout llInstagramLayout;
	
	
	public SettingsLayout(SettingsFragment instance) {
		super(instance);
	}
	
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fragment_settings, container, false);
	}
	
	public void init(){
		
		btnMenu = (Button)getView().findViewById(R.id.menu_button_settings);
		btnMenu.setOnClickListener(buildMenuButtonListener());
	
		tvFragmentName = (TextView)getView().findViewById(R.id.fragment_name_settings);
		tvColor = (TextView)getView().findViewById(R.id.tv_color_settings);
		tvContactSNS = (TextView)getView().findViewById(R.id.tv_contact_sns_settings);
		tvFacebook = (TextView)getView().findViewById(R.id.tv_facebook_settings);
		tvTwitter = (TextView)getView().findViewById(R.id.tv_twitter_settings);
		tvInstagram = (TextView)getView().findViewById(R.id.tv_instagram_settings);
		tvAppName = (TextView)getView().findViewById(R.id.app_name_settings);
		
		llFacebookLayout = (LinearLayout)getView().findViewById(R.id.ll_facebook_settings);
		llTwitterLayout = (LinearLayout)getView().findViewById(R.id.ll_twitter_settings);
		llInstagramLayout = (LinearLayout)getView().findViewById(R.id.ll_instagram_settings);
		
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvColor.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvContactSNS.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvFacebook.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvTwitter.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvInstagram.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		llFacebookLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FacebookManager.getInstance().onClickFacebook();
			}
		});
		
		llTwitterLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TwitterManager.getInstance().connect();
			}
		});
		
		llInstagramLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
		

	private OnClickListener buildMenuButtonListener(){
		
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				((MainActivity)fragment.getActivity()).open_button(v);
			}
		};
	}
	
}
