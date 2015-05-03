package com.example.anton.emerge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class CameraInit extends Activity {

    private static final int REQUEST_CODE = 1;
    private Button button_1;
    public int TAKE_PICTURE = 1;
    private ImageView image_view;
    private static final int CAMERA_REQUEST = 1888;
    private static Uri mCapturedImageURI;
    private String selectedImagePath;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_camera);

            button_1 = (Button) findViewById(R.id.button1);

            button_1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bitmap photo = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");
            }
            //write the bytes in file

                MainActivity.inDB = true;
                Intent cameraIntent = new Intent(this, Congrats.class);
                cameraIntent.putExtra("BitmapImage", photo);
                startActivity(cameraIntent);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
