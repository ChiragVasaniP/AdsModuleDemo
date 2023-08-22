package com.demo.fullsetup.aPackage.uis.actvity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.R;
import com.demo.fullsetup.aPackage.AdsShowingClass;
import com.demo.fullsetup.aPackage.uis.DisplayIntroImageAdapter;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class IntroActivity extends AppCompatActivity {
    TextView storage, btnPrevious;
    private TextView btnNext;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    List<Drawable> list = new ArrayList<>();

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
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
        btnPrevious = findViewById(R.id.btn_previous);
        changeStatusBarColor();
        addBottomDots(0);

        AdsShowingClass.showNativeAds(IntroActivity.this, findViewById(R.id.frame_native_big), true);

        list.add(getResources().getDrawable(R.drawable.ic_walkthrough_one));
        list.add(getResources().getDrawable(R.drawable.ic_walkthrough_two));
        list.add(getResources().getDrawable(R.drawable.ic_walkthrough_three));

        myViewPagerAdapter = new DisplayIntroImageAdapter(IntroActivity.this, list);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(-1);
                if (current >= 0) {
                    viewPager.setCurrentItem(current);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(1);
                if (current < list.size()) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    public void addBottomDots(int currentPage) {
        TextView[] textViewArr;
        this.dots = new TextView[list.size()];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        this.dotsLayout.removeAllViews();
        int i = 0;
        while (true) {
            textViewArr = this.dots;
            if (i >= textViewArr.length) {
                break;
            }
            textViewArr[i] = new TextView(this);
            this.dots[i].setText(Html.fromHtml("&#8226;"));
            this.dots[i].setTextSize(35.0f);
            this.dots[i].setTextColor(colorsInactive[currentPage]);
            this.dotsLayout.addView(this.dots[i]);
            i++;
        }
        int i2 = textViewArr.length;
        if (i2 > 0) {
            textViewArr[currentPage].setTextColor(colorsActive[currentPage]);
        }

        // Update button visibility and text
        if (currentPage == 0) {
            // On the first screen, hide the Previous button
            btnPrevious.setVisibility(View.GONE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
        }

        if (currentPage == list.size() - 1) {
            // On the last screen, change Next button text to "Finish"
            btnNext.setText(getString(R.string.finish));
        } else {
            // On any other screen, set Next button text to "Next"
            btnNext.setText(getString(R.string.str_next));
        }
    }


    public int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    public void launchHomeScreen() {
        AdsSharedPref.getInstance(IntroActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOW_INTRO_SCREEN, true);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }
}
