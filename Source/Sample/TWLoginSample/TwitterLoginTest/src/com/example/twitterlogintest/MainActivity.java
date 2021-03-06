package com.example.twitterlogintest;

import java.net.URL;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	AccessToken TwitAccessToken;
	
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
				connect();					
			}
			
		});
	}
	
	public void connect(){
		TwitterConnectAsyncTask task = new TwitterConnectAsyncTask();
		task.execute();
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
		super.onActivityResult(requestCode, resultCode, resultIntent);
		
		if(resultCode == RESULT_OK){
			if(requestCode == TwitterBasicInfo.REQ_CODE_TWIT_LOGIN){
				try{
					Twitter mTwit = TwitInstance;
					
					AccessToken mAccessToken = mTwit.getOAuthAccessToken(TwitRequestToken,resultIntent.getStringExtra("oauthVerifier"));
					
					TwitterBasicInfo.TwitLogin = true;
					TwitterBasicInfo.TWIT_KEY_TOKEN = mAccessToken.getToken();
					TwitterBasicInfo.TWIT_KEY_TOKEN_SECRET = mAccessToken.getTokenSecret();
					
					TwitAccessToken = mAccessToken;
					
					Toast.makeText(getBaseContext(), "Twitter connection succeeded : + TWIT_KEY_TOKEN",Toast.LENGTH_LONG).show();
					
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
	}
	
	public class TwitterConnectAsyncTask extends AsyncTask<URL, Integer, Long>{

		@Override
		protected Long doInBackground(URL... params) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setDebugEnabled(true);
			builder.setOAuthConsumerKey(TwitterBasicInfo.TWIT_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TwitterBasicInfo.TWIT_CONSUMER_SECRET);
			
			TwitterFactory factory = new TwitterFactory(builder.build());
			Twitter mTwit = factory.getInstance();
			TwitInstance = mTwit;
			
			RequestToken mRequestToken = null;
			try {
				mRequestToken = mTwit.getOAuthRequestToken();	
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			TwitRequestToken = mRequestToken;
			
			
			String outToken = mRequestToken.getToken();
			String outTokenSecret = mRequestToken.getTokenSecret();
			Log.d(TAG, "Request Token : " + outToken + ", " + outTokenSecret);
			Log.d(TAG, "AuthorizationURL : " + mRequestToken.getAuthorizationURL());
			
			Intent intent = new Intent(MainActivity.this,TwitLogin.class);
			intent.putExtra("authUrl", mRequestToken.getAuthorizationURL());
			startActivityForResult(intent,TwitterBasicInfo.REQ_CODE_TWIT_LOGIN);
					
			return null;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			Log.d(TAG, "connect success");
			super.onPostExecute(result);
		}
	}
}
