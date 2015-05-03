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
import android.widget.Button;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by anton on 5/2/2015.
 */
public class Congrats extends Activity {
    private Button button_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_congrats);



    //Kairos code
    Intent intent = getIntent();
    Bitmap image = (Bitmap) intent.getParcelableExtra("BitmapImage");
    String subjectId = MainActivity.userId;
    String galleryId = "friends1";
    String selector = "FULL";
    String multipleFaces = "false";
    String minHeadScale = "0.25";

        // Create an instance of the KairosListener
        KairosListener listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
                Toast.makeText(getApplicationContext(), "THIS BITCH WORKED!!! =) " + MainActivity.userLink.toString(),
                        Toast.LENGTH_LONG).show();

/* chrome navigation
                Intent intent = new Intent(Intent.ACTION_VIEW, MainActivity.userLink);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    startActivity(intent);
                }
*/
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
                Toast.makeText(getApplicationContext(), "THIS BITCH FAILED!!! =)",
                        Toast.LENGTH_LONG).show();
            }
        };
try
{
    MainActivity.myKairos.enroll(image, subjectId, galleryId, null, null, null, listener);
}
catch(JSONException e1){}catch(UnsupportedEncodingException e){}

}

    public void addDonation(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

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
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
