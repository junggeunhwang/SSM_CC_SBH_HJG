package com.ssm.cyclists.controller.communication.https;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ssm.cyclists.controller.activity.MainActivity;

import android.util.Log;
import android.widget.Toast;

public class Protocol {
	
	private static Protocol instance;
	
	private static String TAG = Protocol.class.getSimpleName();
	private static final String DEST_DIR  = "/storage/emulated/legacy/Cyclists";
	private static final String DEST_DIR_RECEIVE  = DEST_DIR+"/Receive";
	private static final String DEST_DIR_SEND  = DEST_DIR+ "/Send" ;
	
	private HttpsCommunicationCallback httpsCallback;
	
	private Protocol() {
		
		httpsCallback = new HttpsCommunicationCallback() {
			@Override
			public void onResponseSuccess(HttpsCommunication hcn) {
				
				if(hcn.getResponseOrder().equals("get")){
				
					//multicast data
					
					if(hcn.getResponseType().equals("string")){
						/* 서버  -> 안드로이드 문자열 수신
						 * 멀티캐스트 받은 데이터 
						 */
						
						
					}else if(hcn.getResponseType().equals("file")){
						
						/* 서버  -> 안드로이드 파일 수신*/
						
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
						
						MainActivity.getInstasnce().getmSAPService().sendFile(file.getAbsolutePath());
						
						if(!file.delete()){
							Log.e(TAG,"temporary voice file is not deleted");
						}
					}else if(hcn.getResponseType().equals("text")){
						/* 서버  -> 안드로이드 응답 수신 */
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
	
	public static Protocol getInstance(){
		if(instance==null)instance = new Protocol();
		return instance;
	}
	


    public boolean Login(String myNumber){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("login");
    	httpsCommunication.setUniqueNumber(myNumber);
    	
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "login requeset failed");
			return false;
		}
		Log.d(TAG, "login success");
		return true;
    }
    
    public boolean Logout(String myNumber){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("logout");
    	httpsCommunication.setUniqueNumber(myNumber);
    	
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "logout requeset failed");
			return false;
		}
		Log.d(TAG, "logut success");
		return true;
    }
    
    public boolean MakeRoom(String myNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("mkroom");
		httpsCommunication.setUniqueNumber(myNumber);
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "mkroom requeset failed");
			return false;
		}
		Log.d(TAG, "mkroom success");
		return true;
    }
    
    public boolean JoinRoom(String myNumber, String targetNumber){
       	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("joinroom");
    	httpsCommunication.setUniqueNumber(myNumber);
    	httpsCommunication.setExtraData(targetNumber);
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "JoinRoom requeset failed");
			return false;
		}
    	Log.d(TAG, "JoinRoom success");
    	return true;
    }
    
    public boolean ExitRoom(String myNumber){
       	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("exitroom");
    	httpsCommunication.setUniqueNumber(myNumber);
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "ExitRoom requeset failed");
			return false;
		}
    	Log.d(TAG, "ExitRoom success");
    	return true;
    }
    
    public boolean SendFile(String path,String myNumber){
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
    
    public boolean SendString(String data,String myNumber){
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

	public HttpsCommunicationCallback getHttpsCallback() {
		return httpsCallback;
	}

}
