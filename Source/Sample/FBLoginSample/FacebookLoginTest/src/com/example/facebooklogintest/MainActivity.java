package com.example.facebooklogintest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


@SuppressLint("ValidFragment") public class MainActivity extends ActionBarActivity {

	public TextView successMessage;
	public Button buttonLoginLogout;
	
	private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	successMessage = (TextView)findViewById(R.id.tv_success);
    	buttonLoginLogout = (Button)findViewById(R.id.button_login_logout);
    	
         
        getHashKey();
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        Session.getActiveSession().addCallback(statusCallback);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Session.getActiveSession().removeCallback(statusCallback);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
//    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Session session = Session.getActiveSession();
//        Session.saveSession(session, outState);
//    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	successMessage.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
        	getMyFacebookInfo(session);
            buttonLoginLogout.setText(R.string.logout);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });
        } else {
        	successMessage.setText(R.string.instructions);
            buttonLoginLogout.setText(R.string.login);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

//    private void onClickLogin() {
//        Session session = Session.getActiveSession();
//        if (!session.isOpened() && !session.isClosed()) {
//            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//        } else {
//            Session.openActiveSession(this, true, statusCallback);
//        }
//    }
//
//    private void onClickLogout() {
//        Session session = Session.getActiveSession();
//        if (!session.isClosed()) {
//            session.closeAndClearTokenInformation();
//        }
//    }
//    private void getMyFacebookInfo(Session session){
//    	if(session.isOpened()){
//    		Request.newMeRequest(session, new Request.GraphUserCallback() {
//    			 
//                @Override
//                public void onCompleted(GraphUser user, Response response) {
//                    response.getError();
//                    System.err.println(" getId  :  " + user.getId());
//                    System.err.println(" getFirstName  :  " + user.getFirstName());
//                    System.err.println(" getLastName  :  " + user.getLastName());
//                    System.err.println(" getMiddleName  :  " + user.getMiddleName());
//                    System.err.println(" getBirthday  :  " + user.getBirthday());
//                    System.err.println(" getLink  :  " + user.getLink());
//                    System.err.println(" getName  :  " + user.getName());
//                    System.err.println(" getUsername :  " + user.getUsername());
//                    System.err.println(" getLocation :  " + user.getLocation());
//                    System.err.println("getRawResponse  :  " + response.getRawResponse());
//                }
//            }).executeAsync();
//
//    	}
//    }
//    
//    private void getHashKey()
//    {
//            PackageInfo info;
//            try {
//            info = getPackageManager().getPackageInfo("com.example.facebooklogintest", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {MessageDigest md;
//            md = MessageDigest.getInstance("SHA");
//            md.update(signature.toByteArray());
//                       //String something = new String(Base64.encode(md.digest(), 0));
//                         String something = new String(Base64.encode(md.digest(),0));
//                       Log.e("** Hash Key", something);
//            }
//            }
//            catch (NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//            }
//
//            catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//            }
//            catch (Exception e){
//            Log.e("exception", e.toString());
//            }
//
//    }
    
    
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
}
