package com.qa.adsshared.adsPackage;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdInspectorError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnAdInspectorClosedListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.ShowAds;
import com.qa.adsshared.adsPackage.appLo.BannerAdsAppLovin;
import com.qa.adsshared.adsPackage.appLo.InterstitialAppLovin;
import com.qa.adsshared.adsPackage.appLo.NativeAppLovin;
import com.qa.adsshared.adsPackage.faci.BannerAdsFaceBook;
import com.qa.adsshared.adsPackage.faci.InterstitialFaceBook;
import com.qa.adsshared.adsPackage.faci.NativeFacebook;
import com.qa.adsshared.adsPackage.googi.BannerAdsAdmobGoogle;
import com.qa.adsshared.adsPackage.googi.InterstitialAdmobGoogle;
import com.qa.adsshared.adsPackage.googi.NativeAdmobGoogle;
import com.qa.adsshared.adsPackage.qureks.BannerAdsQureka;
import com.qa.adsshared.adsPackage.qureks.InterstitialQuark;
import com.qa.adsshared.adsPackage.qureks.NativeQureka;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;
import com.qa.adsshared.adsPackage.utils.InternetChecker;

import java.util.Arrays;
import java.util.List;

public class AdsShowingClass {

    public static int InterStillCounter = 1;
    public static long mLastClickTime = 0;

