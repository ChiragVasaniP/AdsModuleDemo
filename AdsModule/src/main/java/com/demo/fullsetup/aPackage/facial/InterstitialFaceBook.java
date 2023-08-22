package com.demo.fullsetup.aPackage.facial;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.R;
import com.demo.fullsetup.ShowAds;
import com.demo.fullsetup.aPackage.gopo.InterstitialAdmobGoogle;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

public class InterstitialFaceBook {

    public static void showInterStillAds(Context context, ShowAds listener) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        InterstitialAd interstitialAd = new InterstitialAd(context, AdsSharedPref.getInstance(context).getInterstitialAdsFaceBook());
        AbstractAdListener adListener = new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                Log.e("TAG_Facebook_Inter", "Native ad failed to load: " + error.getErrorMessage());
                super.onError(ad, error);
                progressDialog.dismiss();
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB) {
                    InterstitialAdmobGoogle.loadInterstitialSingleShow(context, listener);
                }else {
                    listener.onAdsFinish();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
                progressDialog.dismiss();
                listener.onAdsFinish();
            }
        };

        InterstitialAd.InterstitialLoadAdConfig interstitialLoadAdConfig = interstitialAd.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        interstitialAd.loadAd(interstitialLoadAdConfig);


    }
}
