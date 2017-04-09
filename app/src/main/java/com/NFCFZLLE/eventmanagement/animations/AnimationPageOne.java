package com.NFCFZLLE.eventmanagement.animations;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.NFCFZLLE.eventmanagement.R;

import java.lang.ref.WeakReference;

/**
 * Created by Muhammad Tahir Ashraf on 11/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class AnimationPageOne {

    private static final int BUTTON_ANIM_TIME_MS = 600;
    int fromX=1000,toX=0,fromY=1000,toY=0;

    private static AnimationPageOne instance = null;
    private ImageView mCardHand,mDeviceHand;

    WeakReference<Context> mContext;

    public static AnimationPageOne getInstance(){
        if(instance==null)
            instance = new AnimationPageOne();

        return instance;
    }

    public void destroyInstance(){
        if (mCardHand!=null)
        mCardHand.clearAnimation();
        if (mDeviceHand!=null)
        mDeviceHand.clearAnimation();
        instance = null;
    }

    public void initAnimation(ImageView cardHand,ImageView deviceHand,WeakReference<Context> context){

        mContext = context;
        mCardHand = cardHand;
        mDeviceHand = deviceHand;

        animateFromRight(mCardHand);
        animateFromBottom(mDeviceHand);

    }

    private void animateFromBottom(final ImageView view) {
        TranslateAnimation animate = new TranslateAnimation(0,0,fromY,toY);
        animate.setDuration(BUTTON_ANIM_TIME_MS);
//        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.nfc_mobile_empty,mContext.get().getTheme()));
                }else{
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.nfc_mobile_empty));
                }
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.nfc_mobile,mContext.get().getTheme()));
                }else{
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.nfc_mobile));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fromX = 0;
                        toX = 300;
                        fromY = 0;
                        toY = 300;
                        animateToLeft(mCardHand);
                        animateToBottom(mDeviceHand);

                    }
                },1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    private void animateFromRight(View view) {
        TranslateAnimation animate = new TranslateAnimation(fromX,toX,0,0);
        animate.setDuration(BUTTON_ANIM_TIME_MS);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animate);
    }

    private void animateToLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(fromX,toX,0,0);
        animate.setDuration(BUTTON_ANIM_TIME_MS);
        animate.setFillAfter(true);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animate);
    }

    private void animateToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0,0,fromY,toY);
        animate.setDuration(BUTTON_ANIM_TIME_MS);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.nfc_mobile_empty,mContext.get().getTheme()));
                }else{
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.nfc_mobile_empty));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fromX = 300;
                        toX = 0;
                        fromY = 300;
                        toY = 0;
                        animateFromRight(mCardHand);
                        animateFromBottom(mDeviceHand);

                    }
                },1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animate.setFillAfter(true);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animate);
    }



}
