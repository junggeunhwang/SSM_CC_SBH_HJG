package com.ssm.cyclists.model;

import android.graphics.Bitmap;
import android.location.Location;

public class UserData {
	
	private String UserName = null;
	private String UniqueID = null;
	private Bitmap ProfileImg = null;
	
	private String facebook_id = null;
	private boolean checked = false;
	private Location currentLocation = null;
	
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

	public String getFacebook_id() {
		return facebook_id;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
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