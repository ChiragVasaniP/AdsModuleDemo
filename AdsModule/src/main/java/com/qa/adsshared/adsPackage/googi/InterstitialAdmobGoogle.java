package com.qa.adsshared.adsPackage.googi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.ShowAds;
import com.qa.adsshared.adsPackage.ConsentSDK;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

import java.util.ArrayList;
import java.util.List;

public class InterstitialAdmobGoogle {
    //    private static InterstitialAd interstitialAd;
    private static List<InterstitialAd> interstitialAdList;
    private static int MAX_AD_COUNT = 0; // Maximum number of ads to preload


    public static void loadInterstitialSingleShow(Context context, ShowAds interfaceAds) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        if (interstitialAdList != null) {
            if (interstitialAdList.size() > 0) {

                if (!(context instanceof AdActivity)){
                    InterstitialAd interstitialAd = interstitialAdList.get(interstitialAdList.size() - 1);
                    Log.e("TAG_Inter_unit_id", "loadInterstitialSingleShow: " + interstitialAd.getAdUnitId());

                    interstitialAdList.remove(interstitialAdList.size() - 1);
                    ShowInterTrailAds(context, interfaceAds, progressDialog, interstitialAd);
                }else{
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    interfaceAds.onAdsFinish();
                }


            } else {
                int TotalCount = AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.INTERSTITIAL_PRELOAD_COUNT);
                MAX_AD_COUNT = TotalCount;
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                preloadInterStialAds(context, interfaceAds, progressDialog);
            }
        } else {
            interstitialAdList = new ArrayList<>();
            int TotalCount = AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.INTERSTITIAL_PRELOAD_COUNT);
            MAX_AD_COUNT = TotalCount;
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            preloadInterStialAds(context, interfaceAds, progressDialog);
        }

    }

    public static void loadInterstitialSingleShowWithoutPreload(Context context, ShowAds interfaceAds) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        if (interstitialAdList != null) {
            if (interstitialAdList.size() > 0) {

                if (!(context instanceof AdActivity)){
                    InterstitialAd interstitialAd = interstitialAdList.get(interstitialAdList.size() - 1);
                    Log.e("TAG_Inter_unit_id", "loadInterstitialSingleShow: " + interstitialAd.getAdUnitId());

                    interstitialAdList.remove(interstitialAdList.size() - 1);
                    ShowInterTrailAds(context, interfaceAds, progressDialog, interstitialAd);
                }else{
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    interfaceAds.onAdsFinish();
                }


            } else {
                MAX_AD_COUNT = 1;
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                preloadInterStialAds(context, interfaceAds, progressDialog);
            }
        } else {
            interstitialAdList = new ArrayList<>();
            MAX_AD_COUNT = 1;
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            preloadInterStialAds(context, interfaceAds, progressDialog);
        }

    }

    public static void preloadInterStialAds(Context context, ShowAds interfaceAds, Dialog progressDialog) {
        InterstitialAd.load(context, AdsSharedPref.getInstance(context).getInterstitialAdsGoogleAdmob(), ConsentSDK.getAdRequest(context), new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                MAX_AD_COUNT--;
                if ((interstitialAdList.size()) != MAX_AD_COUNT) {
                    preloadInterStialAds(context, interfaceAds, progressDialog);
                } else if (MAX_AD_COUNT == 0) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    interfaceAds.onAdsFinish();
                }
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                interstitialAdList.add(interstitialAd);
                if ((interstitialAdList.size()) != MAX_AD_COUNT) {
                    preloadInterStialAds(context, interfaceAds, progressDialog);
                } else if (MAX_AD_COUNT == interstitialAdList.size()) {
                    ShowInterTrailAds(context, interfaceAds, progressDialog, interstitialAdList.remove(interstitialAdList.size() - 1));
                }
            }
        });
    }


    public static void ShowInterTrailAds(Context context, ShowAds interfaceAds, Dialog progressDialog, InterstitialAd interstitialAd) {
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interfaceAds.onAdsFinish();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }, 100);


            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                interfaceAds.onAdsFinish();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });
        interstitialAd.show((Activity) context);
    }

/*    public static void loadInterstitialSingleShow(final Context context, ShowAds interfaceAds) {
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
                            
                            
                            interstitialAd = minterstitialAd;
                            ShowInterTrailAds(context, interfaceAds, progressDialog);

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB) {
                                InterstitialFaceBook.showInterStillAds(context, interfaceAds);
                            } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA){
                                InterstitialFaceBook.showInterStillAds(context, interfaceAds);
                            } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                                InterstitialQuark.loadInterstitialQuark(context, interfaceAds);
                            }else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN) {
                               InterstitialAppLovin.loadAppLovinInterstitial(context, interfaceAds);
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
    }*/
}



