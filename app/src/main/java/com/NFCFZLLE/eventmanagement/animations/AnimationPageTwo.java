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
public class AnimationPageTwo {

    private static final int BUTTON_ANIM_TIME_MS = 600;
    int fromY=1000,toY=0;

    private static AnimationPageTwo instance = null;
    private ImageView mDeviceHand;

    WeakReference<Context> mContext;

    public static AnimationPageTwo getInstance(){
        if(instance==null)
            instance = new AnimationPageTwo();

        return instance;
    }

    public void initAnimation(ImageView deviceHand,WeakReference<Context> context){

        mContext = context;
        mDeviceHand = deviceHand;

        animateFromBottom(mDeviceHand);

    }

    public void destroyInstance(){
        if(mDeviceHand!=null)
        mDeviceHand.clearAnimation();
        instance = null;
    }


    private void animateFromBottom(final ImageView view) {
        TranslateAnimation animate = new TranslateAnimation(0,0,fromY,toY);
        animate.setDuration(BUTTON_ANIM_TIME_MS);
//        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.qr_hand_empty,mContext.get().getTheme()));
                }else{
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.qr_hand_empty));
                }
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.qr_hand,mContext.get().getTheme()));
                }else{
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.qr_hand));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fromY = 0;
                        toY = 300;
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


    private void animateToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0,0,fromY,toY);
        animate.setDuration(BUTTON_ANIM_TIME_MS);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.qr_hand_empty,mContext.get().getTheme()));
                }else{
                    mDeviceHand.setImageDrawable(mContext.get().getResources().getDrawable(R.drawable.qr_hand_empty));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fromY = 300;
                        toY = 0;
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
