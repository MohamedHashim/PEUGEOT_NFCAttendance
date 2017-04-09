package com.NFCFZLLE.eventmanagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Muhammad Tahir Ashraf on 23/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class Helper {

    private static final String cacheName = "EventManagement";

    public static void checkIfNfcAvaialable(NfcAdapter mNfcAdapter,Context mContext,boolean finishActivity) {
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(mContext, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            if(finishActivity)
                ((Activity)mContext).finish();
            return;

        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(mContext, "Enable NFC from Settings.", Toast.LENGTH_LONG).show();
        } else {

        }

    }

    public static void saveUser(User user, Context mContext) {
        Log.d("", "saving User: " + user.toString());
        String json = new Gson().toJson(user, User.class);
        addPreference(Globals.PreferenceKeys.USER_OBJ, json, mContext);
    }

    public static void addPreference(String key, String value, Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(cacheName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static User getUser(Context mContext) {
        String json = getPreferenceByKey(Globals.PreferenceKeys.USER_OBJ, mContext);
        if (json == null)
            return null;

        User user = new Gson().fromJson(json, User.class);
        return user;
    }

    public static String getPreferenceByKey(String key, Context ctx) {
        if (ctx == null) {
            Log.d("", "context is null");
            return null;
        }
        SharedPreferences settings = ctx.getSharedPreferences(cacheName, 0);
        return settings.getString(key, null);

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static String getPathFromBitmap(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    }


    public static String convertImageToBase64(Context context,String path) {

        String base64 = "";
        try
        {
            Uri uri = Uri.parse(path);

            String filePath = getPath(uri, context);

            File f = new File(filePath);

//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 1;

//			Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            Bitmap bm = decodeSampledBitmapFromResource(context, f, 480, 480);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            //added lines
            byte[] b = baos.toByteArray();
            base64 = Base64.encodeToString(b, Base64.DEFAULT);
//			base64 = Base64.encodeToString(b, Base64.DEFAULT);
           /* if(base64.length()>60000){
                base64 = base64.substring(0,59999);
            }*/
            Log.d("", "base64 length: "+base64.length());
            Log.d("", "base64: "+base64);



        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return base64;
    }

    public static String getPath(Uri uri, Context context)
    {
        try
        {
            String[] projection = { MediaStore.MediaColumns.DATA };
            Cursor cursor = context.getContentResolver().query(uri, projection,
                    null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }catch(Exception ex)
        {
            return "";
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Context context,File f2,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        try {


            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f2), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            Log.d("", "inSampleSize: "+options.inSampleSize);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            return  BitmapFactory.decodeStream(new FileInputStream(f2), null, options);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        Log.d("", "image Height: "+height);
        Log.d("", "image width: "+width);

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void logOutUser(Context mContext) {
        addPreference(Globals.PreferenceKeys.USER_OBJ, null, mContext);
    }



}
