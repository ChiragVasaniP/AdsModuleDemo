package com.qa.adsshared.adsPackage.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.AdsShowingClass;

import java.util.Objects;

public class AdsSharedPref {

    public static String SHARED_PREF_ADS = "LOAN_GUIDE_03_06";

    
    private static final String NativeAdvancedAdsGoogleAdmob = "NativeAdvancedAdsPrefGoogleAdmob";
    private static final String InterstitialAdsGoogleAdmob = "InterstitialAdsPreGoogleAdmob";
    private static final String AppOpenAdsGoogleAdmob = "AppOpenAdsPrefBackPro";
    private static final String BannerAdsGoogleAdmob = "BannerAdsPrefGoogleAdmob";
    private static final String RewardAdsGoogleAdmob = "RewardAdsPrefGoogleAdmob";

    
    private static final String NativeAdvancedAdsFaceBook = "NativeAdvancedAdsPrefFaceBook";
    private static final String InterstitialAdsFaceBook = "InterstitialAdsPreFaceBook";
    private static final String BannerAdsFaceBook = "BannerAdsPrefFaceBook";
    private static final String RewardAdsFaceBook = "RewardAdsPrefFaceBook";

    
    private static final String NativeAdvancedAdsAppLovin = "NativeAdvancedAdsPrefAppLovin";
    private static final String InterstitialAdsAppLovin = "InterstitialAdsPreAppLovin";
    private static final String BannerAdsAppLovin = "BannerAdsPrefAppLovin";
    private static final String RewardAdsAppLovin = "RewardAdsPrefAppLovin";

    
    private static final String NativeAdvancedAdsLastIndex = "NativeAdvancedAdsPrefLastIndex";
    private static final String InterstitialAdsLastIndex = "InterstitialAdsPreLastIndex";
    private static final String AppOpenAdsLastIndex = "AppOpenAdsPrefProLastIndex";
    private static final String BannerAdsLastIndex = "BannerAdsPrefLastIndex";
    private static final String RewardAdsLastIndex = "RewardAdsPrefLastIndex";






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

        if (INSTANCE == null) {

            synchronized (AdsSharedPref.class) {

                if (INSTANCE == null) {
                    INSTANCE = new AdsSharedPref(context);
                }
            }
        }

