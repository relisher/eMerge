package com.example.anton.emerge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;


public class Camera extends Activity {

    private static final int REQUEST_CODE = 1;
    private Button button_1;
    public int TAKE_PICTURE = 1;
    private ImageView image_view;
    private static final int CAMERA_REQUEST = 1888;
    //String fileName = "person.jpg";
    private static Uri mCapturedImageURI;
    private String selectedImagePath;
    KairosListener listener;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            cameraIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        // Create an instance of the KairosListener
        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                Intent hp = new Intent(Camera.this, HomePage.class);
                hp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(hp);
                Log.d("KAIROS DEMO", response);
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Toast.makeText(getApplicationContext(), "Failed: " + response,
                        Toast.LENGTH_LONG).show();
                Log.d("KAIROS DEMO", response);
            }
        };

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bitmap photo = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");
            }
            else return;

            if(HomePage.enroll){

                Bitmap image = photo;
                try
                {
                    String subjectId = MainActivity.userId;
                    String galleryId = "friends1";
                    MainActivity.myKairos.enroll(image, subjectId, galleryId, null, null, null, listener);
                }
                catch(JSONException e1){}catch(UnsupportedEncodingException e){}
            }
        else {
                Intent searchActivity = new Intent(this, SearchActivity.class);
                searchActivity.putExtra("BitmapImage", photo);
                startActivity(searchActivity);
            }
            HomePage.enroll = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
