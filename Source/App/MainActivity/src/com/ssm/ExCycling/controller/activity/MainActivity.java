package com.ssm.ExCycling.controller.activity;


import java.io.File;
import java.util.Date;
import java.util.Timer;
<<<<<<< HEAD
=======



>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import java.util.TimerTask;

import net.simonvt.menudrawer.MenuDrawer;

<<<<<<< HEAD
=======
import com.ssm.ExCycling.LawRightDialog;
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.communication.https.Protocol;
import com.ssm.ExCycling.controller.communication.sapinterface.FileAction;
import com.ssm.ExCycling.controller.communication.sapinterface.SAPProviderService;
import com.ssm.ExCycling.controller.communication.sapinterface.StringAction;
import com.ssm.ExCycling.controller.communication.sapinterface.SAPProviderService.LocalBinder;
import com.ssm.ExCycling.controller.manager.CruiseDataManager;
<<<<<<< HEAD
import com.ssm.ExCycling.controller.manager.DataBaseManager;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import com.ssm.ExCycling.controller.manager.FacebookManager;
import com.ssm.ExCycling.controller.manager.GoogleLocationManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.controller.manager.TwitterManager;
import com.ssm.ExCycling.controller.timertask.CruiseInfoTimerTask;
<<<<<<< HEAD
import com.ssm.ExCycling.controller.timertask.DistDiffMonitorTimerTask;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import com.ssm.ExCycling.controller.timertask.GetTask;
import com.ssm.ExCycling.fragment.CycleTrackerContainerFragment;
import com.ssm.ExCycling.model.TwitterBasicInfo;
import com.ssm.ExCycling.model.UserData;
<<<<<<< HEAD
import com.ssm.ExCycling.view.LawRightDialog;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import com.ssm.ExCycling.view.layout.MainLayout;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
<<<<<<< HEAD
import android.content.ContentResolver;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import android.content.Context; 
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
<<<<<<< HEAD
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
=======
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	//my instance
	private static MainActivity instance;
	
	private static final String TAG_SAP = "SAPProvider";
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private static final String DEST_DIR  = "/storage/emulated/legacy/Cyclists";
	private static final String DEST_DIR_RECEIVE  = DEST_DIR+"/Receive";
	private static final String DEST_DIR_SEND  = DEST_DIR+ "/Send" ;
	
	private int noti_num=0;
	
	//gps
	private GoogleLocationManager googleLocationManager;
	
	private Context mCtxt;
	// 서비스 접근
	private SAPProviderService mSAPService;
	 
	public int mTransId;
	
	private Timer mTimer;
	private GetTask getTask;
	
	//알림  매니저
	NotificationManager notiMgr;
	final static int MyNoti=0;
	
	//진동
	Vibrator vibrator;
	
	//전화번호
	TelephonyManager mTelephonyManager;
	String myNumber;
	
	//cruiseInfoRecordTimer
	Timer cruiseInfoTimer;
	CruiseInfoTimerTask cruiseInfoTimerTask;
	
<<<<<<< HEAD
	//DistDiffMonitorTimertask
	Timer distDiffMonitorTimer;
	DistDiffMonitorTimerTask distDiffMonitorTimerTask;
	
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	//layout
	private MainLayout layout;
	
	//종료 플레그
	int exit_flag = 0;
<<<<<<< HEAD

	//Wifi 꺼짐 방지
	WifiManager.WifiLock wifiLock = null;
=======
	
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	
	public MainActivity() {
		this.instance = this;
	}
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	this.instance = this;
    	// service
    	mCtxt = getApplicationContext();
        mCtxt.bindService(new Intent(getApplicationContext(), SAPProviderService.class), 
                 this.mSAPConnection, Context.BIND_AUTO_CREATE);
