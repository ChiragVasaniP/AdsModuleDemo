package com.qa.adsshared.adsPackage.uis.actvity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.AdsShowingClass;
import com.qa.adsshared.adsPackage.uis.DisplayIntroImageAdapter;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class IntroActivity extends AppCompatActivity {
    TextView noti;
    TextView storage;
    private TextView btnNext;
    private TextView btnPrevious;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    List<Drawable> list = new ArrayList<>();
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            updateButtonVisibility(position);
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private DisplayIntroImageAdapter myViewPagerAdapter;
    private ViewPager viewPager;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    

        setContentView(R.layout.activity_intro);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnPrevious = (TextView) findViewById(R.id.btn_previous);




        AdsShowingClass.showNativeAds(IntroActivity.this,findViewById(R.id.frame_native_small),false);

        list.add(getResources().getDrawable(R.drawable.ic_walkthrough_two));
        list.add(getResources().getDrawable(R.drawable.ic_walkthrough_one));
        list.add(getResources().getDrawable(R.drawable.ic_walkthrough_three));



        myViewPagerAdapter = new DisplayIntroImageAdapter(IntroActivity.this, list);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        updateButtonVisibility(0);
        addBottomDots(0);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                if (currentPosition < myViewPagerAdapter.getCount() - 1) {
                    viewPager.setCurrentItem(currentPosition + 1);
                } else {
                    
                    launchHomeScreen();
                }
                updateButtonVisibility(currentPosition + 1);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                if (currentPosition > 0) {
                    viewPager.setCurrentItem(currentPosition - 1);
                }
                updateButtonVisibility(currentPosition - 1);
            }
        });

    }

    public void addBottomDots(int currentPage) {
        TextView[] textViewArr;
        dots = new TextView[list.size()];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();
        int i = 0;
        while (true) {
            textViewArr = dots;
            if (i >= textViewArr.length) {
                break;
            }
            textViewArr[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35.0f);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
            i++;
        }
        int i2 = textViewArr.length;
        if (i2 > 0) {
            textViewArr[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }


    public int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
    public int getPreviousItem(int i) {
        return viewPager.getCurrentItem() - i;
    }


    public void launchHomeScreen() {
        AdsSharedPref.getInstance(IntroActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOW_INTRO_SCREEN,true);
        setResult(1000);
        finish();

    }

    private void updateButtonVisibility(int currentPosition) {
        if (currentPosition == 0) {
            btnPrevious.setVisibility(View.INVISIBLE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
        }

        if (currentPosition == viewPager.getCurrentItem() - 1) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }



}
