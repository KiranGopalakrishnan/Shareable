package com.example.shareable;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShareRoom extends Activity implements OnItemClickListener {
	Typeface font;
	 ListView listView;
	 List<RowItem> rowItems;
	 List<ScanResult> AvailableLeechers;
	     

	 public static String[] AvailableLeechersArray;

 public static final String[] descriptions = new String[] {
         "1234-5678-9101-9099",
         "5467-5398-9561-9923", "3654-9078-9231-1344",
         "6284-4620-4571-8945" };

 public static final Integer[] images = { R.drawable.profile,
         R.drawable.profile, R.drawable.profile, R.drawable.profile };
 
 public static final Boolean[] defaultIcon_notify = {true,false,false,false};


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shareable_room);
		TextView txt = (TextView) findViewById(R.id.SearchText); 
		Button DoneButton = (Button) findViewById(R.id.DoneButton); 
		font = Typeface.createFromAsset(getAssets(), "lato_reg.ttf");  
		txt.setTypeface(font);
		DoneButton.setTypeface(font);
		WifiConfiguration wifiConfig = new WifiConfiguration();
		wifiConfig.SSID = String.format("\"%s\"", "shareable");
		wifiConfig.preSharedKey = String.format("\"%s\"", "ShareableByKiran@1994");

		WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		wifiManager.startScan();
		AvailableLeechers = wifiManager.getScanResults();
		
		AvailableLeechersArray = new String[AvailableLeechers.size()];
		if(AvailableLeechers.size()> 0)
		{
			for(int i=0; i < AvailableLeechers.size(); i++)
			{
				AvailableLeechersArray[i] = AvailableLeechers.get(i).SSID.toString();
			}
		}
		else
		{
			AvailableLeechersArray[0]="No Leechers Found . . .";
		}
		
		this.getActionBar().hide();
		 DoneButton.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	Intent intent = new Intent(ShareRoom.this,SelectFiles.class);
	                startActivity(intent); 
			    	}		
			});
		
		
		
		 rowItems = new ArrayList<RowItem>();
	     for (int i = 0; i < AvailableLeechersArray.length; i++) {
	         RowItem item = new RowItem(images[i], AvailableLeechersArray[i], descriptions[i],defaultIcon_notify[i]);
	         rowItems.add(item);
	     }
	     listView = (ListView) findViewById(R.id.listView1);
	     CustomListViewAdapter adapter = new CustomListViewAdapter(this,
	             R.layout.list_item, rowItems);  
			     listView.setAdapter(adapter);
	     listView.setOnItemClickListener(this);
	     //listView.setChoiceMode(2);
	    	
	}
	
	     public void onItemClick(AdapterView<?> parent, View view, int position,
         long id) {
	    	 //Intent intent = new Intent(.this,AddNewCard.class);
             //startActivity(intent); 
	    	 TextView AddCount = (TextView) findViewById(R.id.AddedCount);
	    	 CharSequence AddCountString = AddCount.getText();
	    	 int AddCountInteger = Integer.valueOf(AddCountString.toString());
	    	int AddedCount = AddCountInteger+1;
	    	AddCount.setText(String.valueOf(AddedCount));
 }
}
