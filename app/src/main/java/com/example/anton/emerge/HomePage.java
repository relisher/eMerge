package com.example.anton.emerge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.kairos.KairosListener;

/**
 * Created by Josh on 5/2/2015.
 */
public class HomePage extends Activity {
    KairosListener listener;
    TextView profile;
    Bitmap image;
    public static boolean enroll = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        profile = (TextView) findViewById(R.id.profile);
        profile.setText(MainActivity.firstName + " " + MainActivity.lastName);

        // Create an instance of the KairosListener
       listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }
        };
    }



    public void addFriend(View view){
        Intent i = new Intent(this, Camera.class);
        startActivity(i);
    }

   public void galleryFriend(View view){
       Intent i = new Intent(this, Gallery.class);
       startActivity(i);
   }

    public void updatePhoto(View view){
        enroll = true;
        Intent cam = new Intent(this, Camera.class);
        startActivity(cam);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Resources res = getResources();
        // set authentication
        String app_id = res.getString(R.string.kairos_app_id);
        String api_key = res.getString(R.string.kairos_app_key);
        MainActivity.myKairos.setAuthentication(this, app_id, api_key);



        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }
}
