package com.NFCFZLLE.eventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.estimote.sdk.SystemRequirementsChecker;
import com.NFCFZLLE.eventmanagement.animations.AnimationPageOne;
import com.NFCFZLLE.eventmanagement.animations.AnimationPageTwo;
import com.NFCFZLLE.eventmanagement.widgets.CirclePageIndicator;

public class MainActivity extends BaseActivity{

    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private ImageView nextButton,previousButton;
    private int colors[] = {R.color.intro1_bg,R.color.intro2_bg,R.color.intro3_bg};

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeColor = colors[0];
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        nextButton = (ImageView) findViewById(R.id.next_button);
        previousButton = (ImageView) findViewById(R.id.previous_button);

        mIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);

//
//        // Set an Adapter on the ViewPager
//        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));
//
//        // Set a PageTransformer
//        mViewPager.setPageTransformer(false, new IntroPageTransformer());
//
//        mIndicator.setCentered(true);
//        mIndicator.setDotsSpacing(4f);
//        mIndicator.setViewPager(mViewPager);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewPager.getCurrentItem()+1<mViewPager.getAdapter().getCount()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    if(mViewPager.getCurrentItem()>0)
                    previousButton.setVisibility(View.VISIBLE);
                    else
                        previousButton.setVisibility(View.GONE);
                }else
                {
                    startActivity(new Intent(MainActivity.this,Peugeot_profile_activity.class));
                    finish();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1,true);
                if(mViewPager.getCurrentItem() == 0)
                    previousButton.setVisibility(View.GONE);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeStatusBarColor(colors[position]);

               /* if(position==0){
                    startPageOneAnimation();
                    stopPageTwoAnimation();
                }else if(position == 1){
                    startPageTwoAnimation();
                    stopPageOneAnimation();
                }else{
                    stopPageOneAnimation();
                    stopPageTwoAnimation();
                }
*/
                if(mViewPager.getCurrentItem() == 0)
                    previousButton.setVisibility(View.GONE);
                else
                    previousButton.setVisibility(View.VISIBLE);
            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void stopPageOneAnimation() {

    }

    private void startPageTwoAnimation() {

    }

    private void stopPageTwoAnimation() {

    }

    private void startPageOneAnimation() {

    }

    @Override
    protected void onDestroy() {

        AnimationPageOne instance = AnimationPageOne.getInstance();
        if(instance!=null)
        instance.destroyInstance();
        AnimationPageTwo instanceTwo = AnimationPageTwo.getInstance();
        if(instanceTwo!=null)
            instanceTwo.destroyInstance();


        super.onDestroy();

    }


    /*    RequestParams params = new RequestParams();
    params.setBinding(Globals.WebServices.SERVICE_TYPE_REST);
    params.setWebMethodName(Globals.WebMethods.GET_ALL_CONTACTS);
    params.setHttpMethod(Request.Method.GET);
    params.setIsCollection(true);
    params.setIsAuthenticated(false);
    params.setResultType(new TypeToken<List<Contact>>() {
    }.getType());

    InvokeWebserviceVolley<Contact> volleyRequest = new InvokeWebserviceVolley<Contact>(this, this,params);
    volleyRequest.execute();*/
}
