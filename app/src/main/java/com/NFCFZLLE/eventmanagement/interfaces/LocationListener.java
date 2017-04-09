package com.NFCFZLLE.eventmanagement.interfaces;

import android.location.Location;

/**
 * Created by Muhammad Tahir Ashraf on 04/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public interface LocationListener {

    void onLocationReceived(Location location);
    void onLocationFailed(String reason);

}
