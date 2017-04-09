package com.NFCFZLLE.eventmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.network.VolleyHelper;
import com.NFCFZLLE.eventmanagement.utils.CircleImageView;

import java.io.File;

/**
 * Created by Muhammad Tahir Ashraf on 09/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class CheckInCheckOutActivity extends BaseActivity {

    private Boolean mIsCheckin;
    private LinearLayout mainLayout;
    public static final String isCheckinFlag = "mIsCheckIn";
    private Attendees attendee;

    public static final String ATTENDEE_KEY = "attendee";

    TextView name, email, attendee_id, eventTitle, company, phone, registration, expiration, ticket, seat, comments, table_number;

    CircleImageView mProfilePic;
    ImageView a;

    ProgressBar mProgress;
    Button new_scan, take_photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mIsCheckin = getIntent().getBooleanExtra(isCheckinFlag, false);
        if (mIsCheckin)
            themeColor = R.color.green;
        else
            themeColor = R.color.red;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_checkout);

        attendee = (Attendees) getIntent().getSerializableExtra(ATTENDEE_KEY);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        mProfilePic = (CircleImageView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        attendee_id = (TextView) findViewById(R.id.attendee_id);
        eventTitle = (TextView) findViewById(R.id.event_title);
        company = (TextView) findViewById(R.id.company_title);
        phone = (TextView) findViewById(R.id.phone);
//        registration = (TextView) findViewById(R.id.registration_date);
        take_photo = (Button) findViewById(R.id.take_photo);
        a = (ImageView) findViewById(R.id.imageView);
//        expiration = (TextView)findViewById(R.id.expiration_date);
//        ticket = (TextView)findViewById(R.id.ticket_number);
//        seat = (TextView)findViewById(R.id.seat_number);
//        comments = (TextView)findViewById(R.id.comments_details);
        table_number = (TextView) findViewById(R.id.table_number);
        new_scan = (Button) findViewById(R.id.new_scan);
        new_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setVisibility(View.GONE);
                Intent intent = new Intent(CheckInCheckOutActivity.this, Peugeot_NFC_activity.class);
                startActivity(intent);
                finish();
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cam_intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFrontsidePhoto();
                cam_intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cam_intent1, 1);
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(

                        android.content.Intent.ACTION_SEND);

                emailIntent.setType("image/png");
                File file = new File("sdcard/camera_app/front_side.jpg");
                Uri uri = Uri.fromFile(file);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,

                        new String[]{email.getText().toString()});

                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,

                        "Welcome");

                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,

                        "Thank You for using Event Management service!");

                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri

                        .parse(uri.toString()));

                startActivity(Intent.createChooser(emailIntent,

                        "Send mail..."));
            }
        });


        updateLayout();


    }

    private void updateLayout() {
        if (Build.VERSION.SDK_INT < 23) {

            if (mIsCheckin)
                mainLayout.setBackgroundColor(getResources().getColor(R.color.green));
            else
                mainLayout.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            if (mIsCheckin)
                mainLayout.setBackgroundColor(getColor(R.color.green));
            else
                mainLayout.setBackgroundColor(getColor(R.color.red));

        }

        if (mIsCheckin)
            ((TextView) findViewById(R.id.status)).setText("Check In");
        else
            ((TextView) findViewById(R.id.status)).setText("Check Out");


        if (attendee.getPhoto() != null && attendee.getPhoto().length() > 0) {
            // Retrieves an image specified by the URL, displays it in the UI.
            ImageRequest request = new ImageRequest(attendee.getPhoto(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            mProfilePic.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            mProfilePic.setImageResource(R.drawable.usename);
                        }
                    });

            VolleyHelper.getInstance(this).getRequestQueue().add(request);
        }

        updatevalues();

    }

    private void updatevalues() {
        name.setText(attendee.getFirst_name() + " " + attendee.getLast_name());
        email.setText(attendee.getEmail());
        attendee_id.setText(attendee.getNfc_attendee_id());
        eventTitle.setText(attendee.getEvent_name());
        company.setText(attendee.getCompany_attendee_name());
        table_number.setText(attendee.getTable_number());
        phone.setText(attendee.getPhone_number());
//        registration.setText(attendee.getRegistration_date());
//        expiration.setText(attendee.getExpiration_date());
//        comments.setText(attendee.getComments());
//        ticket.setText(attendee.getTicket_number());
//        seat.setText(attendee.getSeat_number());
    }

    private File getFrontsidePhoto() {
        File folder = new File("sdcard/camera_app");
        if (!folder.exists())
            folder.mkdir();
        File image_file = new File(folder, "front_side.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                String front_path = "sdcard/camera_app/front_side.jpg";
                a.setVisibility(View.VISIBLE);
                a.setImageDrawable(Drawable.createFromPath(front_path));
                Toast.makeText(getApplicationContext(),"Press on photo to send to mail",Toast.LENGTH_LONG).show();
                break;
        }
    }

}
