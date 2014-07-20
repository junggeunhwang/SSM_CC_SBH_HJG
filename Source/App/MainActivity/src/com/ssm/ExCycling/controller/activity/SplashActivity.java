package com.ssm.ExCycling.controller.activity;

import twitter4j.GeoLocation;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.asynctask.WeatherUpdateAsyncTask;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
import com.ssm.ExCycling.controller.manager.DataBaseManager;
import com.ssm.ExCycling.controller.manager.ResourceManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.view.layout.SplashLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
<<<<<<< HEAD
import android.text.InputType;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import android.widget.TextView.OnEditorActionListener;

public class SplashActivity extends Activity {

	private SplashLayout layout;
	private String theme_color; 
	public SplashActivity() {
	}
	
	@SuppressLint("HandlerLeak") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
<<<<<<< HEAD
		
=======
		DataBaseManager.getInstance().selectSettingInfo();
		//테마 설정 저장
    	if(SettingsDataManager.getInstance().getThemeColor()==null)SettingsDataManager.getInstance().setThemeColor("gray");
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98

    	layout = new SplashLayout(this);
		setContentView(layout.getView());
		theme_color = getIntent().getStringExtra("color");
		
		LinearLayout lyBackground = (LinearLayout)findViewById(R.id.background_splash);
		if(lyBackground!=null){
			if(theme_color.equals("pink")){
				lyBackground.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.splash_pink));
			}else if(theme_color.equals("green")){
				lyBackground.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.splash_green));
			}else if(theme_color.equals("gray")){
				lyBackground.setBackground(MainActivity.getInstasnce().getResources().getDrawable(R.drawable.splash_gray));
			}			
		}
				
		layout.init();
		
		
    	//운동기록 읽기
    	CruiseDataManager.getInstance().setCycle_data_list(DataBaseManager.getInstance().selectCruiseData());
		ResourceManager.getInstance().setAssetManager(getAssets());

		
		GeoLocation location = DataBaseManager.getInstance().selectLastLocation();
		if(location != null){
			CruiseDataManager.getInstance().setCurrent_loc(location.getLatitude(),location.getLongitude());
		}
		WeatherUpdateAsyncTask task = new WeatherUpdateAsyncTask();
		task.execute(CruiseDataManager.getInstance().getCurrent_loc());
		
		
		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
			
				if(SettingsDataManager.getInstance().getMe().getUserName().equals("")){
					
					setContentView(R.layout.activity_initialize_user_name);
					overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
					
					final EditText etUserName = (EditText)findViewById(R.id.et_user_name_initialize);
					etUserName.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
					
					final Button btnEnjoy = (Button)findViewById(R.id.btn_enjoy_cycling_initialize);
<<<<<<< HEAD
					
					etUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);
					etUserName.setInputType(InputType.TYPE_CLASS_TEXT);
					etUserName.setOnEditorActionListener(new OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
							
							if(actionID == EditorInfo.IME_ACTION_DONE){
								if(etUserName.getText().toString().equals(""))
									Toast.makeText(getApplicationContext(),"Please enter your name.",Toast.LENGTH_SHORT).show();
								else btnEnjoy.callOnClick();
=======
					etUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);
					etUserName.setOnEditorActionListener(new OnEditorActionListener() {
						
						@Override
						public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
							if((actionID == EditorInfo.IME_ACTION_DONE) || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
								btnEnjoy.callOnClick();
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
							}
							return false;
						}
					});
<<<<<<< HEAD
					
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
					btnEnjoy.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
					btnEnjoy.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
<<<<<<< HEAD
							if(etUserName.getText().toString().equals(""))
							{
								Toast.makeText(getApplicationContext(),"Please enter your name.",Toast.LENGTH_SHORT).show();
								return;
							}
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
							//이름 입력정보 저장
							SharedPreferences pref_init_username_out = getSharedPreferences("init_username", 0);
							Editor editor_init_username = pref_init_username_out.edit();
							editor_init_username.putString("init_username",etUserName.getText().toString());
							editor_init_username.commit();
							
							SettingsDataManager.getInstance().getMe().setUserName(etUserName.getText().toString());
							finish();
							overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
						}
					});
				}
				else{
					finish();
					overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
				}
				super.handleMessage(msg);
			}
		};


		handler.sendEmptyMessageDelayed(0, 2500);
		
	}
<<<<<<< HEAD
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
	}
=======

>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	}
}
