package com.ssm.ExCycling.controller.communication.https;

<<<<<<< HEAD
import java.io.ByteArrayOutputStream;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.NoSuchElementException;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

<<<<<<< HEAD
=======
import com.ssm.ExCycling.ProgressDialog;
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import com.ssm.ExCycling.R;
import com.ssm.ExCycling.controller.activity.MainActivity;
import com.ssm.ExCycling.controller.manager.DataBaseManager;
import com.ssm.ExCycling.controller.manager.SettingsDataManager;
import com.ssm.ExCycling.fragment.CruiseContainerFragment;
import com.ssm.ExCycling.fragment.CycleMateFragment;
import com.ssm.ExCycling.fragment.SearchCycleMateFragment;
import com.ssm.ExCycling.model.SettingsData;
import com.ssm.ExCycling.model.UserData;
<<<<<<< HEAD
import com.ssm.ExCycling.view.ProgressDialog;
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
import com.ssm.ExCycling.view.layout.MainLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class Protocol {
	
	private static Protocol instance;
	
	private static String TAG = Protocol.class.getSimpleName();
	
	
	private static final String DEST_DIR  = "/storage/emulated/legacy/Cyclists";
	private static final String DEST_DIR_RECEIVE  = DEST_DIR+"/Receive";
	private static final String DEST_DIR_SEND  = DEST_DIR+ "/Send" ;
	private static final String DEST_DIR_PROFILE  = DEST_DIR+ "/Profile" ;
	
	private ProgressDialog progressDialog;
	
	private HttpsCommunicationCallback httpsCallback;
	
	private Protocol() {
		
		
		httpsCallback = new HttpsCommunicationCallback() {
			@Override
			public void onResponseSuccess(HttpsCommunication hcn) {
				int getImageCount=0; 

				if(hcn.getResponseOrder().equals("get")){
				
					if(hcn.getResponseType().equals("string")){
						/*Multicast Data*/
						
						//방에 초대됨
						if(hcn.getStringResponseData().equals("JOINROOM"))
						{
<<<<<<< HEAD
							Log.d(TAG,"JOINROOM");
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
							MainActivity.getInstasnce().popupNotification();
							SettingsDataManager.getInstance().setStart_stopBicycleFlag(true);
							//운항 기록 시작
							MainActivity.getInstasnce().startCruiseInfoRecord();
<<<<<<< HEAD
							MainActivity.getInstasnce().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									//뷰페이저  활성화
									MainLayout.getmCruiseContainerFragment().setViewPagerEnable(true);
									MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cruise_container);
								}
							});
							
=======
							MainActivity.getInstasnce().getLayout();
							//뷰페이저  활성화
							MainLayout.getmCruiseContainerFragment().setViewPagerEnable(true);
							MainActivity.getInstasnce().getLayout().replaceFragment(R.layout.fragment_cruise_container);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
							return;
						}else if(hcn.getStringResponseData().equals("EXITROOM")){
							Log.d(TAG,"EXITROOM : " + hcn.getResponseUniqueNumber());
							ArrayList<UserData> roomFriendList = SettingsDataManager.getInstance().getCurrentRoomFriendList();
							for(int i = 0 ; i < roomFriendList.size() ; i++){
								if(roomFriendList.get(i).getUniqueID().equals(hcn.getResponseUniqueNumber())){
<<<<<<< HEAD
									
									AlertMessage(roomFriendList.get(i).getUserName(),roomFriendList.get(i).getUserName() + "is exit.");
									
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
									SettingsDataManager.getInstance().getCurrentRoomFriendList().remove(i);
									CruiseContainerFragment.getmCruiseTwoFragment().getLayout().getAdapter().reset();
									return;
								}
							}
						}
						
						String receive = hcn.getStringResponseData();
						String senderUniqueID = hcn.getResponseUniqueNumber();
						
						StringTokenizer tokenizer = new StringTokenizer(receive,",");
						String token = tokenizer.nextToken();
						Location loc = new Location("gps");
						double speed;
						if(token.equals("location")){
							Log.d(TAG,"locationBroadcast received");
							String userName = new String(Base64.decode(tokenizer.nextToken().getBytes(),Base64.DEFAULT));
							loc.setLatitude(Double.valueOf(tokenizer.nextToken()));
							loc.setLongitude(Double.valueOf(tokenizer.nextToken()));
							speed = Double.valueOf(tokenizer.nextToken());
							
							ArrayList<UserData> friendList = SettingsDataManager.getInstance().getCurrentRoomFriendList();
							
							boolean isInFriendflag = false;
							
							for(int i = 0 ; i < friendList.size() ; i++){
								if(friendList.get(i).getUniqueID().equals(senderUniqueID)){
									friendList.get(i).setCurrentLocation(loc);
									friendList.get(i).setSpeed(speed);
									isInFriendflag = true;
								}
							}
							
							if(!isInFriendflag){
								UserData data = new UserData();
								data.setUniqueID(senderUniqueID);
								data.setUserName(userName);
								data.setCurrentLocation(loc);
								SettingsDataManager.getInstance().getCurrentRoomFriendList().add(data);
								GetImage(SettingsDataManager.getInstance().getMe().getUniqueID(),senderUniqueID);
								CruiseContainerFragment.getmCruiseTwoFragment().getLayout().getAdapter().reset();
								Log.d(TAG,"CurrentRoomMember Added : " + senderUniqueID);
							}
						}
					}else if(hcn.getResponseType().equals("file")){
						
<<<<<<< HEAD
						String fileName;
						File file = null;
						/* 서버  -> 안드로이드 파일 수신*/
						for(int i = 0 ;i < SettingsDataManager.getInstance().getFriendList().size();i++){
							if(hcn.getResponseUniqueNumber().equals(SettingsDataManager.getInstance().getFriendList().get(i).getUniqueID())){
								fileName = SettingsDataManager.getInstance().getFriendList().get(i).getUserName()+".amr";
								file = new File(DEST_DIR_RECEIVE + "/" + fileName);
							}
						}
=======
						/* 서버  -> 안드로이드 파일 수신*/
						
						String fileName = hcn.getResponseUniqueNumber()+System.currentTimeMillis()+".amr";
						File file = new File(DEST_DIR_RECEIVE + "/" + fileName);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
						
						try {
							FileOutputStream fileOutputStream = new FileOutputStream(file);
							fileOutputStream.write(hcn.getByteResponseData());
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						
						MainActivity.getInstasnce().getmSAPService().sendFile(file.getAbsolutePath());
						

					}else if(hcn.getResponseType().equals("text")){
						/* 서버  -> 안드로이드 응답 수신 */
						
					}
<<<<<<< HEAD
				}else if(hcn.getResponseOrder().equals("file")){
					Log.d(TAG,hcn.getStringResponseData());
					if(hcn.getPath()!=null){
						new File(hcn.getPath()).delete();
					}
				}
				
				else if(hcn.getResponseOrder().equals("login")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")){
						Log.i(TAG,"Login Success");
						
=======
				}else if(hcn.getResponseOrder().equals("login")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")){
						Log.i(TAG,"Login Success");
						UpdateProfile(SettingsDataManager.getInstance().getMe().getUniqueID());
						FriendsListRequest(SettingsDataManager.getInstance().getMe().getUniqueID());
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
					}
					else if(hcn.getStringResponseData().equals("EALREADYLOGIN")){
						Log.e(TAG,"Login Fail : EALREADYLOGIN");
						FriendsListRequest(SettingsDataManager.getInstance().getMe().getUniqueID());
					}
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Login Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("logout")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.i(TAG,"Logout Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")){
						Log.e(TAG,"logout Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"logout Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("mkroom")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")){
						//방 생성
						Log.i(TAG,"MakeRoom Success");
<<<<<<< HEAD
						MainLayout.getmCruiseContainerFragment().setViewPagerEnable(true);
						SettingsDataManager.getInstance().setStart_stopBicycleFlag(true);
=======
						
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
						//선택된 사용자 초대
						Protocol.getInstance().InviteFriend(SettingsDataManager.getInstance().getMe().getUniqueID(),SettingsDataManager.getInstance().getCurrentRoomFriendList());
						
						for(int i =  0; i < SettingsDataManager.getInstance().getCurrentRoomFriendList().size();i++){ 
							// 방 참석자 브로드캐스트
							String friendsUniqueID = "roommember,";
							
							for(int j = 0 ; j < SettingsDataManager.getInstance().getCurrentRoomFriendList().size();j++){
								friendsUniqueID += SettingsDataManager.getInstance().getCurrentRoomFriendList().get(j).getUniqueID();
								if((j+1)!=SettingsDataManager.getInstance().getCurrentRoomFriendList().size()) friendsUniqueID += ",";
							}
							Protocol.getInstance().SendString(friendsUniqueID, SettingsDataManager.getInstance().getMe().getUniqueID());
						}
					}
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")){
						Log.e(TAG,"MakeRoom Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"MakeRoom Fail : EMISSING");
<<<<<<< HEAD
					else if(hcn.getStringResponseData().equals("EALREADYJOINROOM")) Log.e(TAG,"MakeRoom Fail : EALREADYJOINROOM");
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
					
				}else if(hcn.getResponseOrder().equals("joinroom")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")) Log.i(TAG,"JoinRoom Success");
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
						Log.e(TAG,"JoinRoom Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("EALREADYJOINROOM")) Log.e(TAG,"JoinRoom Fail : EALREADYJOINROOM");
					else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"JoinRoom Fail : ENOTARGET");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"JoinRoom Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("exitroom")){
					
					if(hcn.getStringResponseData().equals("SUCCESS")){
						Log.d(TAG,"ExitRoom Success");
						ArrayList<UserData> roomFriendList = SettingsDataManager.getInstance().getCurrentRoomFriendList();
						String uniqueID = hcn.getResponseUniqueNumber();
						for(int i = 0 ; i < roomFriendList.size() ; i++){
							if(roomFriendList.get(i).getUniqueID().equals(uniqueID)){
								SettingsDataManager.getInstance().getCurrentRoomFriendList().remove(roomFriendList.get(i));
								MainLayout.getmCruiseContainerFragment();
								CruiseContainerFragment.getmCruiseTwoFragment().getLayout().getAdapter().notifyDataSetChanged();
								
							}
						}
						
						
					}
					else if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
						Log.e(TAG,"ExitRoom Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("ENOTJOINROOM")) Log.e(TAG,"ExitRoom Fail : ENOTJOINROOM");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"ExitRoom Fail : EMISSING");
					
				}else if(hcn.getResponseOrder().equals("addfriend")){
					
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
						Log.e(TAG,"AddFriend Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("ENOTARGET")){
						Log.e(TAG,"AddFriend Fail : ENOTTARGET");
						makeToast("User does not exist.");
						((SearchCycleMateFragment)MainActivity.getInstasnce().getLayout().getActivated_fragment()).getLayout().setProgressBarVisible(false);
						((SearchCycleMateFragment)MainActivity.getInstasnce().getLayout().getActivated_fragment()).getLayout().enableButton(true);
					}
					else if(hcn.getStringResponseData().equals("EALREADYFRIEND")){
						Log.e(TAG,"AddFriend Fail : EALREADYFRIEND");
						makeToast("Users are already friends.");
						((SearchCycleMateFragment)MainActivity.getInstasnce().getLayout().getActivated_fragment()).getLayout().setProgressBarVisible(false);
						((SearchCycleMateFragment)MainActivity.getInstasnce().getLayout().getActivated_fragment()).getLayout().enableButton(true);
					}
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"AddFriend Fail : EMISSING");
					else{
						Log.d(TAG,"AddFriend Success");
						UserData newFriend = new UserData();
						newFriend.setUniqueID(hcn.getResponseUniqueNumber());
						SettingsDataManager.getInstance().getFriendList().add(newFriend);
						Protocol.getInstance().FriendsListRequest(SettingsDataManager.getInstance().getMe().getUniqueID());
						makeToast("Add to friends success.");
					}
					
				}else if(hcn.getResponseOrder().equals("delfriend")){
					
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
						Log.e(TAG,"Delete Friend Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("ENOTTARGET")) Log.e(TAG,"Delete Friend Fail : ENOTTARGET");
					else if(hcn.getStringResponseData().equals("ENOTFRIEND")) Log.e(TAG,"Delete Friend Fail : ENOTFRIEND");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Delete Friend Fail : EMISSING");
					else if(hcn.getStringResponseData().equals("SUCCESS")){
						Log.d(TAG,"Delete Friend Success");
						makeToast("Remove friend success.");
						ArrayList<UserData> friendList = SettingsDataManager.getInstance().getCurrentRoomFriendList();
						String uniqueID = hcn.getResponseUniqueNumber();
						for(int i = 0 ; i < friendList.size() ; i++){
							if(friendList.get(i).getUniqueID().equals(uniqueID)){
								SettingsDataManager.getInstance().getFriendList().remove(friendList.get(i));
							}
						}
					}
					MainActivity.getInstasnce().getLayout().getmFragmentCycleMate().getLayout().getAdapter().reset();
					hideProgressDialog();
				}else if(hcn.getResponseOrder().equals("friendlist")){
					
					if(hcn.getResponseType().equals("text")){
						if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
							Log.e(TAG,"Get FriendList Fail : ENOTLOGIN");
						}
						else if(hcn.getStringResponseData().equals("ENOLIST"))Log.e(TAG,"Get FriendList Fail : ENOLIST");
						else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Get FriendList Fail : EMISSING");	
						ArrayList<UserData> friendsList = new ArrayList<UserData>();
						SettingsDataManager.getInstance().setFriendList(friendsList);
						hideProgressDialog();
					}
					else if(hcn.getResponseType().equals("string")){
						Log.i(TAG,"get FriendList Success");
						
						String friendlist = hcn.getStringResponseData();
						StringTokenizer tokenizer = new StringTokenizer(friendlist,";"); 
						
 						String strFriendCount = tokenizer.nextToken();
						int friendCount = Integer.valueOf(strFriendCount);       
						
						ArrayList<UserData> friendsList = new ArrayList<UserData>();
						for(int i = 0 ; i < friendCount ; i++){
							UserData newFriend = new UserData();
<<<<<<< HEAD
							try{
								newFriend.setUniqueID(tokenizer.nextToken());
							}catch(NoSuchElementException e){
								newFriend.setUniqueID("");
								Log.e(TAG, "friendlist UniqueID: No Such Data Exception");
								e.printStackTrace();
							}
							
							try{
								newFriend.setUserName(new String(Base64.decode(tokenizer.nextToken(), Base64.DEFAULT)));
							}catch(NoSuchElementException e){
								Log.e(TAG, "friendlist UserName: No Such Data Exception");
								newFriend.setUserName("");
								e.printStackTrace();
							}
							
							try{
								byte[] byteArr = Base64.decode(tokenizer.nextToken(), Base64.DEFAULT);
								Bitmap bitmap = BitmapFactory.decodeByteArray(byteArr , 0, byteArr .length);
								newFriend.setProfileImg(bitmap);
							}catch(NoSuchElementException e){
								Log.e(TAG, "friendlist ProfileImg: No Such Data Exception");
								e.printStackTrace();
							}
							friendsList.add(newFriend);
							
//							GetImage(SettingsDataManager.getInstance().getMe().getUniqueID(),newFriend.getUniqueID());
						}
						SettingsDataManager.getInstance().setFriendList(friendsList);
						MainActivity.getInstasnce().getLayout().getmFragmentCycleMate().getLayout().getAdapter().reset();
						hideProgressDialog();
=======
							newFriend.setUniqueID(tokenizer.nextToken());
							newFriend.setUserName(new String(Base64.decode(tokenizer.nextToken(), Base64.DEFAULT)));
							friendsList.add(newFriend);
							GetImage(SettingsDataManager.getInstance().getMe().getUniqueID(),newFriend.getUniqueID());
						}
						SettingsDataManager.getInstance().setFriendList(friendsList);
						MainActivity.getInstasnce().getLayout().getmFragmentCycleMate().getLayout().getAdapter().reset();
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
					}	
				}
				else if(hcn.getResponseOrder().equals("getimage")){
					
					if(hcn.getResponseType().equals("text")){
						
						if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
							Log.e(TAG,"getimage Fail : ENOTLOGIN");
						}
						else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"getimage Fail : ENOTARGET");
						else if(hcn.getStringResponseData().equals("ENOTARGET")) Log.e(TAG,"getimage Fail : ENONAME");
						else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"getimage Fail : EMISSING");
						else Log.e(TAG, "getimage Fail");
						
