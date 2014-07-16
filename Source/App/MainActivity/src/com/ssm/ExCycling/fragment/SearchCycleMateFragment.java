package com.ssm.ExCycling.fragment;

import java.util.ArrayList;

import com.ssm.ExCycling.model.UserData;
import com.ssm.ExCycling.view.layout.SearchCycleMateLayout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SearchCycleMateFragment extends Fragment {

	SearchCycleMateLayout layout;
	
	ArrayList<UserData> SearchResult;
	
	public SearchCycleMateFragment() {
		ArrayList<UserData> SearchResult = new ArrayList<UserData>();
	}
	
	public ArrayList<UserData> getSearchResult() {
		return SearchResult;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = new SearchCycleMateLayout(this);
		layout.createView(inflater, container);
		layout.init();
		
		return layout.getView();
	}
	public void setSearchResult(ArrayList<UserData> searchResult) {
		SearchResult = searchResult;
	}
	public SearchCycleMateLayout getLayout() {
		assert(layout!=null);
		return layout;
	}
}
