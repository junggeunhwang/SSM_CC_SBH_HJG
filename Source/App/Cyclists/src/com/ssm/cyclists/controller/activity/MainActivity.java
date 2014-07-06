package com.ssm.cyclists.controller.activity;


import twitter4j.GeoLocation;
import net.simonvt.menudrawer.MenuDrawer;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.sapInterface.total.SAPProviderService;
import com.sapInterface.total.StringAction;
import com.sapInterface.total.FileAction;
import com.sapInterface.total.SAPProviderService.LocalBinder;
import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.DataBaseManager;
import com.ssm.cyclists.controller.FacebookManager;
import com.ssm.cyclists.controller.TwitterManager;
import com.ssm.cyclists.controller.fragment.CycleTrackerDetailFragment;
import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.model.GoogleLocationManager;
import com.ssm.cyclists.model.TwitterBasicInfo;

import com.ssm.cyclists.view.layout.MainLayout;

import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context; 
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private static MainActivity instance;
	
	private static final String TAG = "SAPProvider";
	private static final String DEST_PATH  = "/storage/emulated/legacy/test.amr";
	private static final String TARGET_PATH = "/storage/emulated/legacy/testfromhost.amr";
	
	private GoogleLocationManager googleLocationManager;
	
	private Context mCtxt;
	private AlertDialog mAlert;
	// 서비스 접근
	private SAPProviderService mSAPService;
	private String mFilePath;
	 
	public int mTransId;
	
	private MainLayout layout;
	
	
	public MainActivity() {
		this.instance = this;
		
	}
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	this.instance = this;
    	
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	Intent intent = new Intent(this,SplashActivity.class);
    	startActivity(intent);
    	googleLocationManager = new GoogleLocationManager();
    	googleLocationManager.init(this);
    	//위치 서비스

//    	googleLocationManager.resume();
    	
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
    }
       
    @Override
    protected void onStart() {
    	this.instance = this;
   	 	layout.getmFragmentHome().updateHomeInfo();
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
				public void onTransferComplete(String path) 
				{
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	if(mAlert != null)
	                    	{
	                    		mAlert.dismiss();
	                    	}
	                        Toast.makeText(getBaseContext(), "receive Completed!", Toast.LENGTH_SHORT).show();
	                    }
	                });
				}

				@Override
				public void onTransferRequested(int id, String path) 
				{
					// TODO Auto-generated method stub
					mFilePath = path;
					mTransId = id;

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
							alertbox.setMessage("receive request: " + mFilePath);
							alertbox.setPositiveButton("Accept",
									new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									mAlert.dismiss();
									try {
										mSAPService.receiveFile(mTransId, DEST_PATH, true);
										Log.i(TAG, "sending accepted");
									} catch (Exception e) {
										e.printStackTrace();
										Toast.makeText(mCtxt, "IllegalArgumentException", Toast.LENGTH_SHORT).show();
									}
								}
							});

							alertbox.setNegativeButton("Reject",
									new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) 
								{
									mAlert.dismiss();

									try {
										mSAPService.receiveFile(mTransId, DEST_PATH, false);
										Log.i(TAG, "sending rejected");
									} catch (Exception e) {
										e.printStackTrace();
										Toast.makeText(mCtxt, "IllegalArgumentException", Toast.LENGTH_SHORT).show();
									}
								}
							});

							alertbox.setCancelable(false);
							mAlert = alertbox.create();
							mAlert.show();
						}
					});
				}

            });
        }
    };
    
    public void open_button(View v){
    	layout.open_button(v);
    }

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
    
    public MainLayout getLayout(){
    	assert(layout!=null);
     	return layout;
    }
    
    public static MainActivity getInstasnce(){
    	return instance;
    }

}
