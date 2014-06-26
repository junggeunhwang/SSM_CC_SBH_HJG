package com.ssm.controller;


import com.example.cyclists.R;
import com.ssm.view.HomeLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;


public class HomeFragment extends Fragment {

	HomeLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = new HomeLayout(this);
		layout.createView(inflater, container);
		layout.init();

		return layout.getView();
	}
}
