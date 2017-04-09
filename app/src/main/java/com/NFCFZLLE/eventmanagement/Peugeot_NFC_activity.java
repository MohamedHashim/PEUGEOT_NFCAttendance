package com.NFCFZLLE.eventmanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.network.InvokeWebserviceVolley;
import com.NFCFZLLE.eventmanagement.utils.Helper;
import com.NFCFZLLE.eventmanagement.utils.Messages;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class Peugeot_NFC_activity extends AppCompatActivity implements ValueCallback<Attendees> {
    private ImageView cardHand;
    private ImageView deviceHand;
    private NfcAdapter mNfcAdapter;
    private String TAG = "ScanNfc";
    private TextView description, UID;
    String checkin_url = "http://ae.nfcvalet.com/mobile/Test?";
    String name, mail, nfc_id, event_title, company, phone_number, table, photo, site_id;

    ProgressBar mProgress;
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };


    String attendeeId;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;


    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        themeColor = R.color.intro1_bg;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peugeot_nfc_activity);
//        description = (TextView) findViewById(R.id.description);
        UID = (TextView) findViewById(R.id.UID);
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        Helper.checkIfNfcAvaialable(mNfcAdapter,this,false);

//        AnimationPageOne.getInstance().destroyInstance();
//        initAnimation();

//        if(!NfcAdapter.getDefaultAdapter(this).isEnabled()) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (mNfcAdapter == null) {
                // Stop here, we definitely need NFC
                Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
//            if (!mNfcAdapter.isEnabled()) {
//                UID.setText("NFC is disabled");
//            } else {
//                UID.setText("NFC Scanner");
//            }
        }
//    }

    private void initCheckIn(String attendeeId) {


        User mUser = Helper.getUser(this);
        if (mUser != null)
            site_id = mUser.getSite_id();
        Map<String, String> map = new HashMap<>();
        map.put("nfc_attendee_id", "" + attendeeId);
        map.put("site_id", "" + site_id);
        Log.d("site_id>>>>", site_id);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.ATTENDEE_CHECK_IN);
        params.setRequestKey(RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<Attendees>() {
        }.getType());

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this, this, params);
        volleyRequest.execute();


    }

    private void initCheckOut(String attendeeId) {

        mProgress.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("nfc_attendee_id", "" + attendeeId);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.ATTENDEE_CHECK_OUT);
        params.setRequestKey(RequestKeys.CHECK_OUT_REQUEST_BY_NFC_ID);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<Attendees>() {
        }.getType());

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this, this, params);
        volleyRequest.execute();

    }


//    private void initAnimation() {
//
//
//        cardHand = (ImageView) findViewById(R.id.card_hand);
//        deviceHand = (ImageView) findViewById(R.id.device_hand);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AnimationPageOne.getInstance().initAnimation(cardHand, deviceHand, new WeakReference<Context>(Peugeot_NFC_activity.this));
//            }
//        }, 600);
//
//
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        /*
         * This method gets called, when a new Intent gets associated with the current activity instance.
		 * Instead of creating a new activity, onNewIntent will be called. For more information have a look
		 * at the documentation.
		 *
		 * In our case this method gets called, when the user attaches a Tag to the device.
		 */
//        handleIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String id = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
//            description.setText(
//                    "NFC Card\n" + id);
            UID.setText(id);
            initCheckIn(id);
        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "inside handleIntent() ");
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            String type = intent.getType();
//            if (MIME_TEXT_PLAIN.equals(type)) {
            Log.d(TAG, " mime type: " + type);

            String UID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            initCheckIn(UID);

            Log.d(TAG, " UID: " + UID);

            onRead(UID);

        }
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {


        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};


        adapter.enableForegroundDispatch(activity, mPendingIntent, null, null);

    }

    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    public void onRead(String object) {
        attendeeId = object;
//        checkIn.setEnabled(true);
//        checkOut.setEnabled(true);
    }


    public void onReadError(String error) {
        Toast.makeText(Peugeot_NFC_activity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveValue(Attendees value, String message, RequestKeys requestKey) {

    }

    @Override
    public void onReceiveError(String error, RequestKeys requestKey) {
//        mProgress.setVisibility(View.GONE);
        if (requestKey == RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID && error != null && error.equalsIgnoreCase(Messages.NOT_REGISTERED)) {
            Intent intent = new Intent(Peugeot_NFC_activity.this, RegisterActivity.class);
            intent.putExtra(RegisterActivity.IS_NFC, true);
            intent.putExtra(RegisterActivity.SCAN_ID, UID.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Log.d("", "error: " + error);
//            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
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

                        Intent intent = new Intent(Peugeot_NFC_activity.this, Peugeot_In_Out_Activity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("mail", mail);
                        intent.putExtra("nfc_id", nfc_id);
                        intent.putExtra("event_title", event_title);
                        intent.putExtra("company", company);
                        intent.putExtra("phone_number", phone_number);
                        intent.putExtra("table", table);
                        intent.putExtra("photo", photo);

                        startActivity(intent);
                        finish();

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
        }
    }
}