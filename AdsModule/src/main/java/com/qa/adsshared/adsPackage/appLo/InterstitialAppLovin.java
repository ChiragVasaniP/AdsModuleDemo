package com.qa.adsshared.adsPackage.appLo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.qa.adsshared.R;
import com.qa.adsshared.ShowAds;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

public class InterstitialAppLovin {

    public static void loadAppLovinInterstitial(Context context, ShowAds listner) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        MaxInterstitialAd appLovinInterstitial1 = new MaxInterstitialAd(AdsSharedPref.getInstance(context).getInterstitialAdsApplovin(), (Activity) context);
        appLovinInterstitial1.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                appLovinInterstitial1.showAd();

            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                progressDialog.dismiss();

            }

            @Override
            public void onAdHidden(MaxAd ad) {

                listner.onAdsFinish();
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                progressDialog.dismiss();
                onAddFailedMethod(listner);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                progressDialog.dismiss();
                onAddFailedMethod(listner);
            }
        });
        appLovinInterstitial1.loadAd();
    }

    static void onAddFailedMethod(ShowAds listner) {

        listner.onAdsFinish();
    }

}
