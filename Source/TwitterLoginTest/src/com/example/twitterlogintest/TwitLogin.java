package com.example.twitterlogintest;

import java.net.URL;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;

public class TwitLogin extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twit_login);
		
		WebView webview = (WebView)findViewById(R.id.webview);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {

				super.onPageFinished(view, url);
				
				if(url != null && url.equals("http://mobile.twitter.com/")){
					finish();
				}else if(url != null && url.startsWith(TwitterBasicInfo.TWIT_CALLBACK_URL)){//////////////////TWIT_CALLBACK_URL is null value(temporary)
					String[] params = url.split("\\?")[1].split("&");
					String oauthToken = "";
					String oauthVerifier = "";
					
					try{
						if(params[0].startsWith("oauth_token")){
							oauthToken = params[0].split("=")[1];
						}else if(params[1].startsWith("oauth_token")){
							oauthToken = params[1].split("=")[1];
						}
						
						if(params[0].startsWith("oauth_verifier")){
							oauthVerifier = params[0].split("=")[1];
						}else if(params[1].startsWith("oauth_verifier")){
							oauthVerifier = params[1].split("=")[1];
						}
						
						Intent resultIntent = new Intent();
						resultIntent.putExtra("oauthToken",oauthToken);
						resultIntent.putExtra("oauthVerifier", oauthVerifier);
						setResult(RESULT_OK,resultIntent);
						
						finish();
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		Intent passedIntent = getIntent();
		String authUrl = passedIntent.getStringExtra("authUrl");
		webview.loadUrl(authUrl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twit_login, menu);
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
