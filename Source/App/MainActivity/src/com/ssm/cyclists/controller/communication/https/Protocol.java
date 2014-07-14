package com.ssm.cyclists.controller.communication.https;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.manager.SettingsDataManager;
import com.ssm.cyclists.model.UserData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

public class Protocol {
	
	private static Protocol instance;
	
	private static String TAG = Protocol.class.getSimpleName();
	
	
	private static final String DEST_DIR  = "/storage/emulated/legacy/Cyclists";
	private static final String DEST_DIR_RECEIVE  = DEST_DIR+"/Receive";
	private static final String DEST_DIR_SEND  = DEST_DIR+ "/Send" ;
	private static final String DEST_DIR_PROFILE  = DEST_DIR+ "/Profile" ;
	
	private HttpsCommunicationCallback httpsCallback;
	
	private Protocol() {
		
		httpsCallback = new HttpsCommunicationCallback() {
			@Override
			public void onResponseSuccess(HttpsCommunication hcn) {
				
				if(hcn.getResponseOrder().equals("get")){
				
					if(hcn.getResponseType().equals("string")){
						/*Multicast Data*/
						String receive = hcn.getStringResponseData();
						String senderUniqueID;
						
						StringTokenizer tokenizer = new StringTokenizer(receive,",");
						String token = tokenizer.nextToken();
						
						Location loc = new Location("gps");
						
						if(token.equals("location")){
							Log.d(TAG,"locationBroadcast received");
							senderUniqueID = tokenizer.nextToken();
							loc.setLatitude(Double.valueOf(tokenizer.nextToken()));
							loc.setLongitude(Double.valueOf(tokenizer.nextToken()));
							
							ArrayList<UserData> friendList = SettingsDataManager.getInstance().getFriendList();
							
							for(int i = 0 ; i < friendList.size() ; i++){
								if(friendList.get(i).getUniqueID().equals(senderUniqueID)){
									friendList.get(i).setCurrentLocation(loc);
								}
							}
						}else if (token.equals("roommember")){
							Log.d(TAG,"room member Broadcast received");
							ArrayList<UserData> roomMemberList = new ArrayList<UserData>();
							ArrayList<UserData> friendList = SettingsDataManager.getInstance().getFriendList();
							
							while((token = tokenizer.nextToken())!=null){
								for(int i = 0 ; i < friendList.size() ; i++){
									if(friendList.get(i).getUniqueID().equals(token)){
										roomMemberList.add(friendList.get(i));
									}
								}
							}
							SettingsDataManager.getInstance().setCurrentRoomFriendList(roomMemberList);
						}
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
						
						MainActivity.getInstasnce().getmSAPService().sendFile(file.getAbsolutePath());
						
//						if(!file.delete()){
//							Log.e(TAG,"temporary voice file is not deleted");
//						}
					}else if(hcn.getResponseType().equals("text")){
						/* ����  -> �ȵ���̵� ���� ���� */
						
					}
				}else if(hcn.getResponseOrder().equals("login")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.i(TAG,"Login Success");
					else if(hcn.getStringResponseData().equals("EALREADYLOGIN")) Log.e(TAG,"Login Fail : EALREADYLOGIN");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Login Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("logout")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.i(TAG,"Logout Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"logout Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"logout Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("mkroom")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.i(TAG,"MakeRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"MakeRoom Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"MakeRoom Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("joinroom")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.i(TAG,"JoinRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"JoinRoom Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("EALREADYJOINROOM")) Log.e(TAG,"JoinRoom Fail : EALREADYJOINROOM");
					else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"JoinRoom Fail : ENOTARGET");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"JoinRoom Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("exitroom")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"ExitRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"ExitRoom Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("ENOTJOINROOM")) Log.e(TAG,"ExitRoom Fail : ENOTJOINROOM");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"ExitRoom Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("addfriend")){
					
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"AddFriend Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"AddFriend Fail : ENOTTARGET");
					else if(hcn.getStringResponseData().equals("EALREADYFRIEND")) Log.e(TAG,"AddFriend Fail : EALREADYFRIEND");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"AddFriend Fail : EMISSING");
					else if(hcn.getStringResponseData().equals("SUCCESS")){
						Toast.makeText(MainActivity.getInstasnce().getApplicationContext(),"Add friend success",Toast.LENGTH_SHORT).show();
						Log.d(TAG,"AddFriend Success");
					}
					
				}else if(hcn.getResponseOrder().equals("delfriend")){
					
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"Delete Friend Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("ENOTTARGET")) Log.e(TAG,"Delete Friend Fail : ENOTTARGET");
					else if(hcn.getStringResponseData().equals("ENOTFRIEND")) Log.e(TAG,"Delete Friend Fail : ENOTFRIEND");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Delete Friend Fail : EMISSING");
					else if(hcn.getStringResponseData().equals("SUCCESS")){
						Log.d(TAG,"Delete Friend Success");
					}
					
				}else if(hcn.getResponseOrder().equals("friendlist")){
					
					if(hcn.getResponseType().equals("TEXT")){
						if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"Get FriendList Fail : ENOTLOGIN");
						else if(hcn.getStringResponseData().equals("ENOLIST")) Log.e(TAG,"Get FriendList Fail : ENOLIST");
						else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Get FriendList Fail : EMISSING");	
					}
					else if(hcn.getResponseType().equals("STRING")){
						Log.i(TAG,"get FriendList Success");
						String friendlist = hcn.getStringResponseData();
						
						StringTokenizer tokenizer = new StringTokenizer(friendlist,";");
						
						ArrayList<UserData> friendsList = new ArrayList<UserData>();
						
						String UniqueID;
						while((UniqueID = tokenizer.nextToken())!=null){
							UserData friend = new UserData();
							friend.setUniqueID(UniqueID);
							GetName(SettingsDataManager.getInstance().getMe().getUniqueID(), UniqueID);
							GetImage(SettingsDataManager.getInstance().getMe().getUniqueID(), UniqueID);
							friendsList.add(friend);
						}
						SettingsDataManager.getInstance().setFriendList(friendsList);
					}
					
				}else if(hcn.getResponseOrder().equals("getname")){
					
					if(hcn.getResponseType().equals("text")){
						if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"GetName Fail : ENOTLOGIN");
						else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"GetName Fail : ENOTARGET");
						else if(hcn.getStringResponseData().equals("ENONAME")) Log.e(TAG,"GetName Fail : ENONAME");
						else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"GetName Fail : EMISSING");
					}
					else if(hcn.getResponseType().equals("string")){
						String name = hcn.getStringResponseData();
						String targetNumber = hcn.getResponseUniqueNumber();
						ArrayList<UserData> friendList = SettingsDataManager.getInstance().getFriendList();
						
						for(int i = 0;i<friendList.size();i++){
							if(friendList.get(i).getUniqueID().equals(targetNumber)){
								friendList.get(i).setUserName(name);
							}
						}
						Log.i(TAG,"GetName Success : " + name);
					}
					
				}else if(hcn.getResponseOrder().equals("getimage")){
					
					if(hcn.getResponseType().equals("text")){
						if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"getimage Fail : ENOTLOGIN");
						else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"getimage Fail : ENOTARGET");
						else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"getimage Fail : ENONAME");
						else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"getimage Fail : EMISSING");
					}
					else if(hcn.getResponseType().equals("file")){
						//row profile data 
						byte[] profile = hcn.getByteResponseData();
						//convert byte[] -> Bitmap
						Bitmap profile_bitmap = BitmapFactory.decodeByteArray(profile, 0, profile.length);
												
						String targetNumber = hcn.getResponseUniqueNumber();
						ArrayList<UserData> friendList = SettingsDataManager.getInstance().getFriendList();
						
						for(int i = 0;i<friendList.size();i++){
							if(friendList.get(i).getUniqueID().equals(targetNumber)){
								friendList.get(i).setProfileImg(profile_bitmap);
							}
						}
						Log.i(TAG,"GetFile Success");
						
					}
					
				}else if(hcn.getResponseOrder().equals("inviteroom")){
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"inviteroom Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("ENOTJOINROOM")) Log.e(TAG,"inviteroom Fail : ENOTJOINROOM");
					else if(hcn.getStringResponseData().equals("ETARGETNOTLOGIN")) Log.e(TAG,"inviteroom Fail : ETARGETNOTLOGIN");
					else if(hcn.getStringResponseData().equals("ETARGETALREADYJOINROOM")) Log.e(TAG,"inviteroom Fail : ETARGETALREADYJOINROOM");
					else if(hcn.getStringResponseData().equals("SUCCESS")) Log.e(TAG,"inviteroom SUCCESS");
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
	

	public boolean InviteFriend(String myNumber,String targetNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setStringData("inviteroom");
		httpsCommunication.setExtraData(targetNumber);
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "invite friend requeset failed");
			return false;
		}
		Log.d(TAG, "invite friend request success : " + targetNumber);
		return true;
	}
	
	public boolean AddFriendRequest(String myNumber,String targetNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setStringData("addfriend");
		httpsCommunication.setExtraData(targetNumber);
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "addfriend requeset failed");
			return false;
		}
		Log.d(TAG, "addfriend request success : " + targetNumber);
		return true;
	}
	
	public boolean DeleteFriendRequest(String myNumber,String targetNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setStringData("delfriend");
		httpsCommunication.setExtraData(targetNumber);
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "delfriend requeset failed");
			return false;
		}
		Log.d(TAG, "delfriend request success : " + targetNumber);
		return true;
	}
	
	public boolean FriendsListRequest(String myNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("friendlist");
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "friendlist requeset failed");
			return false;
		}
		Log.d(TAG, "friendlist request success");
		return true;
	}

	public boolean GetName(String myNumber,String targetNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("getname");
		httpsCommunication.setExtraData(targetNumber);
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "getname requeset failed");
			return false;
		}
		Log.d(TAG, "getname request success : " + targetNumber);
		return true;
	}

	public boolean GetImage(String myNumber,String targetNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("getimage");
		httpsCommunication.setExtraData(targetNumber);
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "getimage requeset failed");
			return false;
		}
		Log.d(TAG, "getimage request success : " + targetNumber);
		return true;
	}
	
    public boolean Login(String myNumber){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("login");
    	httpsCommunication.setUniqueNumber(myNumber);
    	UserData me = SettingsDataManager.getInstance().getMe();
    	if(me.getUserName()!=null){
    		httpsCommunication.setExtraData(me.getUserName());
    	}
    	if(me.getProfileImg()!=null){
    		File myProfile = new File(DEST_DIR_PROFILE+"/me.png");
    		httpsCommunication.setFileData(myProfile);
    	}
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "login requeset failed");
			return false;
		}
		Log.d(TAG, "login request success");
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
		Log.d(TAG, "logut request success");
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
    	Log.d(TAG, "JoinRoom request success");
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
    	Log.d(TAG, "ExitRoom request success");
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
    	Log.d(TAG, "SendFile request success");
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
    	Log.d(TAG, "SendString request success");
    	return true;
    }

	public HttpsCommunicationCallback getHttpsCallback() {
		return httpsCallback;
	}

}