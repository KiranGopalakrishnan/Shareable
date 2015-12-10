package com.example.shareable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class StartShare extends Activity{

	private WebServer server;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_started);
        server = new WebServer();
        try {
            server.start();
        } catch(IOException ioe) {
            Log.w("Httpd", "The server could not start.");
        }
        Log.w("Httpd", "Web server initialized.");
        File root = Environment.getExternalStorageDirectory();
       // Toast.makeText(getApplicationContext(), (root.getAbsolutePath() + "/www/index.html").toString(), Toast.LENGTH_LONG).show();
    }


    // DON'T FORGET to stop the server
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (server != null)
            server.stop();
    }

    private class WebServer extends NanoHTTPD {

        public WebServer()
        {
            super(8080);
        }

        @Override
        public Response serve(String uri, Method method, 
                              Map<String, String> header,
                              Map<String, String> parameters,
                              Map<String, String> files) {
        	//for(int i=0; i<3; i++)
        	//{
        	String answer = "";
        	
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(Environment.getExternalStorageDirectory()
                        + "/www/");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return new NanoHTTPD.Response(Status.OK, "audio/mpeg", fis);        }
        //}
    }
    

}
