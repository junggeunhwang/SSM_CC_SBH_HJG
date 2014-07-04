package com.example.sapinterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	 private static final String TAG = "SAPProvider";
	 private static final String DEST_PATH  = "/storage/emulated/legacy/test.amr";
	 private static final String SAVE_PATH  = "/storage/emulated/legacy/sample.amr";
	 private static final String TARGET_PATH = "/storage/emulated/legacy/testfromhost.amr";
	 private final String urlString = "https://moonlightchaser.mooo.com";
	 private Context mCtxt;
	 private AlertDialog mAlert;
	 private HttpClient httpClient;
	 // 서비스 접근
	 private SAPProviderService mSAPService;
	 private String mFilePath;
	 
	 public int mTransId;
	 
	 // UI
	 private Button SendStringButton;
	 private Button SendFileButton;
	 private Button SendStringToServerButton;
	 private Button SendFileToServerButton;
	 private Button RequestForGettingFromServerButton;

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
        SendFileToServerButton = (Button)findViewById(R.id.sendfiletoserver);
        SendStringToServerButton = (Button)findViewById(R.id.sendstringtoserver);
        RequestForGettingFromServerButton = (Button)findViewById(R.id.requestget);
        
        
        mylistener a = new mylistener();
        
        SendStringButton.setOnClickListener(a);
        SendFileButton.setOnClickListener(a);
        SendFileToServerButton.setOnClickListener(a);
        SendStringToServerButton.setOnClickListener(a);
        RequestForGettingFromServerButton.setOnClickListener(a);
        
        // set http client
        httpClient = getHttpClient();
    }
    protected void onDestory()
    {
    	super.onDestroy();
    	Log.d(TAG, "onDestory called");
    	mSAPService.stopSelf();
    }
    private HttpClient getHttpClient() 
    {
        try 
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), SFSSLSocketFactory.HTTP_PORT));
            registry.register(new Scheme("https", sf, SFSSLSocketFactory.HTTPS_PORT));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
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
					Thread thread = new Thread() {
	                    @Override
	                    public void run() {
	                        try 
	                        {
	                            URI url = new URI(urlString);
	                            HttpPost httpPost = new HttpPost();
	                            httpPost.setURI(url);

	                            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	                            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	                            
	                            builder.addTextBody("uniqueNumber", "01012345678");
	                            // file, string, request - (mkroom,delroom,joinroom,exitroom,login,logout,get)
	                            builder.addTextBody("type","file");
	                            builder.addBinaryBody("data", new File(TARGET_PATH));
	                            
	                            httpPost.setEntity(builder.build());	                            

	                            HttpResponse response = httpClient.execute(httpPost);
	                            
	                            String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

	                            Log.d(TAG, responseString);

	                        } catch (URISyntaxException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        } catch (ClientProtocolException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        } catch (IOException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        }

	                    }
	                };


	                thread.start();
				}
				break;
			case R.id.sendstringtoserver:
				{
					Thread thread = new Thread() {
	                    @Override
	                    public void run() {
	                        
	                    	try 
	                        {
	                            URI url = new URI(urlString);

	                            HttpPost httpPost = new HttpPost();
	                            httpPost.setURI(url);

	                            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	                            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	                            
	                            builder.addTextBody("uniqueNumber", "01012345678");
	                            // file, string, request - (mkroom,delroom,joinroom,exitroom,login,logout,get)
	                            builder.addTextBody("type","string");
	                            builder.addTextBody("data", "asdjhjkhfkjshdkjg");
	                            
	                            httpPost.setEntity(builder.build());
	                            

	                            HttpResponse response = httpClient.execute(httpPost);
	                          
	                            Header type,src;
	                            type = response.getFirstHeader("type");
	                            src = response.getFirstHeader("src");
	                          
	                            Log.d(TAG,type.getValue());
	                            Log.d(TAG,src.getValue());
	                            
	                                                                     
	                            String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

	                            Log.d(TAG, responseString);

	                        } catch (URISyntaxException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        } catch (ClientProtocolException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        } catch (IOException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        }

	                    }
	                };


	                thread.start();
				}
				break;
			case R.id.requestget:
				{
					Thread thread = new Thread() {
	                    @Override
	                    public void run() {
	                        
	                    	try 
	                        {
	                            URI url = new URI(urlString);

	                            HttpPost httpPost = new HttpPost();
	                            httpPost.setURI(url);

	                            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	                            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	                            
	                            builder.addTextBody("uniqueNumber", "01012345678");
	                            // file, string, request - (mkroom,delroom,joinroom,exitroom,login,logout,get)
	                            builder.addTextBody("type","request");
	                            builder.addTextBody("data", "get");
	                            
	                            httpPost.setEntity(builder.build());
	                            

	                            HttpResponse response = httpClient.execute(httpPost);
	                          
	                            Header type,src;
	                            type = response.getFirstHeader("type");
	                            src = response.getFirstHeader("src");
	                          
	                            Log.d(TAG,type.getValue());
	                            Log.d(TAG,src.getValue());
	                            
	                            
	                                                                     
	                            byte[] filedata = EntityUtils.toByteArray(response.getEntity());
	                            
	                            File targetfile = new File(SAVE_PATH);
	                            Boolean flag = targetfile.createNewFile();
	                            
	                            Log.d(TAG,flag.toString());
	                            
	                            FileOutputStream ws = new FileOutputStream(targetfile);
	                            
	                            ws.write(filedata);
	                            
	                            ws.close();        
	                        } catch (URISyntaxException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        } catch (ClientProtocolException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        } catch (IOException e) {
	                            Log.e(TAG, e.getLocalizedMessage());
	                            e.printStackTrace();
	                        }

	                    }
	                };


	                thread.start();
				}
				break;
			}
		}
    	
    }
}
