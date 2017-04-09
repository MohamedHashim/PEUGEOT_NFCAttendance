package com.NFCFZLLE.eventmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.NFCFZLLE.eventmanagement.animations.AnimationPageTwo;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.network.InvokeWebserviceVolley;
import com.NFCFZLLE.eventmanagement.utils.Messages;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Muhammad Tahir Ashraf on 13/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class ScanQR extends BaseActivity implements ValueCallback<Attendees> {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1122;
    private ImageView pageTwoDeviceHand;
    private Attendees attendees;
    private boolean requestedToCheckIn;
    ProgressBar mProgress;
    String checkin_url = "http://ae.nfcvalet.com/mobile/Test?";
    String name, mail, nfc_id, event_title, company, phone_number, table, photo;
    private String attendeeId;
    TextView UID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        themeColor = R.color.intro2_bg;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);
        AnimationPageTwo.getInstance().destroyInstance();
        initPageTwoAnimation();

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        UID = (TextView) findViewById(R.id.UID);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestedToCheckIn = true;
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP_MR1 && ContextCompat.checkSelfPermission(ScanQR.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ScanQR.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);


                } else {
                    initScanner();
                }

            }
        });
//
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestedToCheckIn = false;
//
//                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP_MR1 && ContextCompat.checkSelfPermission(ScanQR.this,
//                        Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(ScanQR.this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//
//
//                } else {
//                    initScanner();
//                }
//
//                /*Intent intent = new Intent(ScanQR.this, CheckInCheckOutActivity.class);
//                intent.putExtra(CheckInCheckOutActivity.isCheckinFlag, false);
//                intent.putExtra(CheckInCheckOutActivity.ATTENDEE_KEY, attendees);
//                startActivity(intent);*/
//            }
//        });


    }

    private void initPageTwoAnimation() {

        pageTwoDeviceHand = (ImageView) findViewById(R.id.device_hand);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationPageTwo.getInstance().initAnimation(pageTwoDeviceHand, new WeakReference<Context>(ScanQR.this));
            }
        }, 300);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initScanner();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void initScanner() {

        IntentIntegrator integrator = new IntentIntegrator(ScanQR.this);
        integrator.setPrompt("Scan a QR-Code");
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setCameraId(-1);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                String id = result.getContents();
                attendeeId = id;
                UID.setText(id);
//                if(requestedToCheckIn)
                initCheckInRequest(id);
//                else
//                    initCheckOutRequest(id);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initCheckInRequest(String id) {

        mProgress.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("qr_code_attendee", "" + id);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.ATTENDEE_CHECK_IN);
        params.setRequestKey(RequestKeys.CHECK_IN_REQUEST_BY_QR_ID);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<Attendees>() {
        }.getType());

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this, this, params);
        volleyRequest.execute();


    }

    private void initCheckOutRequest(String id) {
        mProgress.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("qr_code_attendee", "" + id);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.ATTENDEE_CHECK_OUT);
        params.setRequestKey(RequestKeys.CHECK_OUT_REQUEST_BY_QR_ID);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<Attendees>() {
        }.getType());

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this, this, params);
        volleyRequest.execute();

    }

    @Override
    public void onReceiveValue(Attendees value, String message, RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
        if (requestKey == RequestKeys.CHECK_IN_REQUEST_BY_QR_ID) {
            Intent intent = new Intent(ScanQR.this, CheckInCheckOutActivity.class);
            intent.putExtra(CheckInCheckOutActivity.isCheckinFlag, true);
            intent.putExtra(CheckInCheckOutActivity.ATTENDEE_KEY, value);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ScanQR.this, CheckInCheckOutActivity.class);
            intent.putExtra(CheckInCheckOutActivity.isCheckinFlag, false);
            intent.putExtra(CheckInCheckOutActivity.ATTENDEE_KEY, value);
            startActivity(intent);
        }
    }

    @Override
    public void onReceiveError(String error, RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
        if (requestKey == RequestKeys.CHECK_IN_REQUEST_BY_QR_ID && error != null && error.equalsIgnoreCase(Messages.NOT_REGISTERED)) {
            Intent intent = new Intent(ScanQR.this, RegisterActivity.class);
            intent.putExtra(RegisterActivity.IS_NFC, true);
            intent.putExtra(RegisterActivity.SCAN_ID, attendeeId);
            startActivity(intent);
        } else {
            Log.d("", "error: " + error);
//            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(ScanQR.this, CheckInCheckOutActivity.class);
//            intent.putExtra(CheckInCheckOutActivity.isCheckinFlag, true);
//            intent.putExtra(CheckInCheckOutActivity.ATTENDEE_KEY, attendeeId);
//            startActivity(intent);

            RequestQueue MyRequestQueue2 = Volley.newRequestQueue(getApplicationContext());
            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, checkin_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Attendee Response", response);

                    JSONObject jsono = null;
                    JSONObject object = null;
                    try {
                        jsono = new JSONObject(response);
                        object = jsono.getJSONObject("attendee");
                        name = object.getString("first_name") + " " + object.getString("last_name");
                        mail = object.getString("email");
                        nfc_id = object.getString("nfc_attendee_id");
                        event_title = object.getString("event_name");
                        company = object.getString("c_name");
                        phone_number = object.getString("phone_number");
                        table = object.getString("t_number");
                        photo = object.getString("photo");

                        Intent intent = new Intent(ScanQR.this, in_out_Activity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("mail", mail);
                        intent.putExtra("nfc_id", nfc_id);
                        intent.putExtra("event_title", event_title);
                        intent.putExtra("company", company);
                        intent.putExtra("phone_number", phone_number);
                        intent.putExtra("table", table);
                        intent.putExtra("photo", photo);

                        startActivity(intent);

                        Log.d("Response", response + "\n" + object + "\n" + name + "\n" + nfc_id + "\n" + event_title + "\n" + company + "\n" + phone_number + "\n" + table + "\n" + photo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());
                    Toast.makeText(getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("nfc_attendee_id", UID.getText().toString());

                    return MyData;
                }
            };
            MyRequestQueue2.add(MyStringRequest);
            finish();
        }
    }
}
