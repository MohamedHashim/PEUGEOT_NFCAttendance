package com.NFCFZLLE.eventmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.utils.CircleImageView;

import java.io.File;

import static android.R.attr.data;
import static android.R.attr.typeface;

public class Peugeot_In_Out_Activity extends AppCompatActivity {

    private Boolean mIsCheckin;
    private LinearLayout mainLayout;
    public static final String isCheckinFlag = "mIsCheckIn";
    private Attendees attendee;
    static final int CAM_REQUEST1 = 1;

    public static final String ATTENDEE_KEY = "attendee";
    Typeface typeFace, typeFace2,typeFace3,typeFace4,typeFace5,typeFace6,typeFace7;
    TextView name, email, this_is, table_txt, company, phone, registration,mr, guest, ticket, seat, comments, table_number,attendee_type_txt;
    String name_txt, mail, nfc_id, event_title, company_txt, phone_number, table, photo,attendee_type;
    ImageView a;
    CircleImageView mProfilePic;

    ProgressBar mProgress;
    Button new_scan, take_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peugeot_in_out);

//        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

//        mProfilePic = (CircleImageView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name);
        mr= (TextView) findViewById(R.id.mr);
        attendee_type_txt= (TextView) findViewById(R.id.attendee_type);
        this_is= (TextView) findViewById(R.id.this_is);
        guest= (TextView) findViewById(R.id.guest);
        table_txt= (TextView) findViewById(R.id.table);
//        email = (TextView) findViewById(R.id.email);
//        attendee_id = (TextView) findViewById(R.id.attendee_id);
//        eventTitle = (TextView) findViewById(R.id.event_title);
//        company = (TextView) findViewById(R.id.company_title);
//        phone = (TextView) findViewById(R.id.phone);
//        registration = (TextView) findViewById(R.id.registration_date);
//        take_photo = (Button) findViewById(R.id.take_photo);
//        a= (ImageView) findViewById(R.id.imageView);
//        expiration = (TextView)findViewById(R.id.expiration_date);
//        ticket = (TextView)findViewById(R.id.ticket_tnumber);
//        seat = (TextView)findViewById(R.id.seat_number);
//        comments = (TextView)findViewById(R.id.comments_details);
        registration = (TextView) findViewById(R.id.table_number);

        typeFace= Typeface.createFromAsset(getAssets(),"fonts/Peugeot Normal v2_0.ttf");
        typeFace2= Typeface.createFromAsset(getAssets(),"fonts/Peugeot Light v2_0.ttf");
        typeFace3= Typeface.createFromAsset(getAssets(),"fonts/Peugeot Bold v2_0.ttf");
        typeFace4= Typeface.createFromAsset(getAssets(),"fonts/GE Dinar One Light.otf");
        typeFace5= Typeface.createFromAsset(getAssets(),"fonts/GE Dinar One Medium.otf");
        typeFace6= Typeface.createFromAsset(getAssets(),"fonts/GE Dinar One Bold.otf");
        typeFace7= Typeface.createFromAsset(getAssets(),"fonts/GEDinarOne-LightItalic.otf");




        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name_txt = bundle.getString("name");
            mail = bundle.getString("mail");
            nfc_id = bundle.getString("nfc_id");
            event_title = bundle.getString("event_title");
            company_txt = bundle.getString("company");
            phone_number = bundle.getString("phone_number");
            table = bundle.getString("table");
            photo = bundle.getString("photo");
            attendee_type=bundle.getString("attendee_type");

            attendee_type_txt.setText(attendee_type);
            attendee_type_txt.setTypeface(typeFace3);
            name.setText(name_txt);
            name.setTypeface(typeFace);
            mr.setTypeface(typeFace);
//            email.setText(mail);
//            attendee_id.setText(nfc_id);
//            eventTitle.setText(event_title);
//            company.setText(company_txt);
//            phone.setText(phone_number);
            registration.setText(table);
            registration.setTypeface(typeFace);
//            Picasso.with(getApplicationContext()).load(photo).into(mProfilePic);

        }
//        attendee = (Attendees) getIntent().getSerializableExtra(ATTENDEE_KEY);
//        if (attendee.getPhoto() != null && attendee.getPhoto().length() > 0) {
//            // Retrieves an image specified by the URL, displays it in the UI.
//        ImageRequest request = new ImageRequest(photo,
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                        mProfilePic.setImageBitmap(bitmap);
//                    }
//                }, 0, 0, null,
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        mProfilePic.setImageResource(R.drawable.usename);
//                    }
//                });
//
//        VolleyHelper.getInstance(this).getRequestQueue().add(request);
//        }

//            Picasso.with(getApplicationContext()).load(photo).into(mProfilePic);

        new_scan = (Button) findViewById(R.id.new_scan);
        new_scan.setTypeface(typeFace2);
        new_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                a.setVisibility(View.GONE);
                Intent intent = new Intent(Peugeot_In_Out_Activity.this, Peugeot_NFC_activity.class);
                startActivity(intent);
                finish();
            }
        });

        this_is.setTypeface(typeFace);
        guest.setTypeface(typeFace);
        table_txt.setTypeface(typeFace);
//        take_photo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent cam_intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File file = getFrontsidePhoto();
//                cam_intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(cam_intent1, CAM_REQUEST1);
//            }
//        });
//        a.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Intent emailIntent = new Intent(
//
//                        android.content.Intent.ACTION_SEND);
//
//                emailIntent.setType("image/png");
//                File file = new File("sdcard/camera_app/front_side.jpg");
//                Uri uri = Uri.fromFile(file);
//                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
//
//                        new String[]{email.getText().toString()});
//
//                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//
//                        "Welcome");
//
//                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//
//                        "Thank You for using Event Management service!");
//
//                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri
//
//                        .parse(uri.toString()));
//
//                startActivity(Intent.createChooser(emailIntent,
//
//                        "Send mail..."));
//            }
//        });
//
//    }

//    private File getFrontsidePhoto() {
//        File folder = new File("sdcard/camera_app");
//        if (!folder.exists())
//            folder.mkdir();
//        File image_file = new File(folder, "front_side.jpg");
//        return image_file;
//    }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 1:
//                String front_path = "sdcard/camera_app/front_side.jpg";
//                a.setVisibility(View.VISIBLE);
//                a.setImageDrawable(Drawable.createFromPath(front_path));
//                Toast.makeText(getApplicationContext(),"Press on photo to send to mail",Toast.LENGTH_LONG).show();
//                break;
//        }
//    }

}
