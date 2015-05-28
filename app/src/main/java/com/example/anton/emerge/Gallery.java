package com.example.anton.emerge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

public class Gallery extends Activity {

    public static final String PACKAGE_NAME = "com.example.anton.emerge";

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/eMerge/";

    //gallery load variables
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICK_FROM_GALLERY = 2;
    Bitmap thumbnail = null;
    String picturePath;
    Bitmap bitmap;

    public static final String lang = "eng";
    private static final String TAG = "Gallery.java";

    // protected ImageView _image;
    protected EditText _field;
    protected String _path;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN = "photo_taken";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGalleryActivity();
    }

    ///////////////////////////////////////////////////////////////////////////

    // Simple android photo capture:
    // http://labs.makemachine.net/2010/03/simple-android-photo-capture/

    protected void startGalleryActivity() {

        Intent in = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(in, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            //raw image
            thumbnail = (BitmapFactory.decodeFile(picturePath));

        }

        LoadData task = new LoadData();
        task.execute();
    }

    protected void onPhotoPicked() {
        Log.e(TAG, "PHOTO WAS PICKED");
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        bitmap = BitmapFactory.decodeFile(picturePath, options);

        try {
            ExifInterface exif = new ExifInterface(picturePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            if (rotate != 0) {

                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }

            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        } catch (IOException e) {
            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
        }

        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra("BitmapImage", bitmap);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    public class LoadData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
            progressDialog= ProgressDialog.show(Gallery.this, "Processing","Processing please wait...", true);

            //do initialization of required objects objects here
        };
        @Override
        protected Void doInBackground(Void... params)
        {
            onPhotoPicked();
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();
        };
    }





}


























    /*

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
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    }

                    @Override
                    public void onFail(String s) {
                        Toast.makeText(getApplicationContext(), "Failure " + s,
                                Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Gallery.this, HomePage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
*/