<<<<<<< HEAD
					}
					else if(hcn.getResponseType().equals("string")){
						//row profile data 
						byte[] profile = Base64.decode(hcn.getStringResponseData(), Base64.DEFAULT);
=======
						getImageCount++;
						if(getImageCount == SettingsDataManager.getInstance().getFriendList().size()){
							getImageCount=0;
							hideProgressDialog();
						}
					}
					else if(hcn.getResponseType().equals("file")){
						//row profile data 
						byte[] profile = hcn.getByteResponseData();
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
						//convert byte[] -> Bitmap
						Bitmap profile_bitmap = BitmapFactory.decodeByteArray(profile, 0, profile.length);
												
						String targetNumber = hcn.getResponseUniqueNumber();
						ArrayList<UserData> friendList = SettingsDataManager.getInstance().getFriendList();
						
						for(int i = 0;i<friendList.size();i++){
							if(friendList.get(i).getUniqueID().equals(targetNumber)){
								friendList.get(i).setProfileImg(profile_bitmap);
							}
						}
						
<<<<<<< HEAD
=======
						getImageCount++;
						if(getImageCount == SettingsDataManager.getInstance().getFriendList().size()){
							getImageCount=0;
							hideProgressDialog();
						}
						
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
						Log.i(TAG,"GetFile Success");
					}
					if(MainActivity.getInstasnce().getLayout().getActivated_fragment().getClass().equals(CycleMateFragment.class)){
						MainActivity.getInstasnce().getLayout().getmFragmentCycleMate().getLayout().getAdapter().reset();
					}
<<<<<<< HEAD
					hideProgressDialog();
=======

>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
				}else if(hcn.getResponseOrder().equals("inviteroom")){
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) {
						Log.e(TAG,"inviteroom Fail : ENOTLOGIN");
					}
					else if(hcn.getStringResponseData().equals("ENOTJOINROOM")) Log.e(TAG,"inviteroom Fail : ENOTJOINROOM");
<<<<<<< HEAD
					else if(hcn.getStringResponseData().equals("ETARGETNOTLOGIN")) {
						Log.e(TAG,"inviteroom Fail : ETARGETNOTLOGIN");
						for(int i = 0 ;i < SettingsDataManager.getInstance().getFriendList().size();i++){
							if(hcn.getResponseUniqueNumber().equals(SettingsDataManager.getInstance().getFriendList().get(i))){
								makeToast(SettingsDataManager.getInstance().getFriendList().get(i).getUserName() + "is not login");
							}
						}
					}
					else if(hcn.getStringResponseData().equals("ETARGETALREADYJOINROOM")){
						Log.e(TAG,"inviteroom Fail : ETARGETALREADYJOINROOM");
						for(int i = 0 ;i < SettingsDataManager.getInstance().getFriendList().size();i++){
							if(hcn.getResponseUniqueNumber().equals(SettingsDataManager.getInstance().getFriendList().get(i))){
								makeToast(SettingsDataManager.getInstance().getFriendList().get(i).getUserName() + "is already joinroom");
							}
						}
						
					}
