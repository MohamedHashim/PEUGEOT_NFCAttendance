package com.NFCFZLLE.eventmanagement.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.google.gson.reflect.TypeToken;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.network.InvokeWebserviceVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad Tahir Ashraf on 26/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class BeaconService extends Service implements ValueCallback<Attendees>{

    private BeaconManager beaconManager;
    private com.estimote.sdk.Region region;
    List<String> checkedInBeaconIds = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(1000,1000);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(com.estimote.sdk.Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    for(int i=0;i<list.size();i++) {
                        Beacon nearestBeacon = list.get(i);
                        // UUID:Major:Minor
                        String beaconId = nearestBeacon.getProximityUUID().toString() + ":" + nearestBeacon.getMajor() + ":" + nearestBeacon.getMinor();
                        if (!checkedInBeaconIds.contains(beaconId)) {
                            initCheckInService(beaconId);

                        }
                    }
                        Log.d("", "BeaconFound List Size: " + checkedInBeaconIds.size());

                }
            }
        });
        region = new com.estimote.sdk.Region("ranged region", null, null, null);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

    }

    private void initCheckInService(String beaconId) {


        Map<String,String> map = new HashMap<>();
        map.put("beacon_id",beaconId);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.ATTENDEE_CHECK_IN);
        params.setRequestKey(RequestKeys.CHECK_IN_REQUEST_BY_BEACON_ID);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<Attendees>() {
        }.getType());

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this,this,params);
        volleyRequest.execute();

    }

    @Override
    public void onDestroy() {
        beaconManager.stopRanging(region);
        super.onDestroy();
    }

    @Override
    public void onReceiveValue(Attendees value, String beaconId,RequestKeys requestKey) {
        Log.d("","beacon Response: "+beaconId);
        checkedInBeaconIds.add(beaconId);
        Log.d("", "Attendee Checked in: " + value.toString());

    }

    @Override
    public void onReceiveError(String error,RequestKeys requestKey) {
        Log.d("","beacon Response: "+error);
    }
}
