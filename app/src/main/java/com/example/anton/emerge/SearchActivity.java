package com.example.anton.emerge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Josh on 5/3/2015.
 */
public class SearchActivity extends ActionBarActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_home_page);

            // Create an instance of the KairosListener
            KairosListener listener = new KairosListener() {

                @Override
                public void onSuccess(String response) {
                    Log.d("Joe",response);
                }

                @Override
                public void onFail(String response) {
                    // your code here!
                    Log.d("KAIROS DEMO", response);
                }
            };


            try {

                // Fine-grained Example:
                // This example uses a bitmap image and also optional parameters
                Intent intent = getIntent();
                Bitmap image = (Bitmap) intent.getParcelableExtra("BitmapImage");
                String galleryId = "friends";
                String selector = "FULL";
                String threshold = "0.75";
                String minHeadScale = "0.25";
                String maxNumResults = "25";
                MainActivity.myKairos.recognize(image,
                        galleryId,
                        selector,
                        threshold,
                        minHeadScale,
                        maxNumResults,
                        listener);

            } catch (JSONException e1) {
            } catch (UnsupportedEncodingException e) {
            }
            Intent homeIntent = new Intent(this, HomePage.class);
            homeIntent.putExtra("picURI", "x");
            startActivityForResult(homeIntent, 6);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean addFriend(View v){return true;}

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
        // Logs 'install' and 'app activate' App Events.
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
    }

}
