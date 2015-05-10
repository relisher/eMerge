package com.example.anton.emerge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.IOException;

public class Gallery extends Activity {


    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Log.d("Joe",data.getData().getPath());
                Toast.makeText(getApplicationContext(), "filler",
                        Toast.LENGTH_LONG).show();
                KairosListener listener = new KairosListener() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getApplicationContext(), "Success " + s,
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Gallery.this, HomePage.class);
                        startActivity(i);

                    }

                    @Override
                    public void onFail(String s) {
                        Toast.makeText(getApplicationContext(), "Failure " + s,
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Gallery.this, HomePage.class);
                        startActivity(i);
                    }
                };

                Bitmap image = null;
                try {
                    image = BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(data.getData()));
                } catch (IOException e) {
                    Log.d("Joe", e.toString());
                }
                if (image == null) {
                    Toast.makeText(getApplicationContext(), "Image DNE",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                LinearLayout v = new LinearLayout(this);
                ImageView ii = new ImageView(this);
                ii.setImageBitmap(image);
                v.addView(ii);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(v);
                toast.show();

                String galleryId = "friends1";
                String maxNumResults = "25";
                try {
                    MainActivity.myKairos.recognize(image,
                            galleryId,
                            null,
                            null,
                            null,
                            maxNumResults,
                            listener);
                } catch (IOException e) {
                    Log.d("Joe", e.toString());
                } catch (JSONException j) {
                    Log.d("Joe", j.toString());
                }
            }
        }
    }
}