=======
					else if(hcn.getStringResponseData().equals("ETARGETNOTLOGIN")) Log.e(TAG,"inviteroom Fail : ETARGETNOTLOGIN");
					else if(hcn.getStringResponseData().equals("ETARGETALREADYJOINROOM")) Log.e(TAG,"inviteroom Fail : ETARGETALREADYJOINROOM");
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
					else if(hcn.getStringResponseData().equals("SUCCESS")){
						Log.e(TAG,"inviteroom SUCCESS");
					}
				}else if(hcn.getResponseOrder().equals("update")){
					
					if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"Update Profile Fail : ENOTLOGIN");
					else if(hcn.getStringResponseData().equals("EMISSING")) Log.e(TAG,"Update Profile Fail : EMISSING");
					else if(hcn.getStringResponseData().equals("SUCCESS")) Log.d(TAG,"Update Profile Success");	
					
				}else if(hcn.getResponseOrder().equals("search")){
					if(hcn.getResponseType().equals("text")){
						if(hcn.getStringResponseData().equals("ENOTLOGIN")) Log.e(TAG,"Search Fail : ENOTLOGIN");
<<<<<<< HEAD
						else if(hcn.getStringResponseData().equals("ENOSEARCHDATA")){
							ArrayList<UserData> result = new ArrayList<UserData>();
							MainActivity.getInstasnce().getLayout().getmFragmentCycleMate().getLayout().getAdapter().setData(result);
							makeToast("Results does not exist.");
							Log.e(TAG,"Search Fail : ENOSEARCHDATA");
						}
=======
						else if(hcn.getStringResponseData().equals("ENOSEARCHDATA")) Log.e(TAG,"Search Fail : ENOSEARCHDATA");
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
					}
					else if(hcn.getResponseType().equals("string")){
						String receive = hcn.getStringResponseData();
						
						StringTokenizer tokenizer = new StringTokenizer(receive,";"); 
						
 						String strResultCount = tokenizer.nextToken();
						int nResultCount = Integer.valueOf(strResultCount);       
						
						ArrayList<UserData> result = new ArrayList<UserData>();
						
<<<<<<< HEAD
						ArrayList<UserData> friendsList = new ArrayList<UserData>();
						for(int i = 0 ; i < nResultCount ; i++){
							UserData newFriend = new UserData();
							try{
								newFriend.setUniqueID(tokenizer.nextToken());
							}catch(NoSuchElementException e){
								newFriend.setUniqueID("");
								Log.e(TAG, "Search UniqueID: No Such Data Exception");
								e.printStackTrace();
							}
							
							try{
								newFriend.setUserName(new String(Base64.decode(tokenizer.nextToken(), Base64.DEFAULT)));
							}catch(NoSuchElementException e){
								Log.e(TAG, "Search UserName: No Such Data Exception");
								newFriend.setUserName("");
								e.printStackTrace();
							}
							
							try{
								String tempBitmap = tokenizer.nextToken();
								if(!tempBitmap.equals("NONE")){
									byte[] byteArr = Base64.decode(tempBitmap, Base64.DEFAULT);
									Bitmap bitmap = BitmapFactory.decodeByteArray(byteArr , 0, byteArr .length);
									newFriend.setProfileImg(bitmap);	
								}
							}catch(NoSuchElementException e){
								Log.e(TAG, "Search ProfileImg: No Such Data Exception");
								e.printStackTrace();
							}
							result.add(newFriend);
							
=======
						for(int i = 0 ; i < nResultCount ; i++){
							UserData searchResultUser = new UserData();
							searchResultUser.setUniqueID(tokenizer.nextToken());
							searchResultUser.setUserName(new String(Base64.decode(tokenizer.nextToken(),Base64.DEFAULT)));
							result.add(searchResultUser);
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
						}
						
						MainActivity.getInstasnce().getLayout().getmFragmentCycleMate().getLayout().getAdapter().setData(result);
						Log.d(TAG,"Search Success : " + strResultCount);
						
					}
					hideProgressDialog();
				}
			}
			
			@Override
			public void onResponseFailure(String errMsg) {
				Log.e(TAG,"https : response failure");
			}
		};
	
		progressDialog = new ProgressDialog(MainActivity.getInstasnce());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
	}
	
	public static Protocol getInstance(){
		if(instance==null)instance = new Protocol();
		return instance;
	}

	private void makeToast(final String text){
		 new Thread() {
             @Override
             public void run() {
                 Looper.prepare();
                 Toast.makeText(MainActivity.getInstasnce(),text,Toast.LENGTH_SHORT).show();
                 Looper.loop();
             }
         }.start();
	}

	
	Timer cancelTimer;
	
	private void showProgressDialog(){
		MainActivity.getInstasnce().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(progressDialog!=null) progressDialog.show();
				
				cancelTimer = new Timer();
				cancelTimer.schedule(new TimerTask(){
					@Override
					public void run() {
//						makeToast("Network status is not good.");
 						hideProgressDialog();
					}
				}, 5000);
			}
		});

	}
	
	private void hideProgressDialog(){
		MainActivity.getInstasnce().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null) progressDialog.hide();
				if(cancelTimer!=null){
					cancelTimer.cancel();
					cancelTimer = null;
				}
			}
		});

	}
	
	public void DismissProgressDialog(){
		progressDialog.dismiss();
	}
	
	public boolean SearchFriend(String myNumber,String targetName){
		showProgressDialog();
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setUniqueNumber(myNumber);
		httpsCommunication.setStringData("search");
		httpsCommunication.setExtraData(Base64.encodeToString(targetName.getBytes(),Base64.DEFAULT));
		
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "search requeset failed");
			return false;
		}
		Log.d(TAG, "search request success : " + targetName);
		return true;
	}
	
	public boolean InviteFriend(String myNumber,ArrayList<UserData> targetNumbers){
		for(int i = 0 ; i < targetNumbers.size();i++){
			HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
			httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
			httpsCommunication.setUniqueNumber(myNumber);
			httpsCommunication.setStringData("inviteroom");
			httpsCommunication.setExtraData(targetNumbers.get(i).getUniqueID());
			
			if(!httpsCommunication.ExecuteRequest()){
				Log.e(TAG, "invite friend requeset failed");
				return false;
			}
			Log.d(TAG, "invite friend request success : " + targetNumbers.get(i));	
		}
		
		return true;
	}
	
	public boolean AddFriendRequest(String myNumber,String targetNumber){
		showProgressDialog();
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
		showProgressDialog();
		
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
		showProgressDialog();
		
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("friendlist");
		httpsCommunication.setUniqueNumber(myNumber);
		
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
	
<<<<<<< HEAD
//    public boolean Login(String myNumber){
//    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
//    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
//    	httpsCommunication.setStringData("login");
//    	httpsCommunication.setUniqueNumber(myNumber);
////    	UserData me = SettingsDataManager.getInstance().getMe();
////    	if(me.getUserName()!=null){
////    		httpsCommunication.setExtraData(me.getUserName());
////    	}
////    	if(me.getProfileImg()!=null){
////    		File myProfile = new File(DEST_DIR_PROFILE+"/me.png");
////    		httpsCommunication.setFileData(myProfile);
////    	}
//		if(!httpsCommunication.ExecuteRequest()){
//			Log.e(TAG, "login requeset failed");
//			return false;
//		}
//		Log.d(TAG, "login request success");
//		return true;
//    }
=======
    public boolean Login(String myNumber){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("login");
    	httpsCommunication.setUniqueNumber(myNumber);
//    	UserData me = SettingsDataManager.getInstance().getMe();
//    	if(me.getUserName()!=null){
//    		httpsCommunication.setExtraData(me.getUserName());
//    	}
//    	if(me.getProfileImg()!=null){
//    		File myProfile = new File(DEST_DIR_PROFILE+"/me.png");
//    		httpsCommunication.setFileData(myProfile);
//    	}
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "login requeset failed");
			return false;
		}
		Log.d(TAG, "login request success");
		return true;
    }
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    
    public boolean UpdateProfile(String myNumber){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("update");
    	httpsCommunication.setUniqueNumber(myNumber);
    	
    	UserData me = SettingsDataManager.getInstance().getMe();
<<<<<<< HEAD
    	String send ="";
    	if(me.getUserName()!=null){
//    		httpsCommunication.setExtraData(Base64.encodeToString(me.getUserName().getBytes(),Base64.DEFAULT));
    		send += Base64.encodeToString(me.getUserName().getBytes(),Base64.DEFAULT);
		}
    	send += ";";
		if(me.getProfileImg()!=null){
//			File myProfile = new File(DEST_DIR_PROFILE+"/me.png");
//			httpsCommunication.setFileData(myProfile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			me.getProfileImg().compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			send+=Base64.encodeToString(byteArray,Base64.DEFAULT);
		}
		
		httpsCommunication.setExtraData(send);
=======
    	if(me.getUserName()!=null){
    		httpsCommunication.setExtraData(Base64.encodeToString(me.getUserName().getBytes(),Base64.DEFAULT));
		}
		if(me.getProfileImg()!=null){
			File myProfile = new File(DEST_DIR_PROFILE+"/me.png");
			httpsCommunication.setFileData(myProfile);
		}
		
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "UpdateProfile requeset failed");
			return false;
		}
		Log.d(TAG, "UpdateProfile request success");
		return true;
    }
    