<<<<<<< HEAD
        DataBaseManager.getInstance().selectSettingInfo();
		//테마 설정 저장
    	if(SettingsDataManager.getInstance().getThemeColor()==null)SettingsDataManager.getInstance().setThemeColor("gray");
    	
    	if(wifiLock==null){
    		WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    		wifiLock = wifiManager.createWifiLock("wifilock");
    		wifiLock.setReferenceCounted(true);
    		wifiLock.acquire();
    	}
    	//전화번호
        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        myNumber = mTelephonyManager.getLine1Number();
        if(myNumber==null)myNumber = "01098765432";
//       	myNumber = "01012345678";
        
        Log.d(TAG,"my number : "+myNumber);
        	
        UserData me = SettingsDataManager.getInstance().getMe();
        me.setUniqueID(myNumber);
        SettingsDataManager.getInstance().setMe(me);
        	
        Protocol.getInstance().UpdateProfile(SettingsDataManager.getInstance().getMe().getUniqueID());
        Protocol.getInstance().FriendsListRequest(SettingsDataManager.getInstance().getMe().getUniqueID());

        //저장된 이름 불러오기
=======
        
        
        
        //전화번호 얻기 -> 로그인 -> 임시파일 저장할 디렉토리 생성 까지 멀티쓰레드 사용으로 성능 향상
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				//전화번호
		        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		        myNumber = mTelephonyManager.getLine1Number();
		        if(myNumber==null)myNumber = "01098765432";
//		       	myNumber = "01012345678";
		        	
		        Log.d(TAG,"my number : "+myNumber);
		        	
		        UserData me = SettingsDataManager.getInstance().getMe();
		        me.setUniqueID(myNumber);
		        SettingsDataManager.getInstance().setMe(me);
		        	
		        //저장된 이름 불러오기
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		        SharedPreferences pref_init_username_in = getSharedPreferences("init_username", 0);
		        String storedUserName = pref_init_username_in.getString("init_username", null);
		        if(storedUserName!=null) SettingsDataManager.getInstance().getMe().setUserName(storedUserName);
		          	
<<<<<<< HEAD
		        Protocol.getInstance().UpdateProfile(SettingsDataManager.getInstance().getMe().getUniqueID());
				Protocol.getInstance().FriendsListRequest(SettingsDataManager.getInstance().getMe().getUniqueID());
				
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		        //위치정보 법 관련 다이얼로그 수락 저장
		        SharedPreferences pref1 = getSharedPreferences("law", 0);
		        if(!pref1.getBoolean("right", false)){
		        	LawRightDialog dialog = new LawRightDialog(MainActivity.getInstasnce());
		        	dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		        	dialog.setCancelable(false);
		        	dialog.show();
		        }
		          	
<<<<<<< HEAD
=======
		        Protocol.getInstance().Login(myNumber);
		        
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		       	MainActivity.getInstasnce().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		    	 
		    	File RootDir = new File(DEST_DIR);
		    	if(!RootDir.exists()){
		    		RootDir.mkdir();
		    	}
		    	
		    	File SendDir = new File(DEST_DIR_SEND);
		    	if(!SendDir.exists()){
		    		SendDir.mkdir();
		    	}
		    	
		    	File ReceiveDir = new File(DEST_DIR_RECEIVE);
		    	if(!ReceiveDir.exists()){
		    		ReceiveDir.mkdir();
		    	}
<<<<<<< HEAD

    	
        //Facebook 초기화       
        FacebookManager.getInstance().init(savedInstanceState);
    	
        
