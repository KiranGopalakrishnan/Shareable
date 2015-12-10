package com.example.shareable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class VideoPlayer extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		VideoView videoHolder = (VideoView) findViewById(R.id.videoScreen);
		final LinearLayout controlHolder= (LinearLayout) findViewById(R.id.ControlLayout);
		videoHolder.setVideoPath(Environment.getExternalStorageDirectory() + "/video/video.mp4");
		//videoHolder.
		videoHolder.start();
		videoHolder.setOnTouchListener(new View.OnTouchListener() {	  
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				controlHolder.bringToFront();
				return false;
			}		
		});
	}
}