        return INSTANCE;
    }

    
    

    
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

    public void setRewardAdsGoogleAdmobPrf(String reward_ads_id) {
        editor.putString(RewardAdsGoogleAdmob, reward_ads_id);
        editor.commit();
    }

    public String getNativeAdvancedAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(NativeAdvancedAdsGoogleAdmob, null) == null) {
            return context.getString(R.string.str_native_admob_google);
        }

        else return lastIndexGetWithKey(NativeAdvancedAdsGoogleAdmob, "NativeAds");
    }

    public String getInterstitialAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(InterstitialAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_interstitial_admob_google);

        else return lastIndexGetWithKey(InterstitialAdsGoogleAdmob, "Interstitial");
    }

    public String getAppOpenAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(AppOpenAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_app_open_admob_google);

        else return lastIndexGetWithKey(AppOpenAdsGoogleAdmob, "AppOpen");
    }

    public String getBannerAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(BannerAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_banner_admob_google);

        else return lastIndexGetWithKey(BannerAdsGoogleAdmob, "Banner");
    }

    public String getRewardAdsGoogleAdmob() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(RewardAdsGoogleAdmob, null) == null)
            return context.getString(R.string.str_reward_admob_google);

        else return lastIndexGetWithKey(RewardAdsGoogleAdmob, "Reward");
    }

    

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

    public void setRewardAdsFaceBookPrf(String reward_ads_id) {
        editor.putString(RewardAdsFaceBook, reward_ads_id);
        editor.commit();
    }

    public String getNativeAdvancedAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(NativeAdvancedAdsFaceBook, null) == null)
            return context.getString(R.string.str_fb_native_facebook);

        else return lastIndexGetWithKey(NativeAdvancedAdsFaceBook, "NativeAds");
    }

    public String getInterstitialAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(InterstitialAdsFaceBook, null) == null)
            return context.getString(R.string.str_interstitial_facebook);

        else return lastIndexGetWithKey(InterstitialAdsFaceBook, "Interstitial");
    }

    public String getBannerAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(BannerAdsFaceBook, null) == null)
            return context.getString(R.string.str_banner_90_facebook);

        else return lastIndexGetWithKey(BannerAdsFaceBook, "Banner");
    }

    public String getRewardAdsFaceBook() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(RewardAdsFaceBook, null) == null)
            return context.getString(R.string.str_reward_facebook);

        else return lastIndexGetWithKey(RewardAdsFaceBook, "Reward");
    }

    

    public void setNativeAdvancedAdsApplovinPrf(String banner_ads_id) {
        editor.putString(NativeAdvancedAdsAppLovin, banner_ads_id);
        editor.commit();
    }

    public void setInterstitialAdsApplovinPrf(String interstitial_ads_id) {
        editor.putString(InterstitialAdsAppLovin, interstitial_ads_id);
        editor.commit();

    }

    public void setBannerAdsApplovinPrf(String banner_ads_id) {
        editor.putString(BannerAdsAppLovin, banner_ads_id);
        editor.commit();
    }

    public void setRewardAdsApplovinPrf(String reward_ads_id) {
        editor.putString(RewardAdsAppLovin, reward_ads_id);
        editor.commit();
    }

    public String getNativeAdvancedAdsApplovin() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(NativeAdvancedAdsAppLovin, null) == null)
            return context.getString(R.string.str_native_applovin);

        else return lastIndexGetWithKey(NativeAdvancedAdsAppLovin, "NativeAds");
    }

    public String getInterstitialAdsApplovin() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(InterstitialAdsAppLovin, null) == null)
            return context.getString(R.string.str_interstitial_applovin);

        else return lastIndexGetWithKey(InterstitialAdsAppLovin, "Interstitial");
    }

    public String getBannerAdsApplovin() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(BannerAdsAppLovin, null) == null)
            return context.getString(R.string.str_banner_applovin);

        else return lastIndexGetWithKey(BannerAdsAppLovin, "Banner");
    }

    public String getRewardAdsAppLovin() {
        if ((!getABoolean(FirebaseConfigConst.IS_LIVEMOD)) || sharedPreferences.getString(RewardAdsAppLovin, null) == null)
            return context.getString(R.string.str_reward_facebook);

        else return lastIndexGetWithKey(RewardAdsAppLovin, "Reward");
    }




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


    @SuppressLint("LongLogTag")
    public String lastIndexGetWithKey(String key, String type) {
        String idArray = sharedPreferences.getString(key, "");
        Log.e("idArray_idArray", "idArray All: " + idArray.toString());
        String[] str_arr = idArray.split("&");
        String returnAdsString = "";
        if (str_arr.length > 1) {
            int totalNumberOfSize = str_arr.length;
            if (Objects.equals(type, "NativeAds")) {
                returnAdsString = str_arr[getLastIndex(NativeAdvancedAdsLastIndex, totalNumberOfSize)];
            } else if (Objects.equals(type, "Banner")) {
                returnAdsString = str_arr[getLastIndex(BannerAdsLastIndex, totalNumberOfSize)];
            } else if (Objects.equals(type, "AppOpen")) {
                returnAdsString = str_arr[getLastIndex(AppOpenAdsLastIndex, totalNumberOfSize)];
            } else if (Objects.equals(type, "Reward")) {
                returnAdsString = str_arr[getLastIndex(RewardAdsLastIndex, totalNumberOfSize)];
            } else if (Objects.equals(type, "Interstitial"))  {
                returnAdsString = str_arr[getLastIndex(InterstitialAdsLastIndex, totalNumberOfSize)];
            }
        } else {
            returnAdsString = idArray;
        }
        Log.e("Tag_ads_type_return_ad_id", "Shared Key : " + key + " Ads Type : " + type + " return " + returnAdsString);
        return returnAdsString;
    }

    int getLastIndex(String counterKey, int arraySize) {
        int savedIndex = sharedPreferences.getInt(counterKey, 0); 
        int localVariable = 0;
        if (savedIndex != (arraySize - 1)) {
            localVariable = savedIndex + 1;
        }
        editor.putInt(counterKey, localVariable);
        editor.commit();
        Log.e("TAG_ads_return_index\n", "\nlastSaved Index :_____" + savedIndex + "\nReturn Index Index:_____ " + localVariable + "\ncounter Key: " + counterKey);
        return localVariable;
    }

}
