package com.example.zachet2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.ObjectsCompat;

public class MainActivity extends AppCompatActivity {

    private final int Pick_Image = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
        Button pickImage = findViewById(R.id.button14);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("*/*");
                startActivityForResult(photoPickerIntent, Pick_Image);
            }
        });

        Button saveImage = findViewById(R.id.button15);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveImage().execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_Image:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        ImageView imageView = findViewById(R.id.imageView2);
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

   public class SaveImage extends AsyncTask {
       @Override
       protected Object doInBackground(Object[] objects) {
           ImageView iv = findViewById(R.id.imageView2);
           FileOutputStream fOut = null;
           try {
               try {
                   File myFile = new File(Environment.getExternalStorageDirectory(), "image.jpg");
                   myFile.createNewFile();
                   BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                   Bitmap bitmap = drawable.getBitmap();
                   fOut = new FileOutputStream(myFile);
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 99, fOut);
               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   if (fOut != null){
                       fOut.close();
                   }
               }
           }catch (Exception e){
               e.printStackTrace();
           }
           return null;
       }
   }
}