=======
			}
		}).run();
    	
        //Faceboo 초기화       
        FacebookManager.getInstance().init(savedInstanceState);
    	
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    	//Splash 시작
    	Intent intent = new Intent(this,SplashActivity.class);
    	intent.putExtra("color", SettingsDataManager.getInstance().getThemeColor());
    	
    	startActivity(intent);
    	
		SharedPreferences pref2 = getSharedPreferences("law", 0);
		Editor editor = pref2.edit();
		editor.putBoolean("right",true);
		editor.commit();

		
    	//위치 서비스
    	googleLocationManager = new GoogleLocationManager();
    	googleLocationManager.init(this);

    	CruiseDataManager.getInstance().updateCruiseData();
    	
    	super.onCreate(savedInstanceState);
    	
    	layout = new MainLayout(this);
    	setContentView(layout.getView());
        layout.init();
        startGetRequest();
        
    }

    @Override
    protected void onResume() {
    	this.instance = this;
    	super.onStart();
    	FacebookManager.getInstance().start();
    	googleLocationManager.resume();
    	super.onResume();
    }

    @Override
    protected void onDestroy() {
<<<<<<< HEAD

    	if(wifiLock!=null){
    		wifiLock.release();
    		wifiLock = null;
    	}
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    	
    	mSAPService.setVirtualBindStatus(false);
    	mSAPService.closeGearConnection();
    	mCtxt.unbindService(mSAPConnection);
    	
    	getFragmentManager().beginTransaction().remove(MainLayout.getmCruiseContainerFragment());
    	super.onDestroy();
    }
    
    @Override
    protected void onPause() {
    	Log.d(TAG,"onPause");
    	FacebookManager.getInstance().stop();
    	googleLocationManager.pause();
    	super.onPause();
    }
   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	FacebookManager.getInstance().onActivityResult(requestCode, resultCode, data);
    	if(requestCode == TwitterBasicInfo.REQ_CODE_TWIT_LOGIN)
    		TwitterManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        layout.onRestoreInstanceState(inState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        layout.onSaveInstanceState(outState);
        FacebookManager.getInstance().onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
    	Log.d(TAG,"onBackPressed");
    	final int drawerState = layout.getmMenuDrawer().getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
        	layout.getmMenuDrawer().closeMenu();
            return;
        }
        
        if(layout.getActivated_fragment().getClass().equals(CycleTrackerContainerFragment.class))
        	CycleTrackerContainerFragment.getmCycleTrackerDetailGraphFragment().getLayout().backScreen();

        else if(layout.getActivated_fragment().equals(MainLayout.getmCruiseContainerFragment())){
        	//운동중엔 백버튼 무력화
        	if(SettingsDataManager.getInstance().isStart_stopBicycleFlag()  == true){
        		return;
        	}
        	
        	if(exit_flag==1){
        		stopGetRequest();
        		Protocol.getInstance().DismissProgressDialog();
<<<<<<< HEAD
=======
        		Protocol.getInstance().Logout(SettingsDataManager.getInstance().getMe().getUniqueID());
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
        		finish();
            	System.exit(0);
        	}
        	else if(exit_flag==0){
        		Toast.makeText(getApplicationContext(),"If you one more click, App is exit.", Toast.LENGTH_SHORT).show();
        		exit_flag++;
        	}
        	
        	Timer t = new Timer();
        	t.schedule(new TimerTask(){

				@Override
				public void run() {
					exit_flag=0;
				}
        		
        	},2000);
        }
        else{
        	layout.replaceFragment(R.layout.fragment_cruise_container);
        	layout.getmMenuDrawer().setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        }
    }
    
  
    
    
    private ServiceConnection mSAPConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG_SAP, "SAP service connection lost");
            mSAPService = null;
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) 
        {
            Log.d(TAG_SAP, "SAP service connected");
            mSAPService = ((LocalBinder) service).getService();
            mSAPService.setVirtualBindStatus(true);
            // 문자열 관련 수신 액션
            mSAPService.registerStringAction(new StringAction()
            {

				@Override
				public void onError(int channelId, String errorString, int error) 
				{
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onReceive(int channelId, byte[] data)
				{
					
				}

				@Override
				public void onServiceConnectionLost(int errorCode)
				{
					// TODO Auto-generated method stub
					
				}
            	
            });
            mSAPService.registerFileAction(new FileAction()
            {

				@Override
				public void onError(String errorMsg, int errorCode)
				{
					// TODO Auto-generated method stub
				}

				@Override
				public void onProgress(long progress)
				{
					// TODO Auto-generated method stub
				}
				@Override
				public void onTransferComplete(final String path) 
				{
					if(path.contains(DEST_DIR_SEND)){
						Protocol.getInstance().SendFile(path, myNumber);
					}
<<<<<<< HEAD
					if(path.contains(DEST_DIR_RECEIVE)){
						new File(path).delete();
					}
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
				}

				@Override
				public void onTransferRequested(int id, String path) 
				{
					String receiveDir = DEST_DIR_SEND+"/" + myNumber + System.currentTimeMillis()+".amr";
					mSAPService.receiveFile(id, receiveDir, true);
				}
            });
        }
    };
