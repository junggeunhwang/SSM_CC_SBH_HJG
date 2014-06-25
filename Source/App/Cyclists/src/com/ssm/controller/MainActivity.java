package com.ssm.controller;

import com.example.cyclists.R;
import com.ssm.view.MainLayout;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity{

	
	private MainLayout layout;
	
	public MainActivity() {
		
	}
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	Intent intent = new Intent(this,SplashActivity.class);
    	startActivity(intent);
    	
    	super.onCreate(savedInstanceState);
    	
    	layout = new MainLayout(this);

    	setContentView(layout.getView());
    	
    	if (savedInstanceState != null) {
	        layout.setActiveViewID(savedInstanceState.getInt(MainLayout.STATE_ACTIVE_VIEW_ID));
	    }
    	
        layout.init();
        
    }
    
    

    public void open_button(View v){
    	layout.open_button(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        layout.onRestoreInstanceState(inState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        layout.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               layout.toggle_menu();
               return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        layout.onBackBtnPressed();
        super.onBackPressed();
    }
    
    public MainLayout getLayout(){
    	return layout;
    }

}
