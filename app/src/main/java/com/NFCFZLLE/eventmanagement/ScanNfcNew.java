package com.NFCFZLLE.eventmanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.NFCFZLLE.eventmanagement.animations.AnimationPageOne;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.interfaces.NFCListener;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.network.InvokeWebserviceVolley;
import com.NFCFZLLE.eventmanagement.utils.Helper;
import com.NFCFZLLE.eventmanagement.utils.Messages;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Muhammad Tahir Ashraf on 13/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class ScanNfcNew extends BaseActivity implements NFCListener<String>, ValueCallback<Attendees>{

    private ImageView cardHand;
    private ImageView deviceHand;
    private NfcAdapter mNfcAdapter;
    private String TAG = "ScanNfc";

    ProgressBar mProgress;

//    private static final String MIME_TEXT_PLAIN = "event/management";

    Button checkIn;
    private Button checkOut;

    private String attendeeId;
    private static PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onResume() {
        super.onResume();

		/*
		 * It's important, that the activity is in the foreground (resumed). Otherwise
		 * an IllegalStateException is thrown.
		 */
        setupForegroundDispatch(this, mNfcAdapter);
    }



  /*  @Override
    protected void onPause() {
		*//*
		 * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
		 *//*
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }
*/

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        themeColor = R.color.intro1_bg;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_nfc);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Helper.checkIfNfcAvaialable(mNfcAdapter,this,false);

        AnimationPageOne.getInstance().destroyInstance();
        initAnimation();

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        checkIn = (Button)findViewById(R.id.button);
//        checkOut = (Button)findViewById(R.id.button2);

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCheckIn(attendeeId);

            }
        });
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCheckOut(attendeeId);
            }
        });

        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.


        handleIntent(getIntent());

    }

    private void initCheckIn(String attendeeId) {

        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();
        map.put("nfc_attendee_id",""+attendeeId);
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

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this,this,params);
        volleyRequest.execute();


    }

    private void initCheckOut(String attendeeId) {

        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();
        map.put("nfc_attendee_id",""+attendeeId);
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

        InvokeWebserviceVolley<Attendees> volleyRequest = new InvokeWebserviceVolley<>(this,this,params);
        volleyRequest.execute();

    }


    private void initAnimation() {


        cardHand = (ImageView) findViewById(R.id.card_hand);
        deviceHand = (ImageView) findViewById(R.id.device_hand);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationPageOne.getInstance().initAnimation(cardHand,deviceHand,new WeakReference<Context>(ScanNfcNew.this));
            }
        },600);


    }


    @Override
    protected void onNewIntent(Intent intent) {
		/*
		 * This method gets called, when a new Intent gets associated with the current activity instance.
		 * Instead of creating a new activity, onNewIntent will be called. For more information have a look
		 * at the documentation.
		 *
		 * In our case this method gets called, when the user attaches a Tag to the device.
		 */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "inside handleIntent() ");

    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
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
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };


        adapter.enableForegroundDispatch(activity, mPendingIntent, null, null);
    }

    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    public void onRead(String object) {
        attendeeId = object;
        checkIn.setEnabled(true);
        checkOut.setEnabled(true);
    }

    @Override
    public void onReadError(String error) {
        Toast.makeText(ScanNfcNew.this,error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveValue(Attendees value, String message, RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
        if(requestKey == RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID) {
            Intent intent = new Intent(ScanNfcNew.this, CheckInCheckOutActivity.class);
            intent.putExtra(CheckInCheckOutActivity.isCheckinFlag, true);
            intent.putExtra(CheckInCheckOutActivity.ATTENDEE_KEY, value);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(ScanNfcNew.this, CheckInCheckOutActivity.class);
            intent.putExtra(CheckInCheckOutActivity.isCheckinFlag, false);
            intent.putExtra(CheckInCheckOutActivity.ATTENDEE_KEY, value);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onReceiveError(String error, RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
        if(requestKey == RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID && error!=null && error.equalsIgnoreCase(Messages.NOT_REGISTERED)){
            Intent intent = new Intent(ScanNfcNew.this,RegisterActivity.class);
            intent.putExtra(RegisterActivity.IS_NFC,true);
            intent.putExtra(RegisterActivity.SCAN_ID,attendeeId);
            startActivity(intent);
            finish();
        }else
            Log.d("","error: "+error);
    }

/*
    */
/**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     *//*

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
			*/
/*
			 * See NFC forum specification for "Text Record Type Definition" at 3.2.1
			 *
			 * http://www.nfc-forum.org/specs/
			 *
			 * bit_7 defines encoding
			 * bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 *//*


            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
              attendee =  new Gson().fromJson(result, Attendees.class);
              Log.d("",""+attendee.toString());
                if(attendee!=null){
                    checkIn.setEnabled(true);
                    checkOut.setEnabled(true);
                }
            }
        }
    }
*/

}
