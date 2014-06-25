package com.ssm.view;

import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragmentLayout {

	protected Activity activity;
	protected Fragment fragment;
	protected View view;
	
	public BaseFragmentLayout(Fragment instance) {
		this.fragment = instance;
	}
	
	public BaseFragmentLayout(Activity instance) {
		this.activity = instance;
	}
	
	public abstract void createView(LayoutInflater inflater, ViewGroup container);
	
	public View getView(){
		return view;
	}
	
}
