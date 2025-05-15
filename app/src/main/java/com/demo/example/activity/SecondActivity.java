package com.demo.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shared.ads.libs.adsPackage.AdsShowingClass;
import com.demo.example.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        AdsShowingClass.showNativeAds(SecondActivity.this,findViewById(R.id.frame_native_small),true);
        AdsShowingClass.showBannerAds(SecondActivity.this,findViewById(R.id.frame_banner));
    }
}