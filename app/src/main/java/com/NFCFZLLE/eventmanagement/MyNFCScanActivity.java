package com.NFCFZLLE.eventmanagement;

import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NFCFZLLE.eventmanagement.animations.AnimationPageOne;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MyNFCScanActivity extends BaseActivity {

    private ImageView cardHand;
    private ImageView deviceHand;
    private NfcAdapter mNfcAdapter;
    private TextView description;
    String checkin_url = "http://ae.nfcvalet.com/mobile/Test?";
    String name, mail, nfc_id, event_title, company, phone_number, table, photo;
    ProgressDialog dialog;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        themeColor = R.color.intro1_bg;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_nfc);
        description = (TextView) findViewById(R.id.description);

        AnimationPageOne.getInstance().destroyInstance();
        initAnimation();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            description.setText("NFC is disabled");
        } else {
            description.setText("NFC Scanner");
        }
    }

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
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            nfc_id = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            description.setText(
                    "NFC Card\n" + nfc_id);

            RequestQueue MyRequestQueue2 = Volley.newRequestQueue(getApplicationContext());
            dialog = new ProgressDialog(MyNFCScanActivity.this);
            dialog.setMessage("Loading ...");
//            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(true);
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

                        Intent intent = new Intent(MyNFCScanActivity.this, in_out_Activity.class);
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
                    MyData.put("nfc_attendee_id", nfc_id);

                    return MyData;
                }
            };
            MyRequestQueue2.add(MyStringRequest);
        }
    }


    private void initAnimation() {


        cardHand = (ImageView) findViewById(R.id.card_hand);
        deviceHand = (ImageView) findViewById(R.id.device_hand);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationPageOne.getInstance().initAnimation(cardHand, deviceHand, new WeakReference<Context>(MyNFCScanActivity.this));
            }
        }, 600);
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

}