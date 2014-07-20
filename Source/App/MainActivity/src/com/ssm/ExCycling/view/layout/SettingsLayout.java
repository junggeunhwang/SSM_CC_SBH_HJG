package com.ssm.ExCycling.view.layout;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.DataBaseManager;
import com.ssm.ExCycling.controller.manager.FacebookManager;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.controller.manager.TwitterManager;
import com.ssm.ExCycling.fragment.SettingsFragment;
<<<<<<< HEAD
import com.ssm.ExCycling.model.SettingsData;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.audiofx.BassBoost.Settings;
import android.net.wifi.WifiConfiguration.Protocol;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
=======

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
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98

public class SettingsLayout extends BaseFragmentLayout {

	static String TAG = SettingsLayout.class.getSimpleName();
	
	
	private Button btnMenu;
	
	private TextView tvFragmentName;
	private TextView tvColor;
	private TextView tvContactSNS;
	private TextView tvFacebook;
<<<<<<< HEAD
	private TextView tvAppName;
	private TextView tvName;
	
	
	private EditText etName;
	private Button btnEditName;
=======
	private TextView tvTwitter;
	private TextView tvInstagram;
	private TextView tvAppName;
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	
	private LinearLayout llFacebookLayout;
	private LinearLayout llTwitterLayout;
	private LinearLayout llInstagramLayout;
	private LinearLayout lyTopBar;
	
	private RadioButton radioPink;
	private RadioButton radioGreen;
	private RadioButton radioGray;
	
	private	View vColorBar;
	private View vContactSnsBar;
<<<<<<< HEAD
	private View vNameBar;
	
	
	private boolean editable = false;
=======
	
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
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
<<<<<<< HEAD
		tvAppName = (TextView)getView().findViewById(R.id.app_name_settings);
		tvName = (TextView)getView().findViewById(R.id.tv_name_settings);
=======
		tvTwitter = (TextView)getView().findViewById(R.id.tv_twitter_settings);
		tvInstagram = (TextView)getView().findViewById(R.id.tv_instagram_settings);
		tvAppName = (TextView)getView().findViewById(R.id.app_name_settings);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		
		llFacebookLayout = (LinearLayout)getView().findViewById(R.id.ll_facebook_settings);
		llTwitterLayout = (LinearLayout)getView().findViewById(R.id.ll_twitter_settings);
		llInstagramLayout = (LinearLayout)getView().findViewById(R.id.ll_instagram_settings);
		lyTopBar	=  (LinearLayout)getView().findViewById(R.id.top_bar_settings);
		
		radioPink = (RadioButton)getView().findViewById(R.id.radio_pink_settings);
		radioGreen = (RadioButton)getView().findViewById(R.id.radio_green_settings);
		radioGray = (RadioButton)getView().findViewById(R.id.radio_gray_settings);
		
		vColorBar = (View)getView().findViewById(R.id.color_bar_settings);
		vContactSnsBar = (View)getView().findViewById(R.id.contact_sns_bar_settings);
<<<<<<< HEAD
		vNameBar = (View)getView().findViewById(R.id.name_bar_settings);
		
		etName  = (EditText)getView().findViewById(R.id.et_edit_name_settings);
		btnEditName  = (Button)getView().findViewById(R.id.btn_edit_name_settings);
		
		etName.setText(SettingsDataManager.getInstance().getMe().getUserName());

		etName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		btnEditName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
=======
		
		
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		tvFragmentName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvColor.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvContactSNS.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvFacebook.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
<<<<<<< HEAD
		tvAppName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		etName.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etName.setInputType(InputType.TYPE_CLASS_TEXT);
		etName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
				if(actionID == EditorInfo.IME_ACTION_DONE){
					if(etName.getText().toString().equals("")){
						Toast.makeText(MainActivity.getInstasnce().getApplicationContext(),"Please enter your name.",Toast.LENGTH_SHORT).show();
					}
					else{
						btnEditName.callOnClick();
					}
				}
				return false;
			}
		});
		etName.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					MainActivity.getInstasnce().getLayout().hideSoftKeyboard(etName);
				}
			}
		});
		llFacebookLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FacebookManager.getInstance().onClickFacebook();
			}
		});

		
		btnEditName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(editable){
					if(etName.getText().toString().equals("")){
						Toast.makeText(MainActivity.getInstasnce().getApplicationContext(),"Please enter your name.",Toast.LENGTH_SHORT).show();
						return;
					}
					etName.setEnabled(false);
					editable = false;
					if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
						btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.edit_pink));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
						btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.edit_green));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
						btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.edit_gray));
					}
					SettingsDataManager.getInstance().getMe().setUserName(etName.getText().toString());
					com.ssm.ExCycling.controller.communication.https.Protocol.getInstance().UpdateProfile(SettingsDataManager.getInstance().getMe().getUniqueID());
					
					 //저장된 이름 불러오기
			        SharedPreferences pref_init_username_in = MainActivity.getInstasnce().getSharedPreferences("init_username", 0);
			        Editor pref_init_username_in_edit = pref_init_username_in.edit();
			        pref_init_username_in_edit.putString("init_username",etName.getText().toString());
			        pref_init_username_in_edit.commit();
			        
				}else{
					etName.setEnabled(true);
					editable = true;
					if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
						btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.done_check_pink));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
						btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.done_check_green));
					}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
						btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.done_check_gray));
					}
				}
			}
		});
		
		etName.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
				if(actionID == EditorInfo.IME_ACTION_DONE) btnEditName.callOnClick();
				return false;
			}
		});
