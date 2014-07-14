package com.ssm.cyclists.controller.manager;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class ResourceManager {
	private static ResourceManager instance;
	
	static Typeface helvetica_font;
	static Typeface nanum_gothic_font;

	static AssetManager mAssets;
	
	
	private ResourceManager() {
		
		
	}
	
	public void setAssetManager(AssetManager assets){
		mAssets = assets;
		if(helvetica_font==null)
			helvetica_font = Typeface.createFromAsset(mAssets,"fonts/helvetica.ttf");
		if(nanum_gothic_font==null)
			helvetica_font = Typeface.createFromAsset(mAssets,"fonts/nanum_gothic.ttf");
	}
	
	
	public Typeface getFont(String font_name){
		
		if(font_name.equals("helvetica")) return helvetica_font;
		else if(font_name.equals("nanum_gothic")) return nanum_gothic_font;
		else return null;
	}
	
	public static ResourceManager getInstance(){
		if(instance == null){
			instance = new ResourceManager();
		}
		return instance;
	}
}
