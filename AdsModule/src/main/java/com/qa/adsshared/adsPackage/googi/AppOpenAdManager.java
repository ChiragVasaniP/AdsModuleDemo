package com.qa.adsshared.adsPackage.googi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.adsPackage.ConsentSDK;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

import java.util.Date;

public class AppOpenAdManager {

    private static final String LOG_TAG = "AppOpenAdManager_TAG";
    Context context;


    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;


    
    private long loadTime = 0;

    
    public AppOpenAdManager(Context context) {
        this.context = context;
        
    }

    
    public void loadAd(Context context) {
        
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request =getAdRequest();
        AppOpenAd.load(
                context,
                AdsSharedPref.getInstance(context).getAppOpenAdsGoogleAdmob(),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();

                        Log.d(LOG_TAG, "onAdLoaded.");

                    }

                    
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                    }
                });
    }

    public void loadAdWithListener(Context context, OnLoadAdCompleteListener onLoadAdCompleteListener) {
        
        if (isLoadingAd || isAdAvailable()) {
            onLoadAdCompleteListener.onLoadAdComplete();
            return;
        }

        isLoadingAd = true;
        AdRequest request =getAdRequest();
        AppOpenAd.load(
                context,
                AdsSharedPref.getInstance(context).getAppOpenAdsGoogleAdmob(),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();
                        onLoadAdCompleteListener.onLoadAdComplete();

                        Log.d(LOG_TAG, "onAdLoaded.");

                    }

                    
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;
                        onLoadAdCompleteListener.onLoadAdComplete();
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                    }
                });
    }



    private AdRequest getAdRequest() {
        try {
            return ConsentSDK.getAdRequest(context);
        } catch (Exception e) {
            return new AdRequest.Builder().build();
        }
    }

    
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    
    private boolean isAdAvailable() {
        
        
        
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    
    public void showAdIfAvailable(@NonNull final Activity activity) {
        if (!(activity instanceof AdActivity)) {
            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            
                        }
                    });
        }

    }

    
    public void showAdIfAvailable(
            @NonNull final Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (!(activity instanceof AdActivity) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_SHOWING_APP_OPEN)) {
            
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");


                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());


                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");

                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);

        } else {
            onShowAdCompleteListener.onShowAdComplete();
        }

    }


    public void showAdIfAvailableForSplashScreen(
            @NonNull final Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (!(activity instanceof AdActivity) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW) && AdsSharedPref.getInstance(activity).getString(FirebaseConfigConst.GET_SPLASH_ADS_TYPE).equalsIgnoreCase("AppOpen")) {
            
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");


                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());


                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");

                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);

        } else {
            onShowAdCompleteListener.onShowAdComplete();
        }

    }


    
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public interface OnLoadAdCompleteListener {
        void onLoadAdComplete();
    }

    


}