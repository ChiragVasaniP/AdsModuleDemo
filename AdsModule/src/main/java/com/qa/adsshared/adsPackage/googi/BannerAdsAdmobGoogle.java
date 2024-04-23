package com.qa.adsshared.adsPackage.googi;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.adsPackage.ConsentSDK;
import com.qa.adsshared.adsPackage.appLo.BannerAdsAppLovin;
import com.qa.adsshared.adsPackage.faci.BannerAdsFaceBook;
import com.qa.adsshared.adsPackage.qureks.BannerAdsQureka;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

public class BannerAdsAdmobGoogle {

    public static void loadAdvanceBannerAds(Context context, FrameLayout bannerFrameLayout) {
        AdView adView = new AdView(context);
        adView.setAdUnitId(AdsSharedPref.getInstance(context).getBannerAdsGoogleAdmob());
        bannerFrameLayout.addView(adView);
        AdSize adSize = getAdSize(context);
        adView.setAdSize(adSize);
        adView.loadAd(ConsentSDK.getAdRequest(context));
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB) {
                    BannerAdsFaceBook.loadBanner90(context, bannerFrameLayout);
                } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA) {
                    BannerAdsFaceBook.loadBanner90(context, bannerFrameLayout);
                } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                    BannerAdsQureka.loadBannerQureka(context, bannerFrameLayout);
                }
                else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN) {
                    BannerAdsAppLovin.callBannerAd(context, bannerFrameLayout);
                }
            }
        });
    }


    private static AdSize getAdSize(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

}
