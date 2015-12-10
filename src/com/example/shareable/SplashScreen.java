package com.example.shareable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends Activity {
	Typeface font;
	int myProgress = 0;
	ProgressBar myProgressBar;
	CountDownTimer countDownTimer;
	WebView ls;
	int length_in_milliseconds=4000;
	 final SplashScreen sPlashScreen = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		TextView txt = (TextView) findViewById(R.id.AppNameHalfFirst); 
		TextView txt2 = (TextView) findViewById(R.id.AppNameHalfSecond); 
		font = Typeface.createFromAsset(getAssets(), "lato_reg.ttf");  
		txt.setTypeface(font);
		txt2.setTypeface(font);
		countDownTimer = new CountDownTimer(5000,001) {
	        private boolean warned = false;
	        @Override
	        public void onTick(long millisUntilFinished_) {
	        	
	        }

	        @Override
	        public void onFinish() {
	        	
	        		Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
		        	startActivity(mainIntent);
	        	
	        }
	    }.start();
		//new Thread(myThread).start();
	}
}