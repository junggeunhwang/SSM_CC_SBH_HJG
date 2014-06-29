package com.ssm.cyclists.view;

import com.ssm.cyclists.R;
import com.ssm.cyclists.controller.HomeFragment;
import com.ssm.cyclists.model.ResourceManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class HomeLayout extends BaseFragmentLayout {
	
	Button menubutton;
	
	TextView tvUserID;
	TextView tvLocation;
	TextView tvWeekDay;
	TextView tvTemperature;
	TextView tvRainPercent;
	ImageViewRounded ivProfileImage;
	public HomeLayout(HomeFragment instance) {
		super(instance);
	}
	
	public void init(){
		

		
		tvUserID = (TextView)getView().findViewById(R.id.user_id_home);
		tvLocation = (TextView)getView().findViewById(R.id.location_home);
		tvWeekDay = (TextView)getView().findViewById(R.id.weekday_home);
		tvTemperature = (TextView)getView().findViewById(R.id.temperature_home);
		tvRainPercent = (TextView)getView().findViewById(R.id.rainpercent_home);
		ivProfileImage = (ImageViewRounded)getView().findViewById(R.id.profile_image);
		
		ivProfileImage.setImageResource(R.drawable.profile_sample);
		
		
		tvUserID.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvLocation.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvWeekDay.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		
		tvTemperature.setTypeface(ResourceManager.getInstance().getFont("helvetica"));
		tvRainPercent.setTypeface(ResourceManager.getInstance().getFont("nanum_gothic"));
		
	}
	@Override
	public void createView(LayoutInflater inflater, ViewGroup container){
		view = inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	private OnClickListener buildButtonClickListner(){
		
		return new Button.OnClickListener(){
				
					@Override
					public void onClick(View v) {

					}
				};
	}
	
	/*
	  * Making image in circular shape
	  */
	 public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

	  int targetWidth = 50;
	  int targetHeight = 50;

	
	  Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,Bitmap.Config.ARGB_8888);
	  Canvas canvas = new Canvas(targetBitmap);
	  Path path = new Path();
	  path.addCircle(((float) targetWidth) / 2,((float) targetHeight) / 2,(Math.min(((float) targetWidth),
			  ((float) targetHeight)) / 2),Path.Direction.CW);
	  Paint paint = new Paint(); 
	  paint.setColor(Color.GRAY); 

	  paint.setStyle(Paint.Style.FILL);
	  paint.setAntiAlias(true);
	  paint.setDither(true);
	  paint.setFilterBitmap(true);
	  canvas.drawOval(new RectF(0, 0, targetWidth, targetHeight), paint) ;
 
	  canvas.clipPath(path);
	  Bitmap sourceBitmap = scaleBitmapImage;
	  canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
			  sourceBitmap.getHeight()), new RectF(0, 0, targetWidth,
			  targetHeight), paint);
	  
	  
	  canvas.drawBitmap(sourceBitmap,new Rect(0, 0, sourceBitmap.getWidth(),
	    sourceBitmap.getHeight()),new Rect(0, 0, targetWidth,
	    targetHeight), null);
	  
	  return targetBitmap;
	 }

}