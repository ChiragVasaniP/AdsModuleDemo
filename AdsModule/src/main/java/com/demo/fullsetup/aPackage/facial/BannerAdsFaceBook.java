package com.demo.fullsetup.aPackage.facial;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.aPackage.gopo.BannerAdsAdmobGoogle;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

public class BannerAdsFaceBook {

    public static void loadBanner90(Context context, FrameLayout adContainer) {

        AdView adView = new AdView(context, AdsSharedPref.getInstance(context).getBannerAdsFaceBook(), AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)==FirebaseConfigConst.FB_FAIL_SHOW_ADMOB){
                    BannerAdsAdmobGoogle.loadAdvanceBannerAds(context,adContainer);
                }
                Log.e("FACEBOOK_BANNER_90", "onAdLoaded: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.e("FACEBOOK_BANNER_90", "onAdLoaded: " + "Ad Loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        adView.loadAd(loadAdConfig);
    }
}
