package com.demo.fullsetup.aPackage.gopo;

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

import java.util.Date;

import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.aPackage.ConsentSDK;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

public class AppOpenAdManager {

    private static final String LOG_TAG = "AppOpenAdManager_TAG";
    Context context;


    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;


    /**
     * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
     */
    private long loadTime = 0;

    /**
     * Constructor.
     *
     * @param context
     */
    public AppOpenAdManager(Context context) {
        this.context = context;
        /*            private static final String AD_UNIT_ID = */
    }

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    public void loadAd(Context context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request =/* new AdRequest.Builder().build();*/getAdRequest();
        AppOpenAd.load(
                context,
                AdsSharedPref.getInstance(context).getAppOpenAdsGoogleAdmob(),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();

                        Log.d(LOG_TAG, "onAdLoaded.");
//                            Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                            Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadAdWithListener(Context context, OnLoadAdCompleteListener onLoadAdCompleteListener) {
        // Do not load ad if there is an unused ad or one is already loading.
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
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();
                        onLoadAdCompleteListener.onLoadAdComplete();

                        Log.d(LOG_TAG, "onAdLoaded.");
//                            Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isLoadingAd = false;
                        onLoadAdCompleteListener.onLoadAdComplete();
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                            Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
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

    /**
     * Check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Check if ad exists and can be shown.
     */
    private boolean isAdAvailable() {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    public void showAdIfAvailable(@NonNull final Activity activity) {
        if (!(activity instanceof AdActivity)) {
            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            // Empty because the user will go back to the activity that shows the ad.
                        }
                    });
        }

    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull final Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (!(activity instanceof AdActivity) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_SHOWING_APP_OPEN)) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//                            Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                            Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                            Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
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
        if (!(activity instanceof AdActivity) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT) ) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//                            Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//                            Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show();

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                            Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show();
                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);

        } else {
            onShowAdCompleteListener.onShowAdComplete();
        }

    }
    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public interface OnLoadAdCompleteListener {
        void onLoadAdComplete();
    }

    /**
     * Inner class that loads and shows app open ads.
     */


}