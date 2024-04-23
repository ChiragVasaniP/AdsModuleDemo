package com.demo.example.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.BuildConfig;
import com.demo.example.MyApp;
import com.demo.example.R;
import com.demo.example.activity.MainActivity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.ShowAds;
import com.qa.adsshared.adsPackage.AdsShowingClass;
import com.qa.adsshared.adsPackage.googi.AppOpenAdManager;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;
import com.qa.adsshared.adsPackage.utils.InternetChecker;


public class SplashActivity extends AppCompatActivity {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private final String TAG = "SplashScreenActivity";
    String selected_lan = "english";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(1024, 1024);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        if (InternetChecker.getInstance().isConnectingToInternet(SplashActivity.this)) {
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Config params updated: Success");

                        } else {
                            Log.d(TAG, "Config params updated: Failed");
                        }

//                        Log.e("Tag__SharedPref", "onComplete: " + AdsSharedPref.getInstance(LaunchActivity.this).getString(FirebaseConfigConst.VERSION));
                        fetchConfigData(mFirebaseRemoteConfig);
                    });
        } else {
            navigateToStart();
        }

    }

    private void fetchConfigData(FirebaseRemoteConfig mFirebaseRemoteConfig) {

        AdsShowingClass.initLiseAdsSdk(SplashActivity.this);

        //Base App Database
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.BASE_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.BASE_URL));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.DEV_KEY, mFirebaseRemoteConfig.getString(FirebaseConfigConst.DEV_KEY));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.IMAGE_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.IMAGE_URL));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.REWARD_COINS, mFirebaseRemoteConfig.getString(FirebaseConfigConst.REWARD_COINS));

        //App Api Path
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETSETTINGS, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETSETTINGS));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_REGISTERUSER, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_REGISTERUSER));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_LOGOUTUSER, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_LOGOUTUSER));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETUSERDETAIL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETUSERDETAIL));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETCOUNTRYLIST, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETCOUNTRYLIST));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETGIRLSLIST, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETGIRLSLIST));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETCOMMENTS, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETCOMMENTS));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETGIFTCATEGORY, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETGIFTCATEGORY));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETGIFTS, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETGIFTS));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETRANDOMVIDEO, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETRANDOMVIDEO));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETBRANDINGIMAGES, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETBRANDINGIMAGES));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETCOINPACKAGES, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETCOINPACKAGES));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETOFFERCOINSPAKAGES, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETOFFERCOINSPAKAGES));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_ADDCOIN, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_ADDCOIN));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_LESSCOIN, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_LESSCOIN));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETMESSAGEUSERLIST, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETMESSAGEUSERLIST));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.API_GETMESSAGELIST, mFirebaseRemoteConfig.getString(FirebaseConfigConst.API_GETMESSAGELIST));




//Commons Config Id's
        //if flag true then avs show if not then avd not showing if any test or live
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_ADD_SHOW, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_ADD_SHOW));
        //if app is live mode then app showing live id's and if not then showing test avd
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_LIVEMOD, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_LIVEMOD));

        /*Use for testing*/
