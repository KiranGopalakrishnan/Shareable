package com.example.shareable;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;  

public class SharedFiles extends Activity implements OnItemClickListener, OnPreparedListener {
	private String[] mAudioPath;
	public MediaPlayer mMediaPlayer;
	public static int[] songsArt;
	public static int MusicFileDuration;
	public static int duration;
	boolean   running;
    JSONObject jsonObject = new JSONObject();
    JSONArray SelectedSongsArray = new JSONArray();  
	
	int myProgress = 0;
	ProgressBar myProgressBar;
	CountDownTimer countDownTimer;
	
	 View ViewLoc;
	 Point p;
	
	 ListView listView;
	 ProgressBar progressbar;
	 List<MusicRowItem> rowItems;
	 Context context;    
	 SeekBar mSeekBarPlayer;
	    /*private view holder class*/
	    private class ViewHolder {
	        ImageView imageView;
	        TextView txtTitle;
	        TextView txtDesc;
	        TextView Di;
	    }
	 
	 public static String[] mMusicList;
	 public static final String[] descriptions = new String[] {
        "1234-5678-9101-9099",
        "5467-5398-9561-9923", "3654-9078-9231-1344",
        "6284-4620-4571-8945" };
public static String[] titles;

public static final Boolean[] defaultIcon_notify = {true,false,false,false};
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.select_shares);
	    context=getApplicationContext();
	    mMediaPlayer = new MediaPlayer();
	    //ListView mListView = (ListView) findViewById(R.id.listView1);
	    //songsArt=new int[]{R.drawable.default_music};
	    mMusicList = getAudioList();
	    //mMusicList.
	      
	   Button DoneButton = (Button) findViewById(R.id.DoneButton); 
		Typeface font = Typeface.createFromAsset(getAssets(), "lato_reg.ttf");  
		DoneButton.setTypeface(font);
		//ListTitle.setTypeface(font);
		this.getActionBar().hide();
		 DoneButton.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {

            		try {
						jsonObject.put("MusicList", SelectedSongsArray);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			    	    //handle case of no SDCARD present
			    	} else {
			    	    String dir = Environment.getExternalStorageDirectory()+File.separator+"Shareable/lists/";
			    	    //create folder
			    	    File folder = new File(dir); //folder name
			    	    File file = new File(dir, "ShareList.json");
			    	    if(folder.exists() == false){
			    	    	folder.mkdirs();
			    	    	try {
								file.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    	    } 
			    	   try {
			    		   	OutputStream outputStream = new FileOutputStream(file, false);
		    	            PrintStream outputStreamWriter = new PrintStream(outputStream);
							outputStreamWriter.print(jsonObject.toString());
		    	            outputStreamWriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	    
			    	        
			    	            
			    	    }
			    	//Intent intent = new Intent(SelectFiles.this,StartShare.class);
	                //startActivity(intent); 
			    	}		
			});
	    
	    mMediaPlayer.setOnPreparedListener(this);  
        
       
	    
	    rowItems = new ArrayList<MusicRowItem>();
	 //   Toast.makeText(getBaseContext(), String.valueOf(MusicFileDuration[0]) , Toast.LENGTH_LONG).show();
	     for (int i = 0; i < mMusicList.length; i++) {
	         
	    	 MusicRowItem item = new MusicRowItem(R.drawable.default_music,mMusicList[i],"Music Track",true);
	         rowItems.add(item);
	     }
