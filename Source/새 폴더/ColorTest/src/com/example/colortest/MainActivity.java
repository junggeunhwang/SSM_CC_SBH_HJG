package com.example.colortest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	Button apply_button;
	EditText color_input;
	LinearLayout background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        
        apply_button = (Button)findViewById(R.id.apply_button);
        color_input = (EditText)findViewById(R.id.color_input);
        background = (LinearLayout)findViewById(R.id.layout);
     
        findViewById(R.id.apply_button).setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
								
				color_input.getText();
				String input = color_input.getText().toString();
				String sharp = "#"; 
				if(!input.contains("#"))input = sharp.concat(input);
				background.setBackgroundColor(Color.parseColor(input));
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
