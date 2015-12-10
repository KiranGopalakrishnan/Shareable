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
import java.util.Arrays;
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
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;  

public class SelectFiles extends Activity implements OnItemClickListener {
	private String[] mAudioPath;
	public MediaPlayer mMediaPlayer;
	public static int[] songsArt;
	public static int MusicFileDuration;
	public static int duration;
	boolean   running;
	Typeface font;
	JSONArray ArrayJSON = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    String[] SelectedSongsArray;
    ArrayList<String> TempMusicArray = new ArrayList();
    ArrayList<String> TempMusicArray2 = new ArrayList();
    int ItemClickCount;
	
	int myProgress = 0;
	ProgressBar myProgressBar;
	CountDownTimer countDownTimer;
	
	 View ViewLoc;
	 Point p;
	
	 ListView listView;
	 ProgressBar progressbar;
	 List<MusicRowItem> rowItems;
	 Context context;    
	 
	 
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
	    mMusicList = getAudioList();
	    SelectedSongsArray = new String[mMusicList.length];
	    ItemClickCount = 0;
	    Button DoneButton = (Button) findViewById(R.id.DoneButton); 
		font = Typeface.createFromAsset(getAssets(), "lato_reg.ttf");  
		DoneButton.setTypeface(font);
		this.getActionBar().hide();
		 DoneButton.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	if(TempMusicArray.size()>0)
			    	{
			    		for(int i=0; i< TempMusicArray.size(); i++)
			    		{
			    			for(int j=0; j< TempMusicArray.size(); j++)
				    		{
			    				if(!TempMusicArray.get(i).equals(TempMusicArray.get(j)))
			    				{
			    					TempMusicArray2.add(TempMusicArray.get(i));
			    				}
				    		}
			    		}
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
			    		    SelectedSongsArray=new String[TempMusicArray.size()];
			    		    for(int i = 0; i < SelectedSongsArray.length; i++)
			    		    {
			    		    	SelectedSongsArray[i] = TempMusicArray.get(i);
			    		    }
			    		   	JSONArray jsonArray = new JSONArray(Arrays.asList(SelectedSongsArray));
			    		   	try {
								jsonObject.put("MusicList", jsonArray);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    		   	OutputStream outputStream = new FileOutputStream(file, false);
		    	            PrintStream outputStreamWriter = new PrintStream(outputStream);
							outputStreamWriter.print(jsonArray.toString());
		    	            outputStreamWriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	    
			    	}
			    	
			    }		
			
		 });
	    
       
	    
	    rowItems = new ArrayList<MusicRowItem>();
	 //   Toast.makeText(getBaseContext(), String.valueOf(MusicFileDuration[0]) , Toast.LENGTH_LONG).show();
	     for (int i = 0; i < mMusicList.length; i++) {
	         
	    	 MusicRowItem item = new MusicRowItem(R.drawable.default_music,mMusicList[i],"Music Track",true);
	         rowItems.add(item);
	     }
	    listView = (ListView) findViewById(R.id.listView1);
	    MusicCustomListViewAdapter adapter = new MusicCustomListViewAdapter(this,
	             R.layout.music_list, rowItems);
	    		 listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			     listView.setAdapter(adapter);
			     listView.setOnItemClickListener(this);
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
		   public void onItemClick(AdapterView<?> parent, View view, int position,
		         long id) {
			   		   
					   View setSelected = parent.findViewById(R.id.SelectedIcon);
					   setSelected.setVisibility(View.VISIBLE);
					   //TextView SelectSelected = (TextView) setSelected;
					   //SelectSelected.setText("Selected");
					   //setSelected.setTypeface(font);
					  // SelectSelected.setBackgroundResource(R.drawable.notify_pairing);
					   //SelectSelected.setVisibility(View.VISIBLE);
					   //TempMusicArray.add(mAudioPath[position]);
	}	   
	   
}
