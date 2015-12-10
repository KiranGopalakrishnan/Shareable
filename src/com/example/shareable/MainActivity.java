package com.example.shareable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	Typeface font;
	LinearLayout StartSharing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView txt = (TextView) findViewById(R.id.StartPlayText); 
		font = Typeface.createFromAsset(getAssets(), "lato_reg.ttf");
		TextView txt2 = (TextView) findViewById(R.id.StartExploreText);
		txt.setTypeface(font);
		txt2.setTypeface(font);
		 StartSharing = (LinearLayout)findViewById(R.id.StartPlayLayout);
		 StartSharing.setOnClickListener(new View.OnClickListener() {
				@Override
		    	public void onClick(View v) {
		    		Intent intent = new Intent(MainActivity.this,SelectFiles.class);
	            	startActivity(intent); 
		    		}
			 });
	}
	
}
