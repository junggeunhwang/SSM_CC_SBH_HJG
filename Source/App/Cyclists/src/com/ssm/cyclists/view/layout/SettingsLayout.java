package com.ssm.cyclists.view.layout;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.DataBaseManager;
import com.ssm.cyclists.controller.FacebookManager;
import com.ssm.cyclists.controller.TwitterManager;
import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.fragment.SettingsFragment;
import com.ssm.cyclists.model.ResourceManager;
import com.ssm.cyclists.model.SettingsData;

import android.media.audiofx.BassBoost.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class SettingsLayout extends BaseFragmentLayout {

	static String TAG = SettingsLayout.class.getSimpleName();
	
	
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
	private LinearLayout lyTopBar;
	
	private RadioButton radioPink;
	private RadioButton radioGreen;
	private RadioButton radioGray;
	
	private	View vColorBar;
	private View vContactSnsBar;
	
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
		lyTopBar	=  (LinearLayout)getView().findViewById(R.id.top_bar_settings);
		
		radioPink = (RadioButton)getView().findViewById(R.id.radio_pink_settings);
		radioGreen = (RadioButton)getView().findViewById(R.id.radio_green_settings);
		radioGray = (RadioButton)getView().findViewById(R.id.radio_gray_settings);
		
		vColorBar = (View)getView().findViewById(R.id.color_bar_settings);
		vContactSnsBar = (View)getView().findViewById(R.id.contact_sns_bar_settings);
		
		
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
		
		radioPink.setOnCheckedChangeListener(buildRadioButtonCheckedListener());
		radioGreen.setOnCheckedChangeListener(buildRadioButtonCheckedListener());
		radioGray.setOnCheckedChangeListener(buildRadioButtonCheckedListener());
	}
		

	private OnClickListener buildMenuButtonListener(){
		
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				((MainActivity)fragment.getActivity()).open_button(v);
			}
		};
	}
	
	private OnCheckedChangeListener buildRadioButtonCheckedListener(){
		
		return new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(buttonView.equals(radioPink) && isChecked){
					Log.d(TAG,"color change : pink");
					SettingsData.getInstance().setThemeColor("pink");
					updateColor();
					DataBaseManager.getInstance().updateSettingInfo();
				}else if(buttonView.equals(radioGreen) && isChecked){
					Log.d(TAG,"color change : green");
					SettingsData.getInstance().setThemeColor("green");
					updateColor();
					DataBaseManager.getInstance().updateSettingInfo();
				}else if(buttonView.equals(radioGray) && isChecked){
					Log.d(TAG,"color change : gray");
					SettingsData.getInstance().setThemeColor("gray");
					updateColor();
					DataBaseManager.getInstance().updateSettingInfo();
				}
				
			}
		};
	}
	
	public void updateColor(){
		
		if(SettingsData.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			vColorBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			vContactSnsBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvColor.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvContactSNS.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			radioPink.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioGreen.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioGray.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioPink.setChecked(true);
			radioGreen.setChecked(false);
			radioGray.setChecked(false);
		}else if(SettingsData.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			vColorBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			vContactSnsBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvColor.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvContactSNS.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
	
			radioPink.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			radioGreen.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			radioGray.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			
			radioPink.setChecked(false);
			radioGreen.setChecked(true);
			radioGray.setChecked(false);
			
		}else if(SettingsData.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			vColorBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			vContactSnsBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvColor.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvContactSNS.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));

			radioPink.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			radioGreen.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			radioGray.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			
			radioPink.setChecked(false);
			radioGreen.setChecked(false);
			radioGray.setChecked(true);
		}
	}
}