//        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_LIVEMOD, false);
//        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_ADD_SHOW, true);

        //Used for googi avd id's
        AdsSharedPref.getInstance(SplashActivity.this).setNativeAdvancedAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setInterstitialAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_INTERSTITIAL));
        AdsSharedPref.getInstance(SplashActivity.this).setAppOpenAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_APP_OPEN));
        AdsSharedPref.getInstance(SplashActivity.this).setBannerAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_BANNER));
        AdsSharedPref.getInstance(SplashActivity.this).setRewardAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_REWARD));

        //Used for Faci Avd id's
        AdsSharedPref.getInstance(SplashActivity.this).setNativeAdvancedAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setInterstitialAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_INTERSTITIAL));
        AdsSharedPref.getInstance(SplashActivity.this).setBannerAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_BANNER));
        AdsSharedPref.getInstance(SplashActivity.this).setRewardAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_REWARD));

        //Used for Faci Avd id's
        AdsSharedPref.getInstance(SplashActivity.this).setNativeAdvancedAdsApplovinPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.APPLOVIN_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setInterstitialAdsApplovinPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.APPLOVIN_INTERSTITIAL));
        AdsSharedPref.getInstance(SplashActivity.this).setBannerAdsApplovinPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.APPLOVIN_BANNER));
        AdsSharedPref.getInstance(SplashActivity.this).setRewardAdsApplovinPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.APPLOVIN_REWARD));
        //Preload Counter For Native and interstial
        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.INTERSTITIAL_PRELOAD_COUNT, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.INTERSTITIAL_PRELOAD_COUNT));
        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.NATIVE_PRELOAD_COUNT, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.NATIVE_PRELOAD_COUNT));

        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_APP_OPEN_CHANGE_INTERSTITIAL, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_APP_OPEN_CHANGE_INTERSTITIAL));


        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_ADS_MUTE, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_ADS_MUTE));



        //Back press Click Time Ads Showing or not
        AdsSharedPref.getInstance(SplashActivity.this).setBackPressInterShow(mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_BACK_PRESS_INTER_SHOW));
        //Skip Ads For Activity
        AdsSharedPref.getInstance(SplashActivity.this).setSkipAdsActivityArray(mFirebaseRemoteConfig.getString(FirebaseConfigConst.SKIP_ADS_ACTIVITY_ARRAY));
        //Check Wiche Ads Show Click Time
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.CLICK_TIME_ADS_TYPE_NAME, mFirebaseRemoteConfig.getString(FirebaseConfigConst.CLICK_TIME_ADS_TYPE_NAME));

        //Used for interStials ads Click
//        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.INTERSTITIAL_ADS_COUNTER, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.INTERSTITIAL_ADS_COUNTER));

        //Used for ads Sequance Here
        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.ADS_SEQUENCE, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.ADS_SEQUENCE));

        //Click Counter
        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.AD_CLICK_AND_INTERVAL_COUNTER, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.AD_CLICK_AND_INTERVAL_COUNTER));

        //turn on or off all inters avd quereka,facebook,google all of them if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF));

        //turn on or off all native avd quereka,facebook,google all of them if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF));

        //turn on or off all banner avd quereka,facebook,google all of them if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.BANNER_ALL_ADS_ON_OFF, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.BANNER_ALL_ADS_ON_OFF));

        //if flag on then ads showing in splash activity or if flag of then skip ads in splash time
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOWING_SPLASH_ADS, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOWING_SPLASH_ADS));

        //turn on or off all appopen google if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOWING_APP_OPEN, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOWING_APP_OPEN));

        //if flag true then showing native ads in exit screen if not then not showing
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_EXIT_DIALOG_NATIVE_SHOW, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_EXIT_DIALOG_NATIVE_SHOW));

        //if flag true then showing native ads in exit screen if not then not showing
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_EXIT_DIALOG_INTERSTITIAL_SHOW, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_EXIT_DIALOG_INTERSTITIAL_SHOW));

        //if data get interstial then showin inter and if getting appopen then showing app open ads
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.GET_SPLASH_ADS_TYPE, mFirebaseRemoteConfig.getString(FirebaseConfigConst.GET_SPLASH_ADS_TYPE));


        //Quereka Ads Banner Native, Interstitial avd url
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.QUREKA_CLICK_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.QUREKA_CLICK_URL));

        //Quereka Ads Image Banner Native, Interstitial avd url
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.QUREKA_NATIVE_IMAGE_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.QUREKA_NATIVE_IMAGE_URL));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.QUREKA_BANNER_IMAGE_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.QUREKA_BANNER_IMAGE_URL));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.QUREKA_INTERSTITIAL_IMAGE_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.QUREKA_INTERSTITIAL_IMAGE_URL));


        //New Added Config for vpn and other functionally
//        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_EXIT_DIALOG_ADS, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_EXIT_DIALOG_ADS));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.APP_VERSION, mFirebaseRemoteConfig.getString(FirebaseConfigConst.APP_VERSION));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS, mFirebaseRemoteConfig.getString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS));

        /*testing time disable ad interval*/
