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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.NFCFZLLE.eventmanagement.animations.AnimationPageTwo;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MyQRScanActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1122;
    private ImageView pageTwoDeviceHand;
    ProgressBar mProgress;
    String checkin_url = "http://ae.nfcvalet.com/mobile/Test?";
    String name, mail, nfc_id, event_title, company, phone_number, table, photo;
    private String attendeeId;
    Button checkin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        themeColor = R.color.intro2_bg;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);
        AnimationPageTwo.getInstance().destroyInstance();
        initPageTwoAnimation();
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        checkin = (Button) findViewById(R.id.button);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP_MR1 && ContextCompat.checkSelfPermission(MyQRScanActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MyQRScanActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    initScanner();
                }

            }
        });

    }

    private void initPageTwoAnimation() {

        pageTwoDeviceHand = (ImageView) findViewById(R.id.device_hand);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationPageTwo.getInstance().initAnimation(pageTwoDeviceHand, new WeakReference<Context>(MyQRScanActivity.this));
            }
        }, 300);


    }

    private void initScanner() {

        IntentIntegrator integrator = new IntentIntegrator(MyQRScanActivity.this);
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

                            Intent intent = new Intent(MyQRScanActivity.this, in_out_Activity.class);
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
                        MyData.put("nfc_attendee_id", attendeeId);

                        return MyData;
                    }
                };
                MyRequestQueue2.add(MyStringRequest);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
