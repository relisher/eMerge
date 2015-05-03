package com.example.anton.emerge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;


public class Camera extends Activity {

    private static final int REQUEST_CODE = 1;
    private Button button_1;
    public int TAKE_PICTURE = 1;
    private ImageView image_view;
    private static final int CAMERA_REQUEST = 1888;
    //String fileName = "person.jpg";
    private static Uri mCapturedImageURI;
    private String selectedImagePath;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bitmap photo = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");
            }
                Intent searchActivity = new Intent(this, SearchActivity.class);
                searchActivity.putExtra("BitmapImage", photo);
                startActivity(searchActivity);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
