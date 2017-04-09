package com.NFCFZLLE.eventmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.network.VolleyHelper;
import com.NFCFZLLE.eventmanagement.utils.Helper;

/**
 * Created by Muhammad Tahir Ashraf on 08/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class UserProfile extends BaseActivity {

    ImageView scanNFCBtn,scanQRBtn;

    TextView username,companyTitle,email;

    ImageView user_profile;

    Button mLogout;

    User mUser;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        themeColor =R.color.red;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_profile);

        mUser = Helper.getUser(this);

        mLogout = (Button) findViewById(R.id.logoutBtn);

        user_profile = (ImageView) findViewById(R.id.user_profile);
        username = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);


        companyTitle = (TextView)findViewById(R.id.company_title);

        scanNFCBtn = (ImageView)findViewById(R.id.scan_nfc_btn);
        scanQRBtn = (ImageView)findViewById(R.id.scan_qr_btn);

        updateUI();


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.logOutUser(UserProfile.this);
                ((NFCApplication)UserProfile.this.getApplication()).StopBeaconService();
                Intent intent = new Intent(UserProfile.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        scanNFCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProfile.this,ScanNfc.class);
                startActivity(intent);
            }
        });

        scanQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,ScanQR.class);
                startActivity(intent);
            }
        });



    }

    private void updateUI() {
        username.setText(mUser.getFirst_name()+" "+mUser.getLast_name());
        email.setText(mUser.getEmail());
        companyTitle.setText(mUser.getCompany());

        if(mUser.getPic()!=null && mUser.getPic().length()>0){
            ImageRequest request = new ImageRequest(mUser.getPic(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            user_profile.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            user_profile.setImageResource(R.drawable.usename);
                        }
                    });

            VolleyHelper.getInstance(this).getRequestQueue().add(request);
        }

    }
}
