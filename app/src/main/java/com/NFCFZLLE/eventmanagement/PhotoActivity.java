package com.NFCFZLLE.eventmanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    ImageView imageView;
    String front_bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imageView = (ImageView) findViewById(R.id.imageView);
        File img_front = new File("sdcard/camera_app/front_side.jpg");
        if (img_front.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(img_front.getAbsolutePath());
            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            front_bitmap = Base64.encodeToString(byte_arr, 0);
            imageView.setImageBitmap(bitmap);
        }
    }
}
