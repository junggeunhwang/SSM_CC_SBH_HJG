package com.example.twitterlogintest;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	static String TWIT_CONSUMER_KEY = "n9EotGYxIBhY9iG7CL4aQKOFu";
	static String TWIT_CONSUMER_SECRET = "3zdniIxcefYW4cyubh4zFVIwafRwOMRWLXVgITc3t40QEXLsC8";
	static String TAG = MainActivity.class.getSimpleName();
	Twitter TwitInstance;
	RequestToken TwitRequestToken;
	
	Button twLoginBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		twLoginBtn = (Button)findViewById(R.id.twlogin);
		
		twLoginBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
			}
			
		});
		
	}
	
	public void connect(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setDebugEnabled(true);
		builder.setOAuthConsumerKey(TWIT_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TWIT_CONSUMER_SECRET);
		
		TwitterFactory factory = new TwitterFactory(builder.build());
		Twitter mTwit = factory.getInstance();
		
		RequestToken mRequestToken;
		try {
			mRequestToken = mTwit.getOAuthRequestToken();
			String outToken = mRequestToken.getToken();
			String outTokenSecret = mRequestToken.getTokenSecret();
			Log.d(TAG, "Request Token : " + outToken + ", " + outTokenSecret);
			Log.d(TAG, "AuthorizationURL : " + mRequestToken.getAuthorizationURL());
			
			TwitInstance = mTwit;
			TwitRequestToken = mRequestToken;
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}

//		Intent intent = new Intent(this,)
		
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
