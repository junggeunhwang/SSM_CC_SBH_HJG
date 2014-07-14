package com.ssm.cyclists.controller.asynctask;

import java.net.URL;

import com.ssm.cyclists.controller.activity.MainActivity;
import com.ssm.cyclists.controller.activity.TwitLoginActivity;
import com.ssm.cyclists.controller.manager.TwitterManager;
import com.ssm.cyclists.model.TwitterBasicInfo;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class TwitterConnectAsyncTask extends AsyncTask<URL, Integer, Long> {

	static String TAG = TwitterConnectAsyncTask.class.getSimpleName(); 
	
	@Override
	protected Long doInBackground(URL... arg0) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setDebugEnabled(true);
		builder.setOAuthConsumerKey(TwitterBasicInfo.TWIT_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterBasicInfo.TWIT_CONSUMER_SECRET);
		
		TwitterFactory factory = new TwitterFactory(builder.build());
		Twitter mTwit = factory.getInstance();
		TwitterManager.getInstance().setTwitInstance(mTwit);
		
		RequestToken mRequestToken = null;
		try {
			mRequestToken = mTwit.getOAuthRequestToken();	
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		TwitterManager.getInstance().setTwitRequestToken(mRequestToken);
		
		String outToken = mRequestToken.getToken();
		String outTokenSecret = mRequestToken.getTokenSecret();
		Log.d(TAG, "Request Token : " + outToken + ", " + outTokenSecret);
		Log.d(TAG, "AuthorizationURL : " + mRequestToken.getAuthorizationURL());
		
		Intent intent = new Intent(MainActivity.getInstasnce(),TwitLoginActivity.class);
		intent.putExtra("authUrl", mRequestToken.getAuthorizationURL());
		MainActivity.getInstasnce().startActivityForResult(intent,TwitterBasicInfo.REQ_CODE_TWIT_LOGIN);
				
		return null;
	}

	@Override
	protected void onPostExecute(Long result) {
		Log.d(TAG, "connect success");
		super.onPostExecute(result);
	}
}