package com.demo.fullsetup.aPackage.gopo;

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
import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.aPackage.ConsentSDK;
import com.demo.fullsetup.aPackage.facial.BannerAdsFaceBook;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

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