//	    Toast.makeText(getBaseContext(), songsArt[0].toString() , Toast.LENGTH_LONG).show();
	    listView = (ListView) findViewById(R.id.listView1);
	    MusicCustomListViewAdapter adapter = new MusicCustomListViewAdapter(this,
	             R.layout.music_list, rowItems);  
			     listView.setAdapter(adapter);
			     listView.setOnItemClickListener(this);
			     
			     
	    
	    /*ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
	    android.R.layout.simple_list_item_1, mMusicList);
	    mListView.setAdapter(mAdapter);*/
	}
	
	private String[] getAudioList() {
	    final Cursor mCursor = getContentResolver().query(
	            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	            new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA}, null, null,
	            "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

	    int count = mCursor.getCount();

	    String[] songs = new String[count];
	    mAudioPath = new String[count];
	    int i = 0;
	    if (mCursor.moveToFirst()) {
	        do {
	            String SongTrack = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
	        	if(SongTrack.length() > 24 ){
	            songs[i] = (SongTrack).substring(0, 24).concat("...");
	        	}else
	        	{
	        		songs[i]=SongTrack;
	        	}
	            mAudioPath[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
	           // MusicFileDuration[i] = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
	            
	            i++;
	        } while (mCursor.moveToNext());
	    }   

	    mCursor.close();

	    return songs;
	}
	private void playSong(String path) throws IllegalArgumentException,
    IllegalStateException, IOException {

    Log.d("ringtone", "playSong :: " + path);

    mMediaPlayer.reset();
    mMediaPlayer.setDataSource(path);       
//mMediaPlayer.setLooping(true);
    mMediaPlayer.prepare();
    mMediaPlayer.start();
}
	/*public Bitmap getAlbumart(Long album_id) 
	   {
	        Bitmap bm = null;
	        try 
	        {
	            final Uri sArtworkUri = Uri
	                .parse("content://media/external/audio/albumart");

	            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

	            ParcelFileDescriptor pfd = getBaseContext().getContentResolver()
	                .openFileDescriptor(uri, "r");

	            if (pfd != null) 
	            {
	                FileDescriptor fd = pfd.getFileDescriptor();
	                bm = BitmapFactory.decodeFileDescriptor(fd);
	            }
	    } catch (Exception e) {
	    }
	    return bm;
	}*/
	   public void onItemClick(AdapterView<?> parent, View view, int position,
		         long id) {
		   try {
			   Log.d("12345", String.valueOf(SelectedSongsArray.length()));
	            if(SelectedSongsArray.length()>0){
	            	JSONArray TempDuplicateCheckerArray = SelectedSongsArray.getJSONArray(1);
	            	Log.d("jr",SelectedSongsArray.toString());
	 	           
	            	for(int i=0;i<TempDuplicateCheckerArray.length();i++)
	            	{
	            	
	            	Log.d("tdt", TempDuplicateCheckerArray.getString(i).toString());
	            	if(TempDuplicateCheckerArray.getString(i).equals(mAudioPath[position])){
	            		continue;
	            	}
	            	else
	            	{
	            		SelectedSongsArray.put(mAudioPath[position]);
	            		//jsonObject.put("MusicList", SelectedSongsArray);//Json Object contains all paths
	            	//parentJSON.put("List", jsonObject);
	            	}
	            }	
	            }
	            else
	            {
	            	SelectedSongsArray.put(mAudioPath[position]);
	            }
	            //Log.d("output", parentJSON.toString());
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
		
		   try {
			playSong(mAudioPath[position]);
			showPopup(SharedFiles.this, p);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	   private int getProgress() {
	        if (mMediaPlayer == null) {
	            return 0;
	        }
	        int ProgressPercentage=0;
	        int position = mMediaPlayer.getCurrentPosition();
	        int duration = mMediaPlayer.getDuration();
	        MusicFileDuration=duration;
	        //if (mProgress != null) {
	            if (duration > 0) {
	                // use long to avoid overflow
	                ProgressPercentage = (position / duration)*100;
	                //mProgress.setProgress( (int) pos);
	            }
	         	   return ProgressPercentage;
	    }
	   
	   
	   
	   private void showPopup(final Activity context, Point p) {
		   int popupWidth = 420;
		   int popupHeight = 150;
		 
		   // Inflate the popup_layout.xml
		   ViewHolder holder = null;
		   View convertView;
		   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		   LayoutInflater layoutInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View layout = layoutInflater.inflate(R.layout.play_video, viewGroup);
		   //layout.setBackgroundResource(R.drawable.seeker_color);
		   //convertView = layoutInflater.inflate(R.layout.popup_player, null);
		   this.mSeekBarPlayer = (SeekBar) layout.findViewById(R.id.seekBar1);
		   running = true;
		   mSeekBarPlayer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {  
		         @Override  
		         public void onStopTrackingTouch(SeekBar seekBar) {}  
		         @Override  
		         public void onStartTrackingTouch(SeekBar seekBar) {}  
		         @Override  
		         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {  
		             if(fromUser){  
		                 mMediaPlayer.seekTo(progress);  
		                 updateTime();  
		             }  
		         }  
		     });  
		    
		  /* holder = new ViewHolder();
           holder.Di = (TextView) findViewById(R.id.TimeRemaining); 
           holder.Di.setText(String.valueOf((MusicFileDuration/60000)/60));*/
		   //(TextView) findViewById(R.id.TimeRemaining);
		   // Creating the PopupWindow
		   final PopupWindow popup = new PopupWindow(context);
		   popup.setContentView(layout);
		   popup.setWidth(popupWidth);
		   popup.setHeight(popupHeight);
		   popup.setFocusable(true);
		   
		   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
		   int OFFSET_X = 100;
		   int OFFSET_Y = 50;
		 
		   // Clear the default translucent background
		   //popup.setBackgroundDrawable(new BitmapDrawable());
		 
		   // Displaying the popup at the specified location, + offsets.
		  popup.showAtLocation(layout, Gravity.CENTER,Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL);
		 
		  
		}
	    //mSeekBarPlayer.setProgress(50);
	  final Runnable onEverySecond = new Runnable() {  
	        @Override  
	        public void run(){    
	              mSeekBarPlayer.setProgress(mMediaPlayer.getCurrentPosition());  
	               
	             if(mMediaPlayer.isPlaying()) {  
	              mSeekBarPlayer.postDelayed(onEverySecond, 1000);  
	              //updateTime();  
	             }  
	           
	        }  
	    };  
	   private void updateTime(){  
		      do {  
		            int current = mMediaPlayer.getCurrentPosition();  
		            System.out.println("duration - " + duration + " current- "  
		                    + current);  
		            int dSeconds = (int) (duration / 1000) % 60 ;  
		            int dMinutes = (int) ((duration / (1000*60)) % 60);  
		            int dHours   = (int) ((duration / (1000*60*60)) % 24);  
		              
		            int cSeconds = (int) (current / 1000) % 60 ;  
		            int cMinutes = (int) ((current / (1000*60)) % 60);  
		            int cHours   = (int) ((current / (1000*60*60)) % 24);  
		              
		           
		              
		            try{  
		                Log.d("Value: ", String.valueOf((int) (current * 100 / duration)));  
		                if(mSeekBarPlayer.getProgress() >= 100){  
		                    break;  
		                }  
		            }catch (Exception e) {}  
		        }while (mSeekBarPlayer.getProgress() <= 100);  
		    }  
		      
		 public void onPrepared(MediaPlayer arg0) {  
		  // TODO Auto-generated method stub  
		  duration = mMediaPlayer.getDuration();  
		  mSeekBarPlayer.setMax(duration);  
		  mSeekBarPlayer.postDelayed(onEverySecond, 1000);  
		 } 
	   
}
