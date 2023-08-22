package com.demo.fullsetup.aPackage.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.R;

public class AdsSharedPref {

    public static String SHARED_PREF_ADS = "jeniAdsSetup1110";

    //Google Key
    private static final String NativeAdvancedAdsGoogleAdmob = "NativeAdvancedAdsPrefGoogleAdmob";
    private static final String InterstitialAdsGoogleAdmob = "InterstitialAdsPreGoogleAdmob";
    private static final String AppOpenAdsGoogleAdmob = "AppOpenAdsPrefBackPro";
    private static final String BannerAdsGoogleAdmob = "BannerAdsPrefGoogleAdmob";

    //FaceBook Key
    private static final String NativeAdvancedAdsFaceBook = "NativeAdvancedAdsPrefFaceBook";
    private static final String InterstitialAdsFaceBook = "InterstitialAdsPreFaceBook";
    private static final String BannerAdsFaceBook = "BannerAdsPrefFaceBook";




    @SuppressLint("StaticFieldLeak")
    static AdsSharedPref INSTANCE = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;


    public AdsSharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_ADS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.context = context;
    }

    public static AdsSharedPref getInstance(Context context) {
        // Check if the instance is already created
        if (INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized (AdsSharedPref.class) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = new AdsSharedPref(context);
                }
            }
        }
        // return the singleton instance
        return INSTANCE;
    }

    //Admob's method
    public void setNativeAdvancedAdsGoogleAdmobPrf(String banner_ads_id) {
        editor.putString(NativeAdvancedAdsGoogleAdmob, banner_ads_id);
        editor.commit();
    }

    public void setInterstitialAdsGoogleAdmobPrf(String interstitial_ads_id) {
        editor.putString(InterstitialAdsGoogleAdmob, interstitial_ads_id);
        editor.commit();

    }

    public void setAppOpenAdsGoogleAdmobPrf(String appOpen_ads_id) {
        editor.putString(AppOpenAdsGoogleAdmob, appOpen_ads_id);
        editor.commit();
    }

    public void setBannerAdsGoogleAdmobPrf(String banner_ads_id) {
        editor.putString(BannerAdsGoogleAdmob, banner_ads_id);
        editor.commit();
    }

    public String getNativeAdvancedAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(NativeAdvancedAdsGoogleAdmob, null) == null) {
            return context.getString(R.string.str_native_admob_google);
        }
        else return sharedPreferences.getString(NativeAdvancedAdsGoogleAdmob, null);
    }

    public String getInterstitialAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(InterstitialAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_interstitial_admob_google);
        else return sharedPreferences.getString(InterstitialAdsGoogleAdmob, null);
    }

    public String getAppOpenAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(AppOpenAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_app_open_admob_google);
        else return sharedPreferences.getString(AppOpenAdsGoogleAdmob, null);
    }

    public String getBannerAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(BannerAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_banner_admob_google);
        else return sharedPreferences.getString(BannerAdsGoogleAdmob, null);
    }


    //Facebook's method
    public void setNativeAdvancedAdsFaceBookPrf(String banner_ads_id) {
        editor.putString(NativeAdvancedAdsFaceBook, banner_ads_id);
        editor.commit();
    }

    public void setInterstitialAdsFaceBookPrf(String interstitial_ads_id) {
        editor.putString(InterstitialAdsFaceBook, interstitial_ads_id);
        editor.commit();

    }

    public void setBannerAdsFaceBookPrf(String banner_ads_id) {
        editor.putString(BannerAdsFaceBook, banner_ads_id);
        editor.commit();
    }

    public String getNativeAdvancedAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(NativeAdvancedAdsFaceBook, null) == null)
            return context.getString(R.string.str_fb_native_facebook);
        else return sharedPreferences.getString(NativeAdvancedAdsFaceBook, null);
    }

    public String getInterstitialAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(InterstitialAdsFaceBook, null) == null)
            return context.getString(R.string.str_interstitial_facebook);
        else return sharedPreferences.getString(InterstitialAdsFaceBook, null);
    }

    public String getBannerAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.APP_IS_LIVE_MODE)) || sharedPreferences.getString(BannerAdsFaceBook, null) == null)
            return context.getString(R.string.str_banner_90_facebook);
        else return sharedPreferences.getString(BannerAdsFaceBook, null);
    }



//    Other Common Methods
    public boolean getBackPressInterShow() {
        return sharedPreferences.getBoolean(FirebaseConfigConst.IS_BACK_PRESS_INTER_SHOW, false);
    }

    public void setBackPressInterShow(boolean is_boolean_value) {
        editor.putBoolean(FirebaseConfigConst.IS_BACK_PRESS_INTER_SHOW, is_boolean_value);
        editor.commit();
    }

    public void setSkipAdsActivityArray(String skipArrayValue) {
        editor.putString(FirebaseConfigConst.SKIP_ADS_ACTIVITY_ARRAY, skipArrayValue);
        editor.commit();
    }

    public String getSkipAdsActivityArray() {
        return sharedPreferences.getString(FirebaseConfigConst.SKIP_ADS_ACTIVITY_ARRAY, "");
    }


//    Use For Original ShareefPref

    public void setInteger(String integer, int is_boolean_value) {
        editor.putInt(integer, is_boolean_value);
        editor.commit();
    }

    public int getInt(String IsBooleanKey) {
        return sharedPreferences.getInt(IsBooleanKey, 1);
    }


    public void setString(String sharedKey, String strValue) {
        editor.putString(sharedKey, strValue);
        editor.commit();
    }

    public String getString(String sharedKey) {
        return sharedPreferences.getString(sharedKey, "");
    }

    public void setBooleanValue(String IsBooleanKey, boolean is_boolean_value) {
        editor.putBoolean(IsBooleanKey, is_boolean_value);
        editor.commit();
    }

    public boolean getABoolean(String IsBooleanKey) {
        return sharedPreferences.getBoolean(IsBooleanKey, false);
    }
}
