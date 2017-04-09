package com.NFCFZLLE.eventmanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.gson.reflect.TypeToken;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.network.InvokeWebserviceVolley;
import com.NFCFZLLE.eventmanagement.services.BeaconService;
import com.NFCFZLLE.eventmanagement.utils.Helper;
import com.NFCFZLLE.eventmanagement.utils.Messages;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Muhammad Tahir Ashraf on 11/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class LoginActivity extends BaseActivity implements ValueCallback<User> {

    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText login_username,login_password;
    Button login_button;
    private ProgressBar mProgress;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Button forgotText;


    @Override
    protected void onResume() {
        super.onResume();

		/*
		 * It's important, that the activity is in the foreground (resumed). Otherwise
		 * an IllegalStateException is thrown.
		 */
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
		/*
		 * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
		 */
        if(mNfcAdapter!=null)
        stopForegroundDispatch(this, mNfcAdapter);
        else
            Toast.makeText(getApplicationContext(),"Ths device doesn't support NFC",Toast.LENGTH_LONG);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(true);
        }
        themeColor = R.color.login_bg;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Helper.checkIfNfcAvaialable(mNfcAdapter,this,false);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        login_username = (EditText)findViewById(R.id.login_username);
        login_password = (EditText)findViewById(R.id.login_password);
        forgotText = (Button)findViewById(R.id.forgetBtn);

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });


        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = login_username.getText().toString();
                String password = login_password.getText().toString();
                if(username.length()>0 && password.length()>0)
                initLoginRequest(username,password);
                else
                    Toast.makeText(LoginActivity.this,"Invalid Username and/or password",Toast.LENGTH_LONG).show();

            }
        });


        handleIntent(getIntent());

    }

    private void initLoginRequest(String username, String password) {
        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();
        map.put("email",username);
        map.put("password",password);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.LOGIN_USER);
        params.setRequestKey(RequestKeys.LOGIN_REQUEST);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<User>() {
        }.getType());

//onProvideAssistData();

        InvokeWebserviceVolley<User> volleyRequest = new InvokeWebserviceVolley<>(this, this,params);
        volleyRequest.execute();
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

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
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

            Log.d(TAG, " UID: " + UID);

            onRead(UID);

        }


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

        if(adapter!=null)
        adapter.enableForegroundDispatch(activity, mPendingIntent, null, null);
        else
            Toast.makeText(getApplicationContext(),"Ths device doesn't support NFC",Toast.LENGTH_LONG);
    }

    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }



    @Override
    public void onReceiveValue(User value, String message, RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
//        User mUser = value.getData();
        if(value!=null && requestKey == RequestKeys.LOGIN_REQUEST) {
            startService(new Intent(this, BeaconService.class));
            Helper.saveUser(value, LoginActivity.this);
            Log.d("login Error___",message);
            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, Peugeot_profile_activity.class);
            startActivity(intent);
            finish();
        }else if(requestKey == RequestKeys.FORGOT_PASSWORD_REQUEST)
        {
            Toast.makeText(LoginActivity.this, "Password Recovery Email Sent", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceiveError(String error,RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
        Log.d("login Error",error);
        Toast.makeText(LoginActivity.this,error,Toast.LENGTH_LONG).show();
    }


    public void onRead(String value) {
        if(value!=null) {
            initLoginByNFCRequest(value);
           /* Helper.saveUser(value, LoginActivity.this);
            Toast.makeText(LoginActivity.this, Messages.LOGIN_SUCCESS,Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));*/
        }
    }

    private void initLoginByNFCRequest(String value) {

        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();
        map.put("nfc_id",value);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.LOGIN_NFC_USER);
        params.setRequestKey(RequestKeys.LOGIN_REQUEST);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<User>() {
        }.getType());


        InvokeWebserviceVolley<User> volleyRequest = new InvokeWebserviceVolley<>(this, this,params);
        volleyRequest.execute();


    }


    public void onReadError(String error) {
        Toast.makeText(LoginActivity.this, Messages.LOGIN_SUCCESS,Toast.LENGTH_LONG).show();
    }

    private void showPasswordDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password, null);
        final EditText email = (EditText) view.findViewById(R.id.username);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if(email.getText()!=null && email.getText().toString().length()>0) {

                            initForgotPassword(email.getText().toString());
                        }else
                            Toast.makeText(LoginActivity.this,"Invalid Email",Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        builder.create().show();

    }

    private void initForgotPassword(String value) {

        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();
        map.put("email",value);
        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.FORGOT_PASSWORD);
        params.setRequestKey(RequestKeys.FORGOT_PASSWORD_REQUEST);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<User>() {
        }.getType());


        InvokeWebserviceVolley<User> volleyRequest = new InvokeWebserviceVolley<>(this, this,params);
        volleyRequest.execute();



    }

}
