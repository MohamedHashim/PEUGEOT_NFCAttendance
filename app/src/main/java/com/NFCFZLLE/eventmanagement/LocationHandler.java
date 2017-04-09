package com.NFCFZLLE.eventmanagement;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.NFCFZLLE.eventmanagement.interfaces.LocationListener;

import java.lang.ref.WeakReference;

/**
 * Created by Muhammad Tahir Ashraf on ${DATE}.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */

public class LocationHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    private static LocationHandler mLocationHandler;
    private WeakReference<Context> mActivity;
    private LocationListener onLocationReceived;
    //aHMED

    //Public Properties
    private Location initialLocation;

    public Location getInitialLocation() {
        return initialLocation;
    }

    public void setInitialLocation(Location location) {
        this.initialLocation = location;
    }

    public LocationListener getOnLocationReceived() {
        return onLocationReceived;
    }

    public void setOnLocationReceived(LocationListener onLocationReceived) {
        this.onLocationReceived = onLocationReceived;
    }

    private LocationHandler(Context context) {
        this.mActivity = new WeakReference<Context>(context);
    }


    private void initialize() {

        if (mActivity.get() != null) {
            if(isGpsEnabled()) {
                mGoogleApiClient = new GoogleApiClient.Builder(mActivity.get())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }
        } else {
            Log.d("LocationHandler", "Activity has been collected");
        }
    }

    public boolean isGpsEnabled() {
        if (mActivity.get() != null) {
            LocationManager manager = (LocationManager) mActivity.get().getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return false;
            } else {
                return true;
            }
        } else {
            Log.d("LocationHandler", "Activity has been collected");
        }
        return false;
    }

    public static synchronized LocationHandler getInstance(Context activity) {
        if (mLocationHandler == null) {
            mLocationHandler = new LocationHandler(activity);
        }
        return mLocationHandler;
    }

    public void requestNewLocation()
    {
        if(mGoogleApiClient!=null) //this might be null if during the time of initialization of this handler , the gps was off.
            mGoogleApiClient.connect();
        else
        {
            //reinitialize
            initialize();
        }

    }
    @Override
    public void onConnected(Bundle bundle) {
        initialLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(initialLocation!=null) {
            if (onLocationReceived != null) {
                onLocationReceived.onLocationReceived(initialLocation);
            }

        }else
        {
            if (onLocationReceived != null) {
                onLocationReceived.onLocationFailed("Location Unavailable.");
            }
        }
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (onLocationReceived != null)
            onLocationReceived.onLocationFailed("");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (onLocationReceived != null)
            onLocationReceived.onLocationFailed(connectionResult.toString());
    }
}
