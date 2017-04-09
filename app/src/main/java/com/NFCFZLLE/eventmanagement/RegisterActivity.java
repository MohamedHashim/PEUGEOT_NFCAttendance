package com.NFCFZLLE.eventmanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.AppData;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.network.InvokeWebserviceVolley;
import com.NFCFZLLE.eventmanagement.utils.CircleImageView;
import com.NFCFZLLE.eventmanagement.utils.Helper;
import com.NFCFZLLE.eventmanagement.utils.Messages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad Tahir Ashraf on 11/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class RegisterActivity extends BaseActivity implements ValueCallback<String>,DatePickerDialog.OnDateSetListener {

    User mUser;

    CircleImageView avatarImage;

    EditText first_name,last_name,email,company,phone,ticket,seat,comments;

    TextView registration,expiration,table_namuber;

    Spinner eventTitle,attendeeTypesSpinner;

    String firstName,lastName,emailText,eventTitleText,companyText,phoneText,registrationText,expirationText,ticketText,seatText,commentsText;

    Button registerBtn;

    boolean isNFC;

    String ScanId;

    int GALLERY_INTENT_CALLED = 100;
    int GALLERY_KITKAT_INTENT_CALLED = 200;
    int CAMERA_REQUEST = 300;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1122;

    int IMAGE_NOT_SELECTED = -1;
    int IMAGE_SELECTED = 1;

    ProgressBar mProgress;

    public static final String IS_NFC = "is_nfc";
    public static final String SCAN_ID = "scan_id";

    AppData mAppData;
    private String selectedEventId;

    private List<String> events = new ArrayList<>();
    private List<String> eventsIds = new ArrayList<>();
    private List<String> attendeeTypes = new ArrayList<>();
    private List<String> attendeeIds = new ArrayList<>();
    private String selectedAttendeeTypeId;
    private String selectedEventTitle;
    private DatePickerDialog mDatePickerDialog;
    private boolean registrationView;
    private boolean expirationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        themeColor = R.color.login_bg;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mUser = Helper.getUser(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isNFC = getIntent().getBooleanExtra(IS_NFC,false);

            ScanId = getIntent().getStringExtra(SCAN_ID);

        }

        Log.d("nfc id",ScanId);

        initView();
        initLoadAttendeeTypes();
    }


    private void initView() {

        mProgress = (ProgressBar)findViewById(R.id.progress_bar);
        avatarImage = (CircleImageView) findViewById(R.id.profile_pic);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        first_name = (EditText)findViewById(R.id.first_name);
        last_name = (EditText)findViewById(R.id.last_name);
        email = (EditText)findViewById(R.id.email);
        //attendee_id = (EditText)findViewById(R.id.attendee_id) ;
        eventTitle = (Spinner) findViewById(R.id.event_title);
        attendeeTypesSpinner = (Spinner) findViewById(R.id.attendee_type);
        company = (EditText)findViewById(R.id.company_title);
        phone = (EditText)findViewById(R.id.phone);
        registration = (TextView)findViewById(R.id.registration_date);
        expiration = (TextView)findViewById(R.id.expiration_date);
        ticket = (EditText)findViewById(R.id.ticket_number);
        seat = (EditText)findViewById(R.id.seat_number);
        comments = (EditText)findViewById(R.id.comments_details);
        table_namuber= (TextView) findViewById(R.id.table_namuber);

        company.setText("");

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationView = true;
                initDatePicker();
            }
        });

        expiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationView = false;
                initDatePicker();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initVerifyData();
            }
        });

        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP_MR1 && (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CAMERA);


                } else {
                    showOptionsDialog();
                }
            }
        });

    }

    private void initLoadAttendeeTypes() {
        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();
        map.put("company_id",mUser.getCompany_id());

        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.APP_DATA);
        params.setRequestKey(RequestKeys.APP_DATA_REQUEST);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<String>() {
        }.getType());

        InvokeWebserviceVolley<String> volleyRequest = new InvokeWebserviceVolley<>(this, this,params);
        volleyRequest.execute();
    }



    private void initVerifyData() {

        firstName = first_name.getText().toString();
        lastName = last_name.getText().toString();
        emailText = email.getText().toString();
        phoneText = phone.getText().toString();
        companyText = company.getText().toString();
        eventTitleText = selectedEventTitle;
        companyText = company.getText().toString();
        registrationText = registration.getText().toString();
        expirationText = expiration.getText().toString();
        ticketText = ticket.getText().toString();
        seatText = seat.getText().toString();
        commentsText = comments.getText().toString();

        if(lastName.length()>0 && firstName.length()>0 && emailText.length()>0 && eventTitleText.length()>0 && selectedAttendeeTypeId.length()>0 && companyText.length()>0 && phoneText.length()>0 && registrationText.length()>0 && expirationText.length()>0){
            initRegistration();
        }else{
            Toast.makeText(this, Messages.FILL_ALL_FIELDS,Toast.LENGTH_LONG).show();
        }
        Log.d("parameters",firstName+"\n"+lastName+"\n"+eventTitleText+"\n"+mUser.getCompany_id()+"\n"+phoneText+"\n"+emailText+"\n"+selectedEventId+"\n"+selectedAttendeeTypeId+"\n"+registrationText+"\n"+expirationText+"\n"+commentsText+"\n"+ticketText+"\n"+seatText+"\n");

    }

    private void initRegistration() {
        mProgress.setVisibility(View.VISIBLE);

        Map<String,String> map = new HashMap<>();

       if(isNFC)
        map.put("nfc_attendee_id",ScanId);
        else
            map.put("qr_code_attendee",ScanId);

        map.put("first_name",firstName);
        map.put("last_name",lastName);
        map.put("title",eventTitleText);
        map.put("company",""+mUser.getCompany_id());
        map.put("c_name",""+company.getText().toString());
        map.put("t_number",""+table_namuber.getText().toString());
        map.put("phone_number",phoneText);
        map.put("email",emailText);
        map.put("event",selectedEventId);
        map.put("attendee_type",selectedAttendeeTypeId);
        map.put("registration_date",registrationText);
        map.put("expiration_date",expirationText);
        map.put("comments",commentsText);
        if(ticketText != null && ticketText.length()>0)
        map.put("ticket_number",ticketText);
        if(seatText != null && seatText.length()>0)
        map.put("seat_number",seatText);
        Log.d("parameters",firstName+"\n"+lastName+"\n"+eventTitleText+"\n"+mUser.getCompany_id()+"\n"+phoneText+"\n"+emailText+"\n"+selectedEventId+"\n"+selectedAttendeeTypeId+"\n"+registrationText+"\n"+expirationText+"\n"+commentsText+"\n"+ticketText+"\n"+seatText+"\n");

        map.put("photo","" + Helper.convertImageToBase64(this,Helper.getPathFromBitmap(this,((BitmapDrawable)avatarImage.getDrawable()).getBitmap())));

        RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.REGISTER_ATTENDEE);
        params.setRequestKey(RequestKeys.ATTENDEE_REGISTER_REQUEST);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<String>() {
        }.getType());

          InvokeWebserviceVolley<String> volleyRequest = new InvokeWebserviceVolley<>(this, this,params);
        volleyRequest.execute();
    }


    private void updateSpinners() {

        updateEventsSpinner();
        updateAttendeesSpinner();
        selectedEventTitle = events.get(0);
        selectedEventId = eventsIds.get(0);
        selectedAttendeeTypeId = attendeeIds.get(0);

    }

    private void updateAttendeesSpinner() {

        for(int i=0;i<mAppData.getAttendeetype().size();i++){
            attendeeTypes.add(mAppData.getAttendeetype().get(i).getAttendees_type());
            attendeeIds.add(mAppData.getAttendeetype().get(i).getId());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, attendeeTypes);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        attendeeTypesSpinner.setAdapter(dataAdapter);


        attendeeTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAttendeeTypeId = attendeeIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateEventsSpinner() {

        for(int i=0;i<mAppData.getEventtype().size();i++){
            events.add(mAppData.getEventtype().get(i).getEvent_type_name());
            eventsIds.add(mAppData.getEventtype().get(i).getId());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, events);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        eventTitle.setAdapter(dataAdapter);

        eventTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEventId = eventsIds.get(position);
                selectedEventTitle = events.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void showOptionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_picture)
                .setItems(R.array.image_selection_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                break;
                            case 1:
                                initImageSelection();
                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private void initImageSelection() {
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)),GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
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
                    showOptionsDialog();

                } else {

                }
                return;
            }
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (null == data) return;
        Uri originalUri = null;
        if (requestCode == GALLERY_INTENT_CALLED) {
            originalUri = data.getData();
            avatarImage.setTag(IMAGE_SELECTED);
        } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
            originalUri = data.getData();
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
                getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

            avatarImage.setTag(IMAGE_SELECTED);

        }else if(requestCode == CAMERA_REQUEST){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            originalUri = getImageUri(this, photo);
            avatarImage.setTag(IMAGE_SELECTED);
        }
        avatarImage.setImageURI(originalUri);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        return Uri.parse(Helper.getPathFromBitmap(inContext, inImage));
    }

    @Override
    public void onReceiveValue(String value, String message, RequestKeys requestKey) {

        Log.d("","value: "+value);
        if(requestKey == RequestKeys.ATTENDEE_REGISTER_REQUEST) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        }else if(requestKey == RequestKeys.APP_DATA_REQUEST){
            mAppData = new Gson().fromJson(value,AppData.class);
            updateSpinners();
        }
        mProgress.setVisibility(View.GONE);
    }


    @Override
    public void onReceiveError(String error, RequestKeys requestKey) {
        mProgress.setVisibility(View.GONE);
        Log.d("","value: "+error);
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }

    public void initDatePicker(){

        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        mDatePickerDialog = new DatePickerDialog(this, this, year, month, day);
        mDatePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String day,month;

        if(registrationView){
            monthOfYear++;
            if(monthOfYear<10)
                month ="0"+monthOfYear;
            else
            month =""+monthOfYear;
            if(dayOfMonth<10)
                day = "0"+dayOfMonth;
            else
            day = ""+dayOfMonth;

            registration.setText(year+"-"+month+"-"+day);
        }else{
            monthOfYear++;

            if(monthOfYear<10)
                month ="0"+monthOfYear;
            else
                month =""+monthOfYear;
            if(dayOfMonth<10)
                day = "0"+dayOfMonth;
            else
                day = ""+dayOfMonth;


            expiration.setText(year+"-"+month+"-"+day);
        }
    }
}