<<<<<<< HEAD
    

=======
      
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    public void popupNotification(){
    	NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
    	 
    	Notification.Builder mBuilder = new Notification.Builder(this);
    	mBuilder.setSmallIcon(R.drawable.ic_launcher);
    	mBuilder.setTicker("Now we start the cycling!");
    	mBuilder.setWhen(System.currentTimeMillis());
    	mBuilder.setNumber(10);
    	mBuilder.setContentTitle("Now we start the cycling!");
    	mBuilder.setContentText("You have been invited to the cycle group.");
    	mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
    	mBuilder.setContentIntent(pendingIntent);
    	mBuilder.setAutoCancel(true);
    	
    	mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
    	 
    	nm.notify(noti_num++, mBuilder.build());
    }
    
    
    public void startGetRequest(){
    	Log.d(TAG,"Sart getTask");
    	if(getTask==null){
    		getTask = new GetTask();	
    	}
    	if(mTimer==null){
    		mTimer = new Timer();
        	mTimer.scheduleAtFixedRate(getTask, new Date(),1000);	
    	}
    }
    
    public void stopGetRequest(){
    	Log.d(TAG,"Stop getTask");
     	mTimer.cancel();
    	mTimer = null;
    }
    
	public void startCruiseInfoRecord(){
<<<<<<< HEAD
		if(cruiseInfoTimerTask==null){
			cruiseInfoTimerTask = new CruiseInfoTimerTask();
		}
		if(cruiseInfoTimer==null){
			cruiseInfoTimer = new Timer();
			cruiseInfoTimer.scheduleAtFixedRate(cruiseInfoTimerTask, new Date(),5000);
		}
		
		if(distDiffMonitorTimerTask==null){
			distDiffMonitorTimerTask = new DistDiffMonitorTimerTask();
		}
		
		if(distDiffMonitorTimer==null){
			distDiffMonitorTimer = new Timer();
			distDiffMonitorTimer.scheduleAtFixedRate(distDiffMonitorTimerTask, new Date(), 20000);
		}
=======
		cruiseInfoTimerTask = new CruiseInfoTimerTask();
		cruiseInfoTimer = new Timer();
		cruiseInfoTimer.scheduleAtFixedRate(cruiseInfoTimerTask, new Date(),1000);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	}
	
	public void stopCruiseInfoRecord(){
		cruiseInfoTimerTask.cancel();
		cruiseInfoTimerTask = null;
		cruiseInfoTimer.cancel();
		cruiseInfoTimer = null;
		
<<<<<<< HEAD
		distDiffMonitorTimerTask.cancel();
		distDiffMonitorTimerTask = null;
		distDiffMonitorTimer.cancel();
		distDiffMonitorTimer = null;
		
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
	}
    
    public void open_button(View v){
    	layout.open_button(v);
    }
    
    public MainLayout getLayout(){
    	assert(layout!=null);
     	return layout;
    }
    
    public static MainActivity getInstasnce(){
    	return instance;
    }

	public SAPProviderService getmSAPService() {
		return mSAPService;
	}

	public String getMyNumber() {
		return myNumber;
	}

	public void setMyNumber(String myNumber) {
		this.myNumber = myNumber;
	}


}
