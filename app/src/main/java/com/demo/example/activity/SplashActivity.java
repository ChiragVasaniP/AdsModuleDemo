package com.demo.example.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.aPackage.gopo.AppOpenAdManager;
import com.demo.fullsetup.aPackage.uis.actvity.IntroActivity;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;
import com.demo.fullsetup.aPackage.utils.InternetChecker;
import com.demo.example.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressText;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private final ActivityResultLauncher<Intent> secondActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    navigateToNextScreen();
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0) // Adjust this as needed
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        if (InternetChecker.getInstance().isConnectingToInternet(SplashActivity.this)) {
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            fetchConfigData(mFirebaseRemoteConfig);
                            Log.d("TAG_", "Config params updated: Success");

                        } else {
                            handleFetchFailure();
                            Log.d("TAG_", "Config params updated: Failed");
                        }
                    });
        } else {
            handleFetchFailure();
        }

    }

    private void fetchConfigData(FirebaseRemoteConfig mFirebaseRemoteConfig) {

        //if flag true then avs show if not then avd not showing if any test or live
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT));
        //if app is live mode then app showing live id's and if not then showing test avd
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.APP_IS_LIVE_MODE, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.APP_IS_LIVE_MODE));

        //Used for googi avd id's
        AdsSharedPref.getInstance(SplashActivity.this).setNativeAdvancedAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setInterstitialAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_INTERSTITIAL));
        AdsSharedPref.getInstance(SplashActivity.this).setAppOpenAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_APP_OPEN));
        AdsSharedPref.getInstance(SplashActivity.this).setBannerAdsGoogleAdmobPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.GOOGLE_ADMOB_BANNER));

        //Used for Faci Avd id's
        AdsSharedPref.getInstance(SplashActivity.this).setNativeAdvancedAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setInterstitialAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_INTERSTITIAL));
        AdsSharedPref.getInstance(SplashActivity.this).setBannerAdsFaceBookPrf(mFirebaseRemoteConfig.getString(FirebaseConfigConst.FACEBOOK_BANNER));

        //Back press Click Time Ads Showing or not
        AdsSharedPref.getInstance(SplashActivity.this).setBackPressInterShow(mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_BACK_PRESS_INTER_SHOW));
        //Skip Ads For Activity
        AdsSharedPref.getInstance(SplashActivity.this).setSkipAdsActivityArray(mFirebaseRemoteConfig.getString(FirebaseConfigConst.SKIP_ADS_ACTIVITY_ARRAY));
        //Check Wiche Ads Show Click Time
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.CLICK_TIME_ADS_TYPE_NAME, mFirebaseRemoteConfig.getString(FirebaseConfigConst.CLICK_TIME_ADS_TYPE_NAME));
        //Used for ads Sequance Here
        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.ADS_SEQUENCE, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.ADS_SEQUENCE));

        //Click Counter
        AdsSharedPref.getInstance(SplashActivity.this).setInteger(FirebaseConfigConst.ADS_CLICK_COUNTER, (int) mFirebaseRemoteConfig.getLong(FirebaseConfigConst.ADS_CLICK_COUNTER));

        //turn on or off all inters avd facebook,google all of them if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.INTERSTITIAL_ALL_ADS_ON_OFF));

        //turn on or off all native avd facebook,google all of them if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.NATIVE_ALL_ADS_ON_OFF));

        //turn on or off all banner facebook,google all of them if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.BANNER_ALL_ADS_ON_OFF, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.BANNER_ALL_ADS_ON_OFF));


        //turn on or off all appopen google if true then show avd if false then not show
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_SHOWING_APP_OPEN, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_SHOWING_APP_OPEN));


        //New Added Config for vpn and other functionally
        AdsSharedPref.getInstance(SplashActivity.this).setString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS,  mFirebaseRemoteConfig.getString(FirebaseConfigConst.AD_INTERVAL_MINUTE_FOR_INTERSTITIALS));

        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_BANNER_CHANGE_NATIVE, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_BANNER_CHANGE_NATIVE));
        AdsSharedPref.getInstance(SplashActivity.this).setBooleanValue(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER, mFirebaseRemoteConfig.getBoolean(FirebaseConfigConst.IS_NATIVE_CHANGE_BANNER));

        showAppOpenAds();

    }

    private void showAppOpenAds() {
        AppOpenAdManager appOpenAdManager = new AppOpenAdManager(SplashActivity.this);
        appOpenAdManager.loadAdWithListener(SplashActivity.this, () -> appOpenAdManager.showAdIfAvailable(SplashActivity.this, new AppOpenAdManager.OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                navigateToNextScreen();
            }
        }));
    }

    // Method to navigate to the next screen on successful fetch
    private void navigateToNextScreen() {
        if (!AdsSharedPref.getInstance(SplashActivity.this).getABoolean(FirebaseConfigConst.IS_SHOW_INTRO_SCREEN)){
            Intent intent=new Intent(SplashActivity.this, IntroActivity.class);
            secondActivityLauncher.launch(intent);
        }else {
            Intent intent=new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Replace this with your navigation logic or start the next activity
    }

    // Method to handle fetch failure
    private void handleFetchFailure() {
        // Handle fetch failure if needed, e.g., show an error message to the user
        navigateToNextScreen();
    }
}



