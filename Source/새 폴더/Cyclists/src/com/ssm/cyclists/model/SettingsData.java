package com.ssm.cyclists.model;

public class SettingsData {
	private static SettingsData instance;
	
	private String ThemeColor;
	
	private boolean FacebookConnectd;
	private boolean TwitterConnectd;
	private boolean InstagramConnectd;
	
	private String Unit;
	
	
	private SettingsData() {
		ThemeColor = "gray";
		
		FacebookConnectd = false;
		TwitterConnectd = false;
		InstagramConnectd = false;
	}
	
	
	public static SettingsData getInstance(){
		if(instance==null) instance = new SettingsData();
		return instance;
	}


	public String getThemeColor() {
		return ThemeColor;
	}


	public boolean isFacebookConnectd() {
		return FacebookConnectd;
	}


	public boolean isTwitterConnectd() {
		return TwitterConnectd;
	}


	public boolean isInstagramConnectd() {
		return InstagramConnectd;
	}


	public void setThemeColor(String themeColor) {
		ThemeColor = themeColor;
	}


	public void setFacebookConnectd(boolean facebookConnectd) {
		FacebookConnectd = facebookConnectd;
	}


	public void setTwitterConnectd(boolean twitterConnectd) {
		TwitterConnectd = twitterConnectd;
	}


	public void setInstagramConnectd(boolean instagramConnectd) {
		InstagramConnectd = instagramConnectd;
	}
	
}
