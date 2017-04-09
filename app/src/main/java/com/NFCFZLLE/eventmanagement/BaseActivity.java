package com.NFCFZLLE.eventmanagement;

import android.app.ActionBar;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.interfaces.LocationListener;
import com.NFCFZLLE.eventmanagement.network.VolleyHelper;
import com.NFCFZLLE.eventmanagement.utils.Helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by t-tahir on 02/05/2016.
 */
public class BaseActivity extends AppCompatActivity implements LocationListener {


    public double lat,lng;


    public int themeColor = R.color.grey;

    @Override
    protected void onResume() {
        super.onResume();
        updateLocation();
    }

    public void updateLocation(){

        LocationHandler mhandler = LocationHandler.getInstance(this);
        if (mhandler.isGpsEnabled()) { //if gps is enabled get new location.
            mhandler.requestNewLocation();
            mhandler.setOnLocationReceived(this);
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && themeColor!=0) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getResources().getColor(themeColor,getTheme()));
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(themeColor));
            }
        }else if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }
        super.onCreate(savedInstanceState);

    }



    public void changeStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && color!=0) {

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getResources().getColor(color,getTheme()));
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(getResources().getColor(color));
            }
        }
    }


    private void initUpdateLocationRequest(User mUser) {



        Map<String,String> map = new HashMap<>();
        map.put("user_id",""+mUser.getId());
        map.put("lat",""+lat);
        map.put("long",""+lng);
        final RequestParams params = new RequestParams();
        params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
        params.setWebMethodName(Globals.WebMethods.UPDATE_LOCATION);
        params.setRequestKey(RequestKeys.UPDATE_LOCATION);
        params.setHttpMethod(Request.Method.POST);
        params.setParams(map);
        params.setIsCollection(false);
        params.setIsAuthenticated(false);
        params.setResultType(new TypeToken<String>() {
        }.getType());

        StringRequest request = new StringRequest(params.getHttpMethod(), params.getEndpointUrl() + params.getWebMethodName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("","location response: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","location response: "+error);
            }
        }){

            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                return params;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(this).getRequestQueue().add(request);


    }

    @Override
    public void onLocationReceived(Location loc) {

        if(loc!=null) {
            lat = loc.getLatitude();
            lng = loc.getLongitude();
        }

        User mUser = Helper.getUser(this);
        if(mUser!=null)
            initUpdateLocationRequest(mUser);

    }

    @Override
    public void onLocationFailed(String reason) {

    }



}
