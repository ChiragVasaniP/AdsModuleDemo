package com.demo.fullsetup.aPackage.gopo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.R;
import com.demo.fullsetup.ShowAds;
import com.demo.fullsetup.aPackage.ConsentSDK;
import com.demo.fullsetup.aPackage.facial.InterstitialFaceBook;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

public class InterstitialAdmobGoogle {
    private static InterstitialAd interstitialAd;
    /*   private static int userClick = 1;*/

    public static void ShowInterTrailAds(Context context, ShowAds interfaceAds, Dialog progressDialog) {
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                interstitialAd = null;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interfaceAds.onAdsFinish();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }, 100);

                // Called when fullscreen content is dismissed.
//                loadInterstitial(context);

            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                interstitialAd = null;
                interfaceAds.onAdsFinish();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // Called when fullscreen content failed to show.
//                loadInterstitial(context);

            }

            @Override
            public void onAdShowedFullScreenContent() {
//                interfaceAds.onAdsFinish();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });
        interstitialAd.show((Activity) context);
    }

    public static void loadInterstitialSingleShow(final Context context, ShowAds interfaceAds) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        if (interstitialAd == null) {
            InterstitialAd.load(context, AdsSharedPref.getInstance(context).getInterstitialAdsGoogleAdmob(), ConsentSDK.getAdRequest(context),
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd minterstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            interstitialAd = minterstitialAd;
                            ShowInterTrailAds(context, interfaceAds, progressDialog);

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB) {
                                InterstitialFaceBook.showInterStillAds(context, interfaceAds);
                            }
                            else{
                                interstitialAd = null;
                                interfaceAds.onAdsFinish();
                            }
                        }
                    });
        } else {
            ShowInterTrailAds(context, interfaceAds, progressDialog);
        }
    }
}