//        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS, (int) 0);

        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_APP_UPDATE_FORCEFULLY_NOT, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_APP_UPDATE_FORCEFULLY_NOT));
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.PRIVACY_POLICY_URL, mFirebaseRemoteConfig.getString(FirebaseConfigConst.PRIVACY_POLICY_URL));

        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_BANNER_CHANGE_NATIVE, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_BANNER_CHANGE_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER));
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOW_RATE_US_DIALOG, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOW_RATE_US_DIALOG));
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_EXIT_DIALOG_SHOW, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_EXIT_DIALOG_SHOW));


        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.SPLASH_ADS_LOAD_TIME, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.SPLASH_ADS_LOAD_TIME));


        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.TAB_SELECTION_COUNTER_APP_OPEN, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.TAB_SELECTION_COUNTER_APP_OPEN));
//        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.APP_OPEN_LOAD_TIME_IN_SPLASH, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.APP_OPEN_LOAD_TIME_IN_SPLASH));
//        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.NON_REWARD_ARRAY_COUNTER, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.NON_REWARD_ARRAY_COUNTER));
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOW_TAB_APP_OPEN, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOW_TAB_APP_OPEN));

        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOW_INTERNET_DIALOG, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOW_INTERNET_DIALOG));

//        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOWING_DASHBOARD, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOWING_DASHBOARD));

        //Check Logic for ads showing in updated app in testing time
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.APP_VERSION_NAME_IN_UPDATE_PLAY_STORE, mFirebaseRemoteConfig.getString(FirebaseConfigConst.APP_VERSION_NAME_IN_UPDATE_PLAY_STORE));
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_APP_NEW_VERSION_UNDER_TESTING_PS, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_APP_NEW_VERSION_UNDER_TESTING_PS));


        if (BuildConfig.VERSION_NAME.equalsIgnoreCase(AdsSharedPref.getInstance(SplashActivity.this).getString(FirebaseConfigConst.APP_VERSION_NAME_IN_UPDATE_PLAY_STORE))
                && AdsSharedPref.getInstance(SplashActivity.this).getABoolean(FirebaseConfigConst.IS_APP_NEW_VERSION_UNDER_TESTING_PS)) {
            AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_ADD_SHOW, false);
        }

        appOpenAdsLoad();

    }


    public void appOpenAdsLoad() {
        new AdsSharedPref(SplashActivity.this);
        Application application = getApplication();
        if (AdsSharedPref.getInstance(SplashActivity.this).getABoolean(FirebaseConfigConst.IS_SHOWING_SPLASH_ADS)) {
            if (AdsSharedPref.getInstance(SplashActivity.this).getString(FirebaseConfigConst.GET_SPLASH_ADS_TYPE).equalsIgnoreCase("AppOpen")) {
                if (!(application instanceof MyApp)) {
                    navigateToStart();
                    return;
                }
                ((MyApp) application).loadOpenAds(SplashActivity.this);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MyApp) application)
                                .showAdIfAvailableForSplash(
                                        SplashActivity.this,
                                        new AppOpenAdManager.OnShowAdCompleteListener() {
                                            @Override
                                            public void onShowAdComplete() {
                                                navigateToStart();
                                            }
                                        });
                    }
                }, AdsSharedPref.getInstance(SplashActivity.this).getInt(FirebaseConfigConst.SPLASH_ADS_LOAD_TIME));

            } else if (AdsSharedPref.getInstance(SplashActivity.this).getString(FirebaseConfigConst.GET_SPLASH_ADS_TYPE).equalsIgnoreCase("Interstitial")) {
                AdsShowingClass.showInterStillAdsForClick(SplashActivity.this, new ShowAds() {
                    @Override
                    public void onAdsFinish() {
                        navigateToStart();
                    }
                });
            } else {
                navigateToStart();
            }

        } else {
            navigateToStart();
        }
    }


    public void navigateToStart() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 500);
    }
}
