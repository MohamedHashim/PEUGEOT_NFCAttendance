package com.NFCFZLLE.eventmanagement.fragments;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.NFCFZLLE.eventmanagement.R;
import com.NFCFZLLE.eventmanagement.animations.AnimationPageOne;
import com.NFCFZLLE.eventmanagement.animations.AnimationPageTwo;

import java.lang.ref.WeakReference;

public class IntroFragment extends Fragment {

    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String PAGE = "page";

    private int mBackgroundColor, mPage;
    private ImageView cardHand;
    private ImageView deviceHand,pageTwoDeviceHand;


    public static IntroFragment newInstance(int backgroundColor, int page) {
        IntroFragment frag = new IntroFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, backgroundColor);
        b.putInt(PAGE, page);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(BACKGROUND_COLOR))
            throw new RuntimeException("Fragment must contain a \"" + BACKGROUND_COLOR + "\" argument!");
        mBackgroundColor = getArguments().getInt(BACKGROUND_COLOR);

        if (!getArguments().containsKey(PAGE))
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        mPage = getArguments().getInt(PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Select a layout based on the current page
        int layoutResId;
        switch (mPage) {
            case 0:
                layoutResId = R.layout.intro_layout_1;
                break;
            case 1:
                layoutResId = R.layout.intro_layout_2;
                break;
            default:
                layoutResId = R.layout.intro_layout_3;
        }

        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);

        // Set the current page index as the View's tag (useful in the PageTransformer)
        view.setTag(mPage);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the background color of the root view to the color specified in newInstance()
//        View background = view.findViewById(R.id.intro_background);
//        background.setBackgroundColor(mBackgroundColor);

        if(mPage==0)
            initAnimation(view);
        else if(mPage==1)
            initPageTwoAnimation(view);
        else if(mPage==2)
            initPageThreeAnimation(view);

    }

    private void initPageThreeAnimation(View view) {
        ImageView beaconSignals = (ImageView) view.findViewById(R.id.beacon_signal);
        ((AnimationDrawable) beaconSignals.getBackground()).start();
    }

    private void initPageTwoAnimation(View view) {

        pageTwoDeviceHand = (ImageView) view.findViewById(R.id.device_hand);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationPageTwo.getInstance().initAnimation(pageTwoDeviceHand,new WeakReference<Context>(getActivity()));
            }
        },300);


    }

    private void initAnimation(View view) {


         cardHand = (ImageView) view.findViewById(R.id.card_hand);
         deviceHand = (ImageView) view.findViewById(R.id.device_hand);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationPageOne.getInstance().initAnimation(cardHand,deviceHand,new WeakReference<Context>(getActivity()));
            }
        },300);


    }




}