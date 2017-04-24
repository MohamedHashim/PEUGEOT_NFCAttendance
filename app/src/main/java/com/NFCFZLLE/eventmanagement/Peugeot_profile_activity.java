package com.NFCFZLLE.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.utils.Helper;



public class Peugeot_profile_activity extends AppCompatActivity {

//    ImageView scanNFCBtn,scanQRBtn;

//    TextView username,companyTitle,email;

//    ImageView user_profile;

    Button mLogout;
    Button checkin;

    User mUser;



    @Override
    public void onCreate(Bundle savedInstanceState) {

//        themeColor =R.color.red;

        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        if (width >= 480)
            setContentView(R.layout.activity_peugeot_profile_activity); // for new and ordinary mobiles above 480p in width
        else
            setContentView(R.layout.activity_peugeot_profile_small_activity);//for small mobiles

        mUser = Helper.getUser(this);

        mLogout = (Button) findViewById(R.id.logout);
        checkin = (Button) findViewById(R.id.checkin);
//        checkin.setBackgroundResource(R.drawable.nfc);
//        user_profile = (ImageView) findViewById(R.id.user_profile);
//        username = (TextView)findViewById(R.id.name);
//        email = (TextView)findViewById(R.id.email);
//
//
//        companyTitle = (TextView)findViewById(R.id.company_title);
//
//        scanNFCBtn = (ImageView)findViewById(R.id.scan_nfc_btn);
//        scanQRBtn = (ImageView)findViewById(R.id.scan_qr_btn);

//        updateUI();


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.logOutUser(Peugeot_profile_activity.this);
                ((NFCApplication)Peugeot_profile_activity.this.getApplication()).StopBeaconService();
                Intent intent = new Intent(Peugeot_profile_activity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Peugeot_profile_activity.this,Peugeot_NFC_activity.class);
                startActivity(intent);
            }
        });

//        scanQRBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Peugeot_profile_activity.this,ScanQR.class);
//                startActivity(intent);
//            }
//        });



    }

//    private void updateUI() {
////        username.setText(mUser.getFirst_name()+" "+mUser.getLast_name());
////        email.setText(mUser.getEmail());
////        companyTitle.setText(mUser.getCompany());
//
//        if(mUser.getPic()!=null && mUser.getPic().length()>0){
//            ImageRequest request = new ImageRequest(mUser.getPic(),
//                    new Response.Listener<Bitmap>() {
//                        @Override
//                        public void onResponse(Bitmap bitmap) {
//                            user_profile.setImageBitmap(bitmap);
//                        }
//                    }, 0, 0, null,
//                    new Response.ErrorListener() {
//                        public void onErrorResponse(VolleyError error) {
//                            user_profile.setImageResource(R.drawable.usename);
//                        }
//                    });
//
//            VolleyHelper.getInstance(this).getRequestQueue().add(request);
//        }

//    }
}
