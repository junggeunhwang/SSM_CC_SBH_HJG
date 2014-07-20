package com.ssm.ExCycling.view;

import java.util.Timer;
import java.util.TimerTask;

import com.ssm.ExCycling.R;
import com.ssm.ExCycling.R.id;
import com.ssm.ExCycling.R.layout;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;

public class ProgressDialog extends Dialog {

	ProgressBar progressBar;
	
	
	public ProgressDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_dialog);
		progressBar = (ProgressBar)findViewById(R.id.progressbar_dialog);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
	//	super.onWindowFocusChanged(hasFocus);
	}

}
