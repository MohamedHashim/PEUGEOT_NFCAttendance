package com.NFCFZLLE.eventmanagement;

import android.app.Application;
import android.content.Intent;

import com.NFCFZLLE.eventmanagement.services.BeaconService;
import com.NFCFZLLE.eventmanagement.utils.Helper;

/**
 * Created by Muhammad Tahir Ashraf on 26/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class NFCApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        if(Helper.getUser(this)!=null)
        startService(new Intent(this, BeaconService.class));

    }

    public void StopBeaconService(){
        stopService(new Intent(this, BeaconService.class));
    }

}
