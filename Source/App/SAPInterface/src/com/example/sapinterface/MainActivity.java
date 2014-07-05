package com.example.sapinterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sapInterface.total.SAPProviderService;
import com.sapInterface.total.StringAction;
import com.sapInterface.total.FileAction;
import com.sapInterface.total.SAPProviderService.LocalBinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity 
{
	 private static final String TAG = "SAPProvider";
	 private static final String DEST_PATH  = "/storage/emulated/legacy/test.amr";
	 private static final String SAVE_PATH  = "/storage/emulated/legacy/sample.amr";
	 private static final String TARGET_PATH = "/storage/emulated/legacy/testfromhost.amr";
	 private Context mCtxt;
	 private AlertDialog mAlert;
	 // 서비스 접근
	 private SAPProviderService mSAPService;
	 private String mFilePath;
	 public int mTransId;
	 
	 // UI
	 private Button SendStringButton;
	 private Button SendFileButton;
	 private Spinner phonenumberSetSpinner;
	 private Button SendStringToServerButton;
	 private Button SendFileToServerButton;
	 private Button RequestForGettingFromServerButton;
	 private Spinner RequestSetSpinner;
	 private Button SendTargetRequestButton;
	 
	 // To Server
	 String uniqueId;
	 String request;

	 private ServiceConnection mSAPConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "SAP service connection lost");
            mSAPService = null;
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            Log.d(TAG, "SAP service connected");
            mSAPService = ((LocalBinder) service).getService();
            
            // 문자열 관련 수신 액션
            mSAPService.registerStringAction(new StringAction(){

				@Override
				public void onError(int channelId, String errorString, int error) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onReceive(int channelId, byte[] data) {
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
				public void onServiceConnectionLost(int errorCode) {
					// TODO Auto-generated method stub
					
				}
            	
            });
            mSAPService.registerFileAction(new FileAction(){

				@Override
				public void onError(String errorMsg, int errorCode) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onProgress(long progress) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTransferComplete(String path) {
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
				public void onTransferRequested(int id, String path) {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // bind services
        mCtxt = getApplicationContext();
        mCtxt.bindService(new Intent(getApplicationContext(), SAPProviderService.class), 
                this.mSAPConnection, Context.BIND_AUTO_CREATE);
        
        // button event binding
        SendStringButton = (Button)findViewById(R.id.sendstring);
        SendFileButton = (Button)findViewById(R.id.sendfile);
        phonenumberSetSpinner = (Spinner)findViewById(R.id.phonenumberspinner);
        SendFileToServerButton = (Button)findViewById(R.id.sendfiletoserver);
        SendStringToServerButton = (Button)findViewById(R.id.sendstringtoserver);
        RequestForGettingFromServerButton = (Button)findViewById(R.id.requestget);
        RequestSetSpinner = (Spinner)findViewById(R.id.requestspinner);
        SendTargetRequestButton = (Button)findViewById(R.id.sendtargetorder);
        
        mylistener a = new mylistener();
        myspinnerlistener b =  new myspinnerlistener();
        
        SendStringButton.setOnClickListener(a);
        SendFileButton.setOnClickListener(a);
        SendFileToServerButton.setOnClickListener(a);
        SendStringToServerButton.setOnClickListener(a);
        RequestForGettingFromServerButton.setOnClickListener(a);
        SendTargetRequestButton.setOnClickListener(a);
        
        // set Spinner
        
        ArrayAdapter<CharSequence> requestadapter = ArrayAdapter.createFromResource(this, R.array.request_array, android.R.layout.simple_spinner_dropdown_item);
        
        requestadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        RequestSetSpinner.setAdapter(requestadapter);
        RequestSetSpinner.setOnItemSelectedListener(b);
        
        request = (String)requestadapter.getItem(0);
        
        // set spinner
        
        ArrayAdapter<CharSequence> phonenumberadapter = ArrayAdapter.createFromResource(this, R.array.phonenumber_array, android.R.layout.simple_spinner_dropdown_item);
        
        phonenumberadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        phonenumberSetSpinner.setAdapter(phonenumberadapter);
        phonenumberSetSpinner.setOnItemSelectedListener(b);
        
        uniqueId = (String)phonenumberadapter.getItem(0);
    }
    protected void onDestory()
    {
    	super.onDestroy();
    	Log.d(TAG, "onDestory called");
    	mSAPService.stopSelf();
    }
    private class myspinnerlistener implements OnItemSelectedListener
    {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			switch(arg0.getId())
			{
			case R.id.requestspinner:
				String a = (String) arg0.getItemAtPosition(arg2);
				Log.d(TAG,a);
				request = a;
				break;
			case R.id.phonenumberspinner:
				String b = (String) arg0.getItemAtPosition(arg2);
				Log.d(TAG,b);
				uniqueId = b;
				break;				
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    private class mylistener implements Button.OnClickListener
    {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.sendstring:
				{
					final String response = new String("I'm Host Device from SAPProvider App");
					mSAPService.SendByteData(response.getBytes());
				}
				break;
			case R.id.sendfile:
				{
					int tid = mSAPService.sendFile(TARGET_PATH);
					Log.d(TAG,"Sending Transaction Id " + String.valueOf(tid));
				}
				break;
			case R.id.sendfiletoserver:
				{
					HttpsCommunicationCallback callback = new HttpsCommunicationCallback()
					{
						
						@Override
						public void onResponseSuccess(HttpsCommunication hcn) {
							// TODO Auto-generated method stub
							final String resStr = "src unique Id : "+hcn.getResponseUniqueNumber()+"\r\n" +
									"Response : " + hcn.getStringResponseData();
									
									runOnUiThread(new Runnable() {
					                    @Override
					                    public void run() {
					                    	Toast.makeText(mCtxt, resStr, Toast.LENGTH_SHORT).show();
					                    }
					                });
						}
						
						@Override
						public void onResponseFailure(String errMsg) {
							// TODO Auto-generated method stub
							
						}
					};					
					HttpsCommunication hcn = new HttpsCommunication(callback);
					
					hcn.setUniqueNumber(uniqueId);
					hcn.setType(HttpsCommunication.TYPE_FILE);
					hcn.setFileData(new File(TARGET_PATH));
					if(hcn.ExecuteRequest() == true)
					{
						Log.d(TAG,"Sucess to File Request to server");
					}
				}
				break;
			case R.id.sendstringtoserver:
				{					
					HttpsCommunicationCallback callback = new HttpsCommunicationCallback()
					{
						
						@Override
						public void onResponseSuccess(HttpsCommunication hcn) {
							// TODO Auto-generated method stub
							final String resStr = "src unique Id : "+hcn.getResponseUniqueNumber()+"\r\n" +
									"Response : " + hcn.getStringResponseData();
									
									runOnUiThread(new Runnable() {
					                    @Override
					                    public void run() {
					                    	Toast.makeText(mCtxt, resStr, Toast.LENGTH_SHORT).show();
					                    }
					                });
						}
						
						@Override
						public void onResponseFailure(String errMsg) {
							// TODO Auto-generated method stub
							
						}
					};					
					HttpsCommunication hcn = new HttpsCommunication(callback);
					
					hcn.setUniqueNumber(uniqueId);
					hcn.setType(HttpsCommunication.TYPE_STRING);
					hcn.setStringData("askjdhkjqhwdkjhqkfjh");
					if(hcn.ExecuteRequest() == true)
					{
						Log.d(TAG,"Sucess to String Request to server");
					}
				}
				break;
			case R.id.requestget:
				{
					HttpsCommunicationCallback callback = new HttpsCommunicationCallback()
					{
						
						@Override
						public void onResponseSuccess(HttpsCommunication hcn) {
							// TODO Auto-generated method stub
							final String resStr = "src unique Id : "+hcn.getResponseUniqueNumber()+"\r\n" +
									"src type : " + hcn.getResponseType() + "\r\n"; 
									
							if(hcn.getResponseType().equals("file"))
							{
								File downloadedfile = new File(SAVE_PATH);
								if(downloadedfile.exists() == true)
								{
									downloadedfile.delete();
								}
								
								try {
									downloadedfile.createNewFile();
									FileOutputStream wc = new FileOutputStream(downloadedfile);
									
									wc.write(hcn.getByteResponseData());
									wc.close();
									
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								final String textResponse = resStr + "file download complete";
								
								runOnUiThread(new Runnable() {
					                   @Override
					                   public void run() {
					                   	Toast.makeText(mCtxt, textResponse, Toast.LENGTH_SHORT).show();
					                   }
					             });
							}
							else
							{
								final String textResponse = resStr + "Response : " + hcn.getStringResponseData();
								
								runOnUiThread(new Runnable() {
					                   @Override
					                   public void run() {
					                   	Toast.makeText(mCtxt, textResponse, Toast.LENGTH_SHORT).show();
					                   }
					             });
							}
						}
						
						@Override
						public void onResponseFailure(String errMsg) {
							// TODO Auto-generated method stub
							
						}
					};					
					HttpsCommunication hcn = new HttpsCommunication(callback);
					
					hcn.setUniqueNumber(uniqueId);
					hcn.setType(HttpsCommunication.TYPE_REQUEST);
					hcn.setStringData("get");
					if(hcn.ExecuteRequest() == true)
					{
						Log.d(TAG,"Sucess to get Request to server");
					}
				}
				break;
			case R.id.sendtargetorder:
				{
					HttpsCommunicationCallback callback = new HttpsCommunicationCallback()
					{
						
						@Override
						public void onResponseSuccess(HttpsCommunication hcn) {
							// TODO Auto-generated method stub
							
							Log.d(TAG,hcn.getStringResponseData());
							
							final String resStr = "src unique Id : "+hcn.getResponseUniqueNumber()+"\r\n" +
							"Response : " + hcn.getStringResponseData();
							
							runOnUiThread(new Runnable() {
			                    @Override
			                    public void run() {
			                    	Toast.makeText(mCtxt, resStr, Toast.LENGTH_SHORT).show();
			                    }
			                });
							
						}
						
						@Override
						public void onResponseFailure(String errMsg) {
							// TODO Auto-generated method stub
							
						}
					};					
					HttpsCommunication hcn = new HttpsCommunication(callback);
					
					hcn.setUniqueNumber(uniqueId);
					hcn.setType(HttpsCommunication.TYPE_REQUEST);
					request = request.substring(request.indexOf('-')+1);
					if(request.equals("joinroom"))
					{
						hcn.setExtraData("01012345678");
					}
					hcn.setStringData(request);
					if(hcn.ExecuteRequest() == true)
					{
						Log.d(TAG,"Sucess to "+request + "Request to server");
					}
				}
				break;
			}
		}
    	
    }
}
