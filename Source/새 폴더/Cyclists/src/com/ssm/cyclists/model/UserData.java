package com.ssm.cyclists.model;

import android.graphics.Bitmap;

public class UserData {
	
	private String UserName = "";
	private String UniqueID = "";
	private Bitmap ProfileImg = null;
	
	private boolean checked = false;
	
	public UserData() {
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public Bitmap getProfileImg() {
		
		return ProfileImg;
	}

	public boolean isChecked() {
		return checked;
	}

	public String getUniqueID() {
		return UniqueID;
	}

	public void setUniqueID(String uniqueID) {
		UniqueID = uniqueID;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setProfileImg(Bitmap profileImg) {
		ProfileImg = profileImg;
	}
	
	
	
}
