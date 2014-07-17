package com.ssm.cyclists.controller.fragment;

import com.ssm.cyclists.view.layout.CycleRoomLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CycleRoomFragment extends Fragment {

	private CycleRoomLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = new CycleRoomLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}
	
	public CycleRoomLayout getLayout() {
		assert(layout!=null);
		return layout;
	}
}
