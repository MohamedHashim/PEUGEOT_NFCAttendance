package com.NFCFZLLE.eventmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.NFCFZLLE.eventmanagement.utils.Helper;

/**
 * Created by Muhammad Tahir Ashraf on 11/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class SplashScreen extends BaseActivity {

    ImageView logo;
    private static final int REQUEST_CODE_GPS = 1234;
    private boolean shouldFinish = false;

    @Override
    public void onStop() {
        super.onStop();
        if (shouldFinish) {
            finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        themeColor = R.color.login_bg;
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash);

        logo = (ImageView)findViewById(R.id.logo);

        LocationHandler mhandler = LocationHandler.getInstance(this);
        if (mhandler.isGpsEnabled()) { //if gps is enabled get new location.
            inithandler();
        }else
            checkIfLocationEnabled();




    }

    private void checkIfLocationEnabled() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please Enable your Location");

        alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPS);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void inithandler() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Helper.getUser(SplashScreen.this) != null) {
                    startActivity(new Intent(SplashScreen.this, Peugeot_profile_activity.class));
                } else
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
//                }
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GPS) {
            LocationHandler mhandler = LocationHandler.getInstance(this);
            if (mhandler.isGpsEnabled()) { //if gps is enabled get new location.
                inithandler();
            }else {
                Toast.makeText(this, "Please enable Location to continue", Toast.LENGTH_SHORT).show();
                checkIfLocationEnabled();
            }
        }
    }



}
