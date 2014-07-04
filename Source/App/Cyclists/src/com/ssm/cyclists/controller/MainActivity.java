package com.ssm.cyclists.controller;


import com.sapInterface.total.SAPProviderService;
import com.sapInterface.total.StringAction;
import com.sapInterface.total.FileAction;
import com.sapInterface.total.SAPProviderService.LocalBinder;
import com.ssm.cyclists.R;
import com.ssm.cyclists.model.CruiseDataManager;
import com.ssm.cyclists.view.layout.MainLayout;

import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context; 
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity{

	private static MainActivity instance;
	
	private static final String TAG = "SAPProvider";
	private static final String DEST_PATH  = "/storage/emulated/legacy/test.amr";
	private static final String TARGET_PATH = "/storage/emulated/legacy/testfromhost.amr";
	    
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
    	
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	Intent intent = new Intent(this,SplashActivity.class);
    	startActivity(intent);
    	
    	super.onCreate(savedInstanceState);
    	
    	layout = new MainLayout(this);
    	
    	setContentView(layout.getView());
    	
        layout.init();
        
        
        
        // bind services
        mCtxt = getApplicationContext();
        mCtxt.bindService(new Intent(getApplicationContext(), SAPProviderService.class), 
                this.mSAPConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStart() {
   	 	layout.getmFragmentHome().updateHomeInfo();
    	super.onStart();
    }

	public LocationListener buildLocationChangediListener(){
		return new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG,"altitude : " + location.getAltitude());
				
				CruiseDataManager.getInstance().setCurrent_speed(location.getSpeed());
				CruiseDataManager.getInstance().setCurrent_loc(location.getLatitude(),location.getLongitude()); 
				CruiseDataManager.getInstance().updateCruiseData();
				DataBaseManager.getInstance().updateLastLocation(location);				
				if(layout.getmMapViewFragment().isVisible()){
					layout.getmMapViewFragment().moveMapCamenra(location);
				}
				if(layout.getmFragmentCruise().isVisible()){
					layout.getmFragmentCruise().updateCruiseInfo();
				}
			}
		};
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
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        layout.onRestoreInstanceState(inState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        layout.onSaveInstanceState(outState);
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
        layout.onBackBtnPressed();
        super.onBackPressed();
    }
    
    public MainLayout getLayout(){
    	assert(layout!=null);
     	return layout;
    }
    
    public static MainActivity getInstasnce(){
    	return instance;
    }



}
