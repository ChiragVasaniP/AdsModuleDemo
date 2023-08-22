package com.demo.fullsetup.aPackage;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.demo.fullsetup.BuildConfig;
import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.ShowAds;
import com.demo.fullsetup.aPackage.facial.BannerAdsFaceBook;
import com.demo.fullsetup.aPackage.facial.InterstitialFaceBook;
import com.demo.fullsetup.aPackage.facial.NativeFacebook;
import com.demo.fullsetup.aPackage.gopo.BannerAdsAdmobGoogle;
import com.demo.fullsetup.aPackage.gopo.InterstitialAdmobGoogle;
import com.demo.fullsetup.aPackage.gopo.NativeAdmobGoogle;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;
import com.demo.fullsetup.aPackage.utils.InternetChecker;

public class AdsShowingClass {

    public static int InterStillCounter = 1;
    private static long mLastClickTime = 0;

    public static void showInterStillAdsForClick(Context context, ShowAds adsListener) {
        initLiseFaceBookAds(context);

        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT)) {
            if ((SystemClock.elapsedRealtime() - mLastClickTime) < convertMinutesToMilliseconds(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS))) {
                adsListener.onAdsFinish();
            } else {
                if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF)) {
                    if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                        mLastClickTime = SystemClock.elapsedRealtime();
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                                InterstitialAdmobGoogle.loadInterstitialSingleShow(context, adsListener);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                                InterstitialFaceBook.showInterStillAds(context, adsListener);
                                break;

                        }
                    } else {
                        adsListener.onAdsFinish();
                    }
                } else {
                    adsListener.onAdsFinish();
                }
            }

        } else {
            adsListener.onAdsFinish();
        }


    }

    public static void showInterStillAds(Context context, ShowAds adsListener) {
        initLiseFaceBookAds(context);

        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT)) {
            if ((SystemClock.elapsedRealtime() - mLastClickTime) < convertMinutesToMilliseconds(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS))) {
                adsListener.onAdsFinish();
            } else {
                if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF)) {
                    if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                        if (InterStillCounter == AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_CLICK_COUNTER)) {
                            InterStillCounter = 1;
                            mLastClickTime = SystemClock.elapsedRealtime();
                            switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                                case FirebaseConfigConst.SHOW_ADMOB:
                                case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                                    InterstitialAdmobGoogle.loadInterstitialSingleShow(context, adsListener);
                                    break;
                                case FirebaseConfigConst.SHOW_FB:
                                case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                                    InterstitialFaceBook.showInterStillAds(context, adsListener);
                                    break;

                            }
                        } else {
                            adsListener.onAdsFinish();
                            InterStillCounter++;
                        }
                    } else {
                        adsListener.onAdsFinish();
                    }
                } else {
                    adsListener.onAdsFinish();
                }
            }

        } else {
            adsListener.onAdsFinish();
        }


    }

    public static void showNativeAds(Context context, FrameLayout frameLayout, boolean isShowBig) {
        initLiseFaceBookAds(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT)) {
            if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                if (!isShowBig && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER)) {
                    showBannerAds(context, frameLayout);
                } else {
                    if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF)) {
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                                frameLayout.removeAllViews();
                                NativeAdmobGoogle.loadNativeAds(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                                NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                                break;
                        }
                    } else {
                        frameLayout.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                frameLayout.setVisibility(View.INVISIBLE);
            }
        } else {
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    public static void showBannerAds(Context context, FrameLayout frameLayout) {
        initLiseFaceBookAds(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT)) {
            if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_BANNER_CHANGE_NATIVE)) {
                    showNativeAds(context, frameLayout, false);
                } else {
                    if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.BANNER_ALL_ADS_ON_OFF)) {
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                                BannerAdsAdmobGoogle.loadAdvanceBannerAds(context, frameLayout);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                                BannerAdsFaceBook.loadBanner90(context, frameLayout);
                                break;
                        }
                    } else {
                        frameLayout.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                frameLayout.setVisibility(View.INVISIBLE);
            }

        } else {
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    public static void initLiseFaceBookAds(Context context) {

        //Google
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        //Facebook
        AudienceNetworkAds.initialize(context);
        AudienceNetworkAds.buildInitSettings(context)
                .withInitListener(new AudienceNetworkAds.InitListener() {
                    @Override
                    public void onInitialized(AudienceNetworkAds.InitResult initResult) {

                    }
                })
                .initialize();

        if (BuildConfig.DEBUG) {
            AdSettings.setTestMode(true);
        }
    }

    public static String getActivityName(Context context) {
        if (context == null) {
            return ""; // Return an empty string if context is null
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Log.i("CURRENT_Activity ", activity.getClass().getSimpleName());
            return activity.getClass().getSimpleName();
        } else if (context instanceof ContextWrapper) {
            // Try to get the base context
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                Activity activity = (Activity) baseContext;
                Log.i("CURRENT_Activity ", activity.getClass().getSimpleName());
                return activity.getClass().getSimpleName();
            }
        }

        // Return an empty string here (or handle differently if needed)
        return "";
    }

     static long convertMinutesToMilliseconds(String minutesString) {
        try {
            double minutes = Double.parseDouble(minutesString);
            long milliseconds = (long) (minutes * 60_000L); // Convert minutes to milliseconds

            return milliseconds;
        } catch (NumberFormatException e) {
            // Handle parsing errors, e.g., invalid minutes format
            e.printStackTrace();
            return 1; // Return 1 to indicate an error
        }
    }
}