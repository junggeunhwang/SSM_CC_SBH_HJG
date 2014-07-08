package com.ssm.cyclists.controller.activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Date;
import java.util.Timer;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import net.simonvt.menudrawer.MenuDrawer;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.DataBaseManager;
import com.ssm.cyclists.controller.FacebookManager;
import com.ssm.cyclists.controller.GetTimer;
import com.ssm.cyclists.controller.TwitterManager;
import com.ssm.cyclists.controller.communication.https.HttpsCommunication;
import com.ssm.cyclists.controller.communication.https.HttpsCommunicationCallback;
import com.ssm.cyclists.controller.communication.https.SFSSLSocketFactory;
import com.ssm.cyclists.controller.communication.sapinterface.FileAction;
import com.ssm.cyclists.controller.communication.sapinterface.SAPProviderService;
import com.ssm.cyclists.controller.communication.sapinterface.StringAction;
import com.ssm.cyclists.controller.communication.sapinterface.SAPProviderService.LocalBinder;
import com.ssm.cyclists.controller.fragment.CycleTrackerDetailFragment;
import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.model.GoogleLocationManager;
import com.ssm.cyclists.model.TwitterBasicInfo;
import com.ssm.cyclists.view.layout.MainLayout;

import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context; 
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	//my instance
	private static MainActivity instance;
	
	private static final String TAG = "SAPProvider";
	private static final String DEST_DIR  = "/storage/emulated/legacy/Cyclists";
	private static final String DEST_DIR_RECEIVE  = DEST_DIR+"/Receive";
	private static final String DEST_DIR_SEND  = DEST_DIR+ "/Send" ;
			
	
	//gps
	private GoogleLocationManager googleLocationManager;
	
	private Context mCtxt;
	private AlertDialog mAlert;
	// ���� ����
	private SAPProviderService mSAPService;
	private String mFilePath;
	 
	public int mTransId;
	
	private Timer timer;
	
	//��ȭ��ȣ
	TelephonyManager mTelephonyManager;
	String myNumber;
	
	//https

	HttpsCommunicationCallback httpsCallback;
	
	//layout
	private MainLayout layout;
	
	//�׸�
	String theme_color;
	
	public MainActivity() {
		this.instance = this;
		
	}
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	this.instance = this;
    	
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
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
    	
    	
    	//�׸� ���� ����
    	theme_color = DataBaseManager.getInstance().selectSettingInfo();
    	if(theme_color==null)theme_color="gray";
    	Intent intent = new Intent(this,SplashActivity.class);
    	intent.putExtra("color", theme_color);
    	
    	startActivity(intent);
    	
    	//��ȭ��ȣ
    	mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    	myNumber = mTelephonyManager.getLine1Number();
