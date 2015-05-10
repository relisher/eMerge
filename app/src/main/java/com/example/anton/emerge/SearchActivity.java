package com.example.anton.emerge;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kairos.KairosListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Josh on 5/3/2015.
 */
public class SearchActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_home_page);

            // Create an instance of the KairosListener
            KairosListener listener = new KairosListener() {

                @Override
                public void onSuccess(String response) {
                    Toast.makeText(getApplicationContext(), "Loading...",
                            Toast.LENGTH_LONG).show();
                    Log.d("Joe",response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        String fbId = obj.getJSONArray("images").getJSONObject(0).getJSONObject("transaction").getString("subject");
                        Log.d("Joe", fbId);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/"+fbId));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.chrome");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            // Chrome browser presumably not installed so allow user to choose instead
                            intent.setPackage(null);
                            startActivity(intent);
                        }
                    }catch(JSONException e){
                        Log.d("Joe",e.toString());
                        Toast.makeText(getApplicationContext(), "No match found, please try again",
                                Toast.LENGTH_LONG).show();
                    }


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
                String galleryId = "friends1";
                String selector = "FULL";
                String threshold = "0.70";
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
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            //homeIntent.putExtra("picURI", "x");
            //startActivityForResult(homeIntent, 6);
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
        Resources res = getResources();
        // set authentication
        String app_id = res.getString(R.string.kairos_app_id);
        String api_key = res.getString(R.string.kairos_app_key);
        MainActivity.myKairos.setAuthentication(this, app_id, api_key);

        // Logs 'install' and 'app activate' App Events.
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
    }


}