    public static void showInterStillAdsAppClass(Context context, ShowAds adsListener) {
        initLiseAdsSdk(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF)) {
                if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                    switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                        case FirebaseConfigConst.SHOW_ADMOB:
                        case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                        case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                        case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                            InterstitialAdmobGoogle.loadInterstitialSingleShow(context, adsListener);
                            break;
                        case FirebaseConfigConst.SHOW_FB:
                        case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                        case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                            InterstitialFaceBook.showInterStillAds(context, adsListener);
                            break;
                        case FirebaseConfigConst.SHOW_QUEREKA:
                        case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                        case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                            InterstitialQuark.loadInterstitialQuark(context, adsListener);
                            break;
                        case FirebaseConfigConst.SHOW_APPLOVIN:
                            InterstitialAppLovin.loadAppLovinInterstitial(context, adsListener);
                            break;
                    }
                } else {
                    adsListener.onAdsFinish();
                }
            } else {
                adsListener.onAdsFinish();
            }

        } else {
            adsListener.onAdsFinish();
        }
    }

    public static void showInterStillAdsForClick(Context context, ShowAds adsListener) {
        initLiseAdsSdk(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if ((SystemClock.elapsedRealtime() - mLastClickTime) < convertMinuteToMilliseconds(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS))) {
                adsListener.onAdsFinish();
            } else {
                if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF)) {
                    if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                        mLastClickTime = SystemClock.elapsedRealtime();
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                                InterstitialAdmobGoogle.loadInterstitialSingleShow(context, adsListener);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                                InterstitialFaceBook.showInterStillAds(context, adsListener);
                                break;
                            case FirebaseConfigConst.SHOW_QUEREKA:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                                InterstitialQuark.loadInterstitialQuark(context, adsListener);
                                break;
                            case FirebaseConfigConst.SHOW_APPLOVIN:
                                InterstitialAppLovin.loadAppLovinInterstitial(context, adsListener);
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

    public static void showInterStillAdsForNoPreload(Context context, ShowAds adsListener) {
        initLiseAdsSdk(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF)) {
                if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                    mLastClickTime = SystemClock.elapsedRealtime();
                    switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                        case FirebaseConfigConst.SHOW_ADMOB:
                        case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                        case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                        case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                            InterstitialAdmobGoogle.loadInterstitialSingleShowWithoutPreload(context, adsListener);
                            break;
                        case FirebaseConfigConst.SHOW_FB:
                        case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                        case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                            InterstitialFaceBook.showInterStillAds(context, adsListener);
                            break;
                        case FirebaseConfigConst.SHOW_QUEREKA:
                        case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                        case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                            InterstitialQuark.loadInterstitialQuark(context, adsListener);
                            break;
                        case FirebaseConfigConst.SHOW_APPLOVIN:
                            InterstitialAppLovin.loadAppLovinInterstitial(context, adsListener);
                            break;
                    }
                } else {
                    adsListener.onAdsFinish();
                }
            } else {
                adsListener.onAdsFinish();
            }

        } else {
            adsListener.onAdsFinish();
        }


    }

    public static void showInterStillAds(Context context, ShowAds adsListener) {
        initLiseAdsSdk(context);

        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if ((SystemClock.elapsedRealtime() - mLastClickTime) < convertMinuteToMilliseconds(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS))) {
                adsListener.onAdsFinish();
            } else {
                if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF)) {
                    if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                        if (InterStillCounter == AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.AD_CLICK_AND_INTERVAL_COUNTER)) {
                            InterStillCounter = 1;
                            mLastClickTime = SystemClock.elapsedRealtime();
                            switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                                case FirebaseConfigConst.SHOW_ADMOB:
                                case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                                case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                                case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                                    InterstitialAdmobGoogle.loadInterstitialSingleShow(context, adsListener);
                                    break;
                                case FirebaseConfigConst.SHOW_FB:
                                case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                                case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                                    InterstitialFaceBook.showInterStillAds(context, adsListener);
                                    break;
                                case FirebaseConfigConst.SHOW_QUEREKA:
                                case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                                case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                                    InterstitialQuark.loadInterstitialQuark(context, adsListener);
                                    break;
                                case FirebaseConfigConst.SHOW_APPLOVIN:
                                    InterstitialAppLovin.loadAppLovinInterstitial(context, adsListener);
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
        initLiseAdsSdk(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                if (!isShowBig && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER)) {
                    showBannerAds(context, frameLayout);
                } else {
                    if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF)) {
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                                frameLayout.removeAllViews();
                                NativeAdmobGoogle.loadNativeAds(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                                NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_QUEREKA:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                                NativeQureka.loadNativeQureka(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_APPLOVIN:
                                NativeAppLovin.loadAppLovinNativeBannerAd(context, frameLayout);
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

    public static void showNativeAdsNoPreload(Context context, FrameLayout frameLayout, boolean isShowBig) {
        initLiseAdsSdk(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                if (!isShowBig && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER)) {
                    showBannerAds(context, frameLayout);
                } else {
                    if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF)) {
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                                frameLayout.removeAllViews();
                                NativeAdmobGoogle.loadNativeAdsWithoutPreload(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                                NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_QUEREKA:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                                NativeQureka.loadNativeQureka(context, frameLayout, isShowBig);
                                break;
                            case FirebaseConfigConst.SHOW_APPLOVIN:
                                NativeAppLovin.loadAppLovinNativeBannerAd(context, frameLayout);
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
        initLiseAdsSdk(context);
        if (InternetChecker.getInstance().isConnectingToInternet(context) && AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (!AdsSharedPref.getInstance(context).getSkipAdsActivityArray().contains(getActivityName(context))) {
                if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_BANNER_CHANGE_NATIVE)) {
                    showNativeAds(context, frameLayout, false);
                } else {
                    if (AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.BANNER_ALL_ADS_ON_OFF)) {
                        switch (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)) {
                            case FirebaseConfigConst.SHOW_ADMOB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA:
                            case FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN:
                                BannerAdsAdmobGoogle.loadAdvanceBannerAds(context, frameLayout);
                                break;
                            case FirebaseConfigConst.SHOW_FB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB:
                            case FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA:
                                BannerAdsFaceBook.loadBanner90(context, frameLayout);
                                break;
                            case FirebaseConfigConst.SHOW_QUEREKA:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_FB:
                            case FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB:
                                BannerAdsQureka.loadBannerQureka(context, frameLayout);
                                break;
                            case FirebaseConfigConst.SHOW_APPLOVIN:
                                BannerAdsAppLovin.callBannerAd(context, frameLayout);
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

    public static void initLiseAdsSdk(Context context) {

/*
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.initializeSdk(context, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {

            }
        });
*/


        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                MobileAds.setAppVolume(0.0f);
                MobileAds.setAppMuted(AdsSharedPref.getInstance(context).getABoolean(FirebaseConfigConst.IS_ADS_MUTE));
            }
        });
        MobileAds.openAdInspector(context, new OnAdInspectorClosedListener() {
            public void onAdInspectorClosed(@Nullable AdInspectorError error) {
                // Error will be non-null if ad inspector closed due to an error.
            }
        });
  /*      List<String> testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);*/


    /*    AudienceNetworkAds.initialize(context);
        AudienceNetworkAds.buildInitSettings(context)
                .withInitListener(new AudienceNetworkAds.InitListener() {
                    @Override
                    public void onInitialized(AudienceNetworkAds.InitResult initResult) {

                    }
                })
                .initialize();*/
    }


    public static long convertMinuteToMilliseconds(String minute) {
        try {
            float minuteFloat = Float.parseFloat(minute);
            long milliseconds = (long) (minuteFloat * 60 * 1000);
            return milliseconds;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getActivityName(Context context) {
        Activity activity = (Activity) context;
        activity.getClass().getName();
        Log.i("CURRENT_Activity ", activity.getClass().getSimpleName());
        return activity.getClass().getSimpleName();
    }
}