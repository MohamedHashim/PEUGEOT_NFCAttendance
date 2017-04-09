package com.NFCFZLLE.eventmanagement.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.NFCFZLLE.eventmanagement.constants.Globals;
import com.NFCFZLLE.eventmanagement.entities.AppData;
import com.NFCFZLLE.eventmanagement.entities.Attendees;
import com.NFCFZLLE.eventmanagement.entities.RequestKeys;
import com.NFCFZLLE.eventmanagement.entities.RequestParams;
import com.NFCFZLLE.eventmanagement.entities.ResponseObject;
import com.NFCFZLLE.eventmanagement.entities.User;
import com.NFCFZLLE.eventmanagement.interfaces.ValueCallback;
import com.NFCFZLLE.eventmanagement.utils.Helper;
import com.NFCFZLLE.eventmanagement.utils.Messages;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class InvokeWebserviceVolley<T> {

    private static final String TAG = InvokeWebserviceVolley.class.getSimpleName();
    private T mResponseObj;
    private Context mContext;
    private Gson mGson = new Gson();

    private RequestParams mParams;
    private RequestQueue queue;
    //new impl
    private ValueCallback<T> mCallback;


    //TODO In the end will club all the constructors together and introduce a Configuration object as an argument to the single constructor

    public InvokeWebserviceVolley(ValueCallback<T> callback, Context callerReference,RequestParams params) {
        mCallback = callback;
        mGson = new Gson();
        this.mContext = callerReference;
        this.mParams = params;
        this.queue = VolleyHelper.getInstance(this.mContext).getRequestQueue();
    }

    public void execute(){

        if(!Helper.isNetworkConnected(this.mContext))
        {
            mCallback.onReceiveError(Messages.INTERNET_NOT_AVAILABLE,null);
            return;
        }

        if(mParams.getHttpMethod() == Request.Method.GET){
            initVolleyRequest(constructUrl(mParams.getEndpointUrl() + mParams.getWebMethodName()));
        }else {
            if(Globals.IS_DEBUG) {
              String url = constructUrl(mParams.getEndpointUrl() + mParams.getWebMethodName());
                Log.d("", "URL>> " + url);
            }
            initVolleyRequest(mParams.getEndpointUrl() + mParams.getWebMethodName());
        }
    }

    private String constructUrl(String url) {


        StringBuilder builder = new StringBuilder();

        for (String key : mParams.getParams().keySet())
        {
            Object value = mParams.getParams().get(key);
            if (value != null)
            {
                try
                {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(key).append("=").append(value);
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        }

        url += "?" + builder.toString();

        return url;
    }

    private void initVolleyRequest(String url) {

        StringRequest objectRequest = new StringRequest(mParams.getHttpMethod(), url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    if(mParams.getRequestKey() == RequestKeys.FORGOT_PASSWORD_REQUEST) {

                        Log.d("", ">>>response location: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage, RequestKeys.FORGOT_PASSWORD_REQUEST);
                            } else {

                                mCallback.onReceiveValue(null, response, RequestKeys.FORGOT_PASSWORD_REQUEST);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.FORGOT_PASSWORD_REQUEST);
                        }
                    }else if(mParams.getRequestKey() == RequestKeys.UPDATE_LOCATION){

                        Log.d("", ">>>response location: " + response);

                        mCallback.onReceiveValue((T) response,"response",RequestKeys.UPDATE_LOCATION);

                    }else if(mParams.getRequestKey() == RequestKeys.LOGIN_REQUEST){

                        Log.d("", ">>>response: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage, RequestKeys.LOGIN_REQUEST);
                            } else {
                                // success
                                User mUser = responseObject.getData();
                                mCallback.onReceiveValue((T) mUser, Messages.LOGIN_SUCCESS,RequestKeys.LOGIN_REQUEST);
                                Log.d("login mail info",mUser.toString() +"\n"+mUser.getSite_id());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.LOGIN_REQUEST);
                        }

                    }else if(mParams.getRequestKey() == RequestKeys.CHECK_IN_REQUEST_BY_BEACON_ID){

                        Log.d("", ">>>response: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage,RequestKeys.CHECK_IN_REQUEST_BY_BEACON_ID);
                            } else {
                                // success
                                Attendees mAttendee = responseObject.getAttendee();
                                mCallback.onReceiveValue((T) mAttendee,mAttendee.getBeacon_id(),RequestKeys.CHECK_IN_REQUEST_BY_BEACON_ID);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.CHECK_IN_REQUEST_BY_BEACON_ID);
                        }


                    }else if(mParams.getRequestKey() == RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID){

                        Log.d("", ">>>response: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage,RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID);
                            } else {
                                // success
                                Attendees mAttendee = responseObject.getAttendee();
                                mCallback.onReceiveValue((T) mAttendee,mAttendee.getBeacon_id(),RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID);
                                Log.d("attendee",mAttendee.getTable_number().toString());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.CHECK_IN_REQUEST_BY_NFC_ID);
                        }


                    }else if(mParams.getRequestKey() == RequestKeys.CHECK_OUT_REQUEST_BY_NFC_ID){

                        Log.d("", ">>>response: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage,RequestKeys.CHECK_OUT_REQUEST_BY_NFC_ID);
                            } else {
                                // success
                                Attendees mAttendee = responseObject.getAttendee();
                                mCallback.onReceiveValue((T) mAttendee,mAttendee.getBeacon_id(),RequestKeys.CHECK_OUT_REQUEST_BY_NFC_ID);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.CHECK_OUT_REQUEST_BY_NFC_ID);
                        }


                    }else if(mParams.getRequestKey() == RequestKeys.CHECK_IN_REQUEST_BY_QR_ID){

                        Log.d("", ">>>response: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage,RequestKeys.CHECK_IN_REQUEST_BY_QR_ID);
                            } else {
                                // success
                                Attendees mAttendee = responseObject.getAttendee();
                                mCallback.onReceiveValue((T) mAttendee,mAttendee.getBeacon_id(),RequestKeys.CHECK_IN_REQUEST_BY_QR_ID);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.CHECK_IN_REQUEST_BY_QR_ID);
                        }


                    }else if(mParams.getRequestKey() == RequestKeys.CHECK_OUT_REQUEST_BY_QR_ID){

                        Log.d("", ">>>response: " + response);
                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage,RequestKeys.CHECK_OUT_REQUEST_BY_QR_ID);
                            } else {
                                // success
                                Attendees mAttendee = responseObject.getAttendee();
                                mCallback.onReceiveValue((T) mAttendee,mAttendee.getBeacon_id(),RequestKeys.CHECK_OUT_REQUEST_BY_QR_ID);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.CHECK_OUT_REQUEST_BY_QR_ID);
                        }


                    }else if(mParams.getRequestKey() == RequestKeys.ATTENDEE_REGISTER_REQUEST){
                        Log.d("", ">>>response: " + response);

                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                            ResponseObject responseObject = ((ResponseObject) mResponseObj);
                            String mErrorMessage = responseObject.getErrorMessage();
                            if (mErrorMessage != null && mErrorMessage.length() > 0) {
                                // error occurred
                                mCallback.onReceiveError(mErrorMessage, RequestKeys.ATTENDEE_REGISTER_REQUEST);
                            } else {
                                // success
                                mCallback.onReceiveValue((T) responseObject.getSuccessMessage(),Messages.REGISTER_SUCCESS,RequestKeys.ATTENDEE_REGISTER_REQUEST);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mCallback.onReceiveError("",RequestKeys.ATTENDEE_REGISTER_REQUEST);
                        }

                    }else if(mParams.getRequestKey() == RequestKeys.APP_DATA_REQUEST){
                        Log.d("", ">>>response: " + response);

                        try {
                            mResponseObj = (T) mGson.fromJson(response, ResponseObject.class);

                        ResponseObject responseObject = ((ResponseObject) mResponseObj);
                        String mErrorMessage = responseObject.getErrorMessage();
                        if (mErrorMessage != null && mErrorMessage.length() > 0) {
                            // error occurred
                            mCallback.onReceiveError(mErrorMessage, RequestKeys.APP_DATA_REQUEST);
                        } else {
                            // success
                            mCallback.onReceiveValue((T) new Gson().toJson(((ResponseObject) mResponseObj).getAppData(),AppData.class),Messages.REGISTER_SUCCESS,RequestKeys.APP_DATA_REQUEST);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        mCallback.onReceiveError("",RequestKeys.APP_DATA_REQUEST);
                    }

                    }else
                        (mCallback).onReceiveError("Unknown Request",null);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    (mCallback).onReceiveError(Messages.INTERNET_NOT_AVAILABLE,null);

                }
            }

        ){

            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(Globals.IS_DEBUG)
                    printParams(mParams.getParams());
                return mParams.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                return params;

            }
        };

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.queue.add(objectRequest);

    }

    private void printParams(Map<String, String> params) {

            Log.d("printParams",""+params.values().toString()+" \n");
    }
}
