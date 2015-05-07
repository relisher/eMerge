package com.example.anton.emerge;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;

import com.kairos.KairosListener;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;


/**
 * Created by arelin on 5/6/15.
 */
public class Gallery extends Activity {

    private static Uri mCapturedImageURI;
    private String selectedImagePath;
    KairosListener listener;
    private Uri outputFileUri;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageView iv;




    Uri myPicture = null;
    Button buttonLoadImage;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);

        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                Intent hp = new Intent(Gallery.this, HomePage.class);
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
        Bitmap photo = null;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            cursor.close();

            BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
            Bitmap image = drawable.getBitmap();

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

}