<<<<<<< HEAD
//    public boolean Logout(String myNumber){
//    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
//    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
//    	httpsCommunication.setStringData("logout");
//    	httpsCommunication.setUniqueNumber(myNumber);
//    	
//		if(!httpsCommunication.ExecuteRequest()){
//			Log.e(TAG, "logout requeset failed");
//			return false;
//		}
//		Log.d(TAG, "logout request success");
//		return true;
//    }
=======
    public boolean Logout(String myNumber){
    	HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
    	httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
    	httpsCommunication.setStringData("logout");
    	httpsCommunication.setUniqueNumber(myNumber);
    	
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "logout requeset failed");
			return false;
		}
		Log.d(TAG, "logout request success");
		return true;
    }
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
    
    public boolean MakeRoom(String myNumber){
		HttpsCommunication httpsCommunication = new HttpsCommunication(httpsCallback);
		httpsCommunication.setType(HttpsCommunication.TYPE_REQUEST);
		httpsCommunication.setStringData("mkroom");
		httpsCommunication.setUniqueNumber(myNumber);
		if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "mkroom requeset failed");
			return false;
		}
<<<<<<< HEAD
		Log.d(TAG, "mkroom requeset success");
=======
		Log.d(TAG, "mkroom success");
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
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
        File file = new File(path);
        httpsCommunication.setFileData(file);
<<<<<<< HEAD
    	httpsCommunication.setPath(path);
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "SendFile requeset failed");
			return false;
		}
=======
    	
    	if(!httpsCommunication.ExecuteRequest()){
			Log.e(TAG, "SendFile requeset failed");
			file.delete();
			return false;
		}
    	file.delete();
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98
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
<<<<<<< HEAD
    
    public void AlertMessage(String sender,String msg){
		String response;
		response = String.format("alertmsg;%s;%s",msg,sender);
		Log.d(TAG,response);
		MainActivity.getInstasnce().getmSAPService().SendByteData(response.getBytes());
	}
=======
>>>>>>> 276e7d88dd36c958c6c77998ee4fe5801d5d9d98

	public HttpsCommunicationCallback getHttpsCallback() {
		return httpsCallback;
	}

}