//    	myNumber = "01098765432";l
    	myNumber = "01012345678";
    	
    	Log.d(TAG,"my number : "+myNumber);
    	
    	
    	GetTimer getTimer = new GetTimer();
    	
    	timer = new Timer();
    	timer.scheduleAtFixedRate(getTimer, new Date(),1000);
    	
    	
    	login();
    	
		
    	//��ġ ����
    	googleLocationManager = new GoogleLocationManager();
    	googleLocationManager.init(this);

    	CruiseDataManager.getInstance().updateCruiseData();
    	
    	super.onCreate(savedInstanceState);
    	
    	layout = new MainLayout(this);
    	
    	setContentView(layout.getView());
    	
        layout.init();
        FacebookManager.getInstance().init(savedInstanceState);
   
        // bind services
        mCtxt = getApplicationContext();
        mCtxt.bindService(new Intent(getApplicationContext(), SAPProviderService.class), 
                this.mSAPConnection, Context.BIND_AUTO_CREATE);
        
        //https
        httpsCallback = new HttpsCommunicationCallback() {
			
			@Override
			public void onResponseSuccess(HttpsCommunication hcn) {
				
				if(hcn.getResponseOrder().equals("get")){
				
					//multicast data
					
					if(hcn.getResponseType().equals("string")){
						/* ����  -> �ȵ���̵� ���ڿ� ����
						 * ��Ƽĳ��Ʈ ���� ������ 
						 */
						
						
					}else if(hcn.getResponseType().equals("file")){
						
						/* ����  -> �ȵ���̵� ���� ����*/
						
						
						String fileName = hcn.getResponseUniqueNumber()+System.currentTimeMillis()+".amr";
						File file = new File(DEST_DIR_RECEIVE + "/" + fileName);
						
						try {
							FileOutputStream fileOutputStream = new FileOutputStream(file);
							fileOutputStream.write(hcn.getByteResponseData());
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						
						mSAPService.sendFile(file.getAbsolutePath());
						Toast.makeText(getApplicationContext(), "receive file", Toast.LENGTH_SHORT);
						
					}else if(hcn.getResponseType().equals("text")){
						/* ����  -> �ȵ���̵� ���� ���� */
					}
				}else if(hcn.getResponseOrder().equals("login")){
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"Login Success");
					else if(hcn.getStringResponseData().equals("EALREADYLOGIN")) Log.e(TAG,"Login Fail : EALREADYLOGIN");
				}else if(hcn.getResponseOrder().equals("logout")){
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"Logout Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"Login Fail : ENOTLOGIN");
				}else if(hcn.getResponseOrder().equals("mkroom")){
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"MakeRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"MakeRoom Fail : ENOTLOGIN");
				}else if(hcn.getResponseOrder().equals("joinroom")){
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"JoinRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"JoinRoom Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("EALREADYJOINROOM")) Log.e(TAG,"JoinRoom Fail : EALREADYJOINROOM");
					else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"JoinRoom Fail : ENOTARGET");
				}else if(hcn.getResponseOrder().equals("exitroom")){
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"ExitRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"ExitRoom Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("ENOTJOINROOM")) Log.e(TAG,"ExitRoom Fail : ENOTJOINROOM");	
				}
			}
			
			@Override
			public void onResponseFailure(String errMsg) {
				Log.e(TAG,"https : response failure");
			}
		};
		
    }
       
    
    
    @Override
    protected void onStart() {
    	this.instance = this;
   	 	layout.getmFragmentHome().updateHomeInfo();
   	 	change_color(theme_color);
   	 	layout.getmFragmentHome().getLayout().updateColor();
    	super.onStart();
    	FacebookManager.getInstance().start();
    	googleLocationManager.resume();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	FacebookManager.getInstance().stop();
    	googleLocationManager.pause();
    }
    
    @Override
    protected void onPause() {
    	
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }
    
    
    private ServiceConnection mSAPConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "SAP service connection lost");
            mSAPService = null;
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) 
        {
            Log.d(TAG, "SAP service connected");
            mSAPService = ((LocalBinder) service).getService();
            
            // ���ڿ� ���� ���� �׼�
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
					// TODO Auto-generated method stub
					final String message = new String(data,0,data.length);
					 runOnUiThread(new Runnable() 
					 {
		                    @Override
		                    public void run() {
		                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		                    }
		             });
					 
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
					if(path.contains(DEST_DIR_SEND))
						SendFile(path);
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
      
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               layout.toggle_menu();
               return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    	final int drawerState = layout.getmMenuDrawer().getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
        	layout.getmMenuDrawer().closeMenu();
            return;
        }
        
        if(layout.getActivated_fragment().getClass().equals(CycleTrackerDetailFragment.class))
        	((CycleTrackerDetailFragment)layout.getActivated_fragment()).getLayout().backScreen();
        else if(layout.getActivated_fragment().equals(layout.getmFragmentHome()))
        	super.onBackPressed();
        else
        	layout.replaceFragment(R.layout.fragment_home);
    }
    
    public boolean login(){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("login");
    	httpsCommunication.setUniqueNumber(myNumber);
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "login requeset failed");
			return false;
		}
		return true;
    }
    
    public boolean logout(){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("logout");
    	httpsCommunication.setUniqueNumber(myNumber);
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "logout requeset failed");
			return false;
		}
		return true;
    }
    
    public boolean MakeRoom(){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("mkroom");
		httpsCommunication.setUniqueNumber(myNumber);
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "mkroom requeset failed");
			return false;
		}
		return true;
    }
    
    public boolean JoinRoom(String targetNumber){
       	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("joinroom");
    	httpsCommunication.setUniqueNumber(myNumber);
    	httpsCommunication.setExtraData(targetNumber);
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "JoinRoom requeset failed");
			return false;
		}
    	return true;
    }
    
    public boolean ExitRoom(){
       	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("exitroom");
    	httpsCommunication.setUniqueNumber(myNumber);
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "ExitRoom requeset failed");
			return false;
		}
    	return true;
    }
    
    public boolean SendFile(String path){
        HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
        httpsCommunication.setType(HttpsCommunication.TYPE_FILE);
        httpsCommunication.setUniqueNumber(myNumber);
        httpsCommunication.setFileData(new File(path));
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "SendFile requeset failed");
			return false;
		}
    	return true;
    }
    
    public boolean SendString(String data){
        HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
        httpsCommunication.setType(HttpsCommunication.TYPE_STRING);
        httpsCommunication.setUniqueNumber(myNumber);
        httpsCommunication.setStringData(data);
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "SendString requeset failed");
			return false;
		}
    	return true;
    }
    
    public void open_button(View v){
    	layout.open_button(v);
    }
    
    public void change_color(String color){
    	
    	if(color.equals("pink")){
    		layout.getmFragmentHome().getLayout().setTheme_color("pink");
    		layout.getmFragmentCruise().getLayout().setTheme_color("pink");
    		layout.getmFragmentCycleMate().getLayout().setTheme_color("pink");
    		layout.getmFragmentCycleTracker().getLayout().setTheme_color("pink");
    		layout.getmMapViewFragment().getLayout().setTheme_color("pink");
    		layout.getmSettingsFragment().getLayout().setTheme_color("pink");
    	}
    	else if(color.equals("green")){
    		layout.getmFragmentHome().getLayout().setTheme_color("green");
    		layout.getmFragmentCruise().getLayout().setTheme_color("green");
    		layout.getmFragmentCycleMate().getLayout().setTheme_color("green");
    		layout.getmFragmentCycleTracker().getLayout().setTheme_color("green");
    		layout.getmMapViewFragment().getLayout().setTheme_color("green");
    		layout.getmSettingsFragment().getLayout().setTheme_color("green");
    	}else if(color.equals("gray")){
    		layout.getmFragmentHome().getLayout().setTheme_color("gray");
    		layout.getmFragmentCruise().getLayout().setTheme_color("gray");
    		layout.getmFragmentCycleMate().getLayout().setTheme_color("gray");
    		layout.getmFragmentCycleTracker().getLayout().setTheme_color("gray");
    		layout.getmMapViewFragment().getLayout().setTheme_color("gray");
    		layout.getmSettingsFragment().getLayout().setTheme_color("gray");
    	}
    	
    }
    
    public MainLayout getLayout(){
    	assert(layout!=null);
     	return layout;
    }
    
    public static MainActivity getInstasnce(){
    	return instance;
    }

	public HttpsCommunicationCallback getHttpsCallback() {
		return httpsCallback;
	}

	public String getMyNumber() {
		return myNumber;
	}

	public void setMyNumber(String myNumber) {
		this.myNumber = myNumber;
	}


}