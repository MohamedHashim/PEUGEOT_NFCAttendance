package com.NFCFZLLE.eventmanagement.interfaces;

/**
 * Created by Muhammad Tahir Ashraf on 23/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public interface NFCListener<T> {

    public void onRead(T object);
    public void onReadError(String error);

}
