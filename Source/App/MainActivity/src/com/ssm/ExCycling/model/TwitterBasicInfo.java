package com.ssm.ExCycling.model;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterBasicInfo {
			
    public static final String TWIT_API_KEY = "n9EotGYxIBhY9iG7CL4aQKOFu";
    public static final String TWIT_CONSUMER_KEY = "n9EotGYxIBhY9iG7CL4aQKOFu";
    public static final String TWIT_CONSUMER_SECRET = "3zdniIxcefYW4cyubh4zFVIwafRwOMRWLXVgITc3t40QEXLsC8";
    public static final String TWIT_CALLBACK_URL = "뭘넣어야할까";
 
    public static final int REQ_CODE_TWIT_LOGIN = 1001;
 
    public static boolean TwitLogin = false;
    public static Twitter TwitInstance = null;
    public static AccessToken TwitAccessToken = null;
    public static RequestToken TwitRequestToken = null;
 
    public static String TWIT_KEY_TOKEN = "";
    public static String TWIT_KEY_TOKEN_SECRET = "";
    public static String TwitScreenName = "";
 
    @SuppressLint("SimpleDateFormat")
	public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
}