=======
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
		
		getView().findViewById(R.id.btn_facebook_logout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FacebookManager.getInstance().onClickFacebookLogout();
			}
		});
		
		getView().findViewById(R.id.btn_twitter_logout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

		getView().findViewById(R.id.btn_instagram_logout).setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
});
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		
		
		radioPink.setOnCheckedChangeListener(buildRadioButtonCheckedListener());
		radioGreen.setOnCheckedChangeListener(buildRadioButtonCheckedListener());
		radioGray.setOnCheckedChangeListener(buildRadioButtonCheckedListener());
		
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
	
	private OnCheckedChangeListener buildRadioButtonCheckedListener(){
		
		return new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(buttonView.equals(radioPink) && isChecked){
					Log.d(TAG,"color change : pink");
					SettingsDataManager.getInstance().setThemeColor("pink");
					updateColor();
					DataBaseManager.getInstance().updateSettingInfo();
				}else if(buttonView.equals(radioGreen) && isChecked){
					Log.d(TAG,"color change : green");
					SettingsDataManager.getInstance().setThemeColor("green");
					updateColor();
					DataBaseManager.getInstance().updateSettingInfo();
				}else if(buttonView.equals(radioGray) && isChecked){
					Log.d(TAG,"color change : gray");
					SettingsDataManager.getInstance().setThemeColor("gray");
					updateColor();
					DataBaseManager.getInstance().updateSettingInfo();
				}
				
			}
		};
	}
	
	public void updateColor(){
		
		if(SettingsDataManager.getInstance().getThemeColor().equals("pink")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			vColorBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
			vContactSnsBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
<<<<<<< HEAD
			vNameBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_pink_heavy));
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvColor.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			tvContactSNS.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
<<<<<<< HEAD
			tvName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_pink));
			
			if(editable) btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.done_check_pink));
			else btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.edit_pink));
			
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
			radioPink.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioGreen.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioGray.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_pink));
			radioPink.setChecked(true);
			radioGreen.setChecked(false);
			radioGray.setChecked(false);
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("green")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			vColorBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
			vContactSnsBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
<<<<<<< HEAD
			vNameBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_green_heavy));
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvColor.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			tvContactSNS.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
<<<<<<< HEAD
			tvName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_green));
			
			if(editable) btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.done_check_green));
			else btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.edit_green));
=======
	
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
			radioPink.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			radioGreen.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			radioGray.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_green));
			
			radioPink.setChecked(false);
			radioGreen.setChecked(true);
			radioGray.setChecked(false);
			
		}else if(SettingsDataManager.getInstance().getThemeColor().equals("gray")){
			lyTopBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			vColorBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
			vContactSnsBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
<<<<<<< HEAD
			vNameBar.setBackgroundColor(MainActivity.getInstasnce().getResources().getColor(R.color.bk_color_gray_heavy));
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
			
			tvAppName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvColor.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			tvContactSNS.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
<<<<<<< HEAD
			tvName.setTextColor(MainActivity.getInstasnce().getResources().getColor(R.color.text_gray));
			if(editable) btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.done_check_gray));
			else btnEditName.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.edit_gray));
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98

			radioPink.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			radioGreen.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			radioGray.setButtonDrawable(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.custom_drawable_radiobox_gray));
			
			radioPink.setChecked(false);
			radioGreen.setChecked(false);
			radioGray.setChecked(true);
		}
	}
}
