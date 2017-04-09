package com.NFCFZLLE.eventmanagement;

/**
 * Created by Muhammad Tahir Ashraf on 11/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class demoScreen extends BaseActivity {


//    ImageView deviceHand;
//    private static final int BUTTON_ANIM_TIME_MS = 600;
//    int fromY=1000,toY=0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.dummy1);
//
//        deviceHand = (ImageView)findViewById(R.id.device_hand);
//
//        inithandler();
//
//
//    }
//
//    private void inithandler() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//              animateFromBottom(deviceHand);
//            }
//        },300);
//
//    }
//
//    private void animateFromBottom(final ImageView view) {
//        TranslateAnimation animate = new TranslateAnimation(0,0,fromY,toY);
//        animate.setDuration(BUTTON_ANIM_TIME_MS);
////        animate.setFillAfter(true);
//        animate.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    deviceHand.setImageDrawable(getResources().getDrawable(R.drawable.qr_hand_empty,getTheme()));
//                }else{
//                    deviceHand.setImageDrawable(getResources().getDrawable(R.drawable.qr_hand_empty));
//                }
//                view.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    deviceHand.setImageDrawable(getResources().getDrawable(R.drawable.qr_hand,getTheme()));
//                }else{
//                    deviceHand.setImageDrawable(getResources().getDrawable(R.drawable.qr_hand));
//                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        fromY = 0;
//                        toY = 300;
//                        animateToBottom(deviceHand);
//
//                    }
//                },1000);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        view.startAnimation(animate);
//    }
//
//
//    private void animateToBottom(View view) {
//        TranslateAnimation animate = new TranslateAnimation(0,0,fromY,toY);
//        animate.setDuration(BUTTON_ANIM_TIME_MS);
//        animate.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    deviceHand.setImageDrawable(getResources().getDrawable(R.drawable.qr_hand_empty,getTheme()));
//                }else{
//                    deviceHand.setImageDrawable(getResources().getDrawable(R.drawable.qr_hand_empty));
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        fromY = 300;
//                        toY = 0;
//                        animateFromBottom(deviceHand);
//
//                    }
//                },1000);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        animate.setFillAfter(true);
//        view.setVisibility(View.VISIBLE);
//        view.startAnimation(animate);
//    }

}
