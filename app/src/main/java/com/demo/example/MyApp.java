package com.demo.example;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.aPackage.gopo.AppOpenAdManager;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;
import com.demo.example.activity.SplashActivity;


public class MyApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String TAG = "MyApplication";
    private static MyApp mInstance;
    private static MyApp myApplication;

    public Context sContext = null;
    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;

    public static MyApp getApplication() {
        return myApplication;
    }

    public static void setApplication(MyApp application) {
        myApplication = application;
    }

    public static synchronized MyApp getInstance() {
        MyApp appEditor;
        synchronized (MyApp.class) {
            synchronized (MyApp.class) {
                synchronized (MyApp.class) {
                    appEditor = mInstance;
                    myApplication = appEditor;
                }
                return appEditor;
            }
        }
    }

    public static Context getContext() {
        return getContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        new AdsSharedPref(this);
        registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        mInstance = this;
        this.sContext = getApplicationContext();
        setApplication(this);
        mInstance = this;
        FirebaseApp.initializeApp(MyApp.this);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure").invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mInstance = this;
        FirebaseApp.initializeApp(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        checkAppReplacingState();


        appOpenAdManager = new AppOpenAdManager(this);
    }

    private void checkAppReplacingState() {
        Log.d(TAG, "app start...");
        if (getResources() == null) {
            Log.d(TAG, "app is replacing...kill");
            Process.killProcess(Process.myPid());
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        Log.e(TAG, "onMoveToForeground: " + currentActivity.getLocalClassName());
        if (AdsSharedPref.getInstance(currentActivity).getABoolean(FirebaseConfigConst.IS_ADS_SHOWING_OR_NOT)
                && AdsSharedPref.getInstance(currentActivity).getABoolean(FirebaseConfigConst.IS_SHOWING_APP_OPEN)
                && !(currentActivity instanceof SplashActivity)

        )
        {
            appOpenAdManager.showAdIfAvailable(currentActivity);
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDistroy() {
        // Show the ad (if available) when the app moves to foreground.

    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }


    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull AppOpenAdManager.OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    public void showAdIfAvailableForSplash(
            @NonNull Activity activity,
            @NonNull AppOpenAdManager.OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailableForSplashScreen(activity, onShowAdCompleteListener);
    }

    public void loadOpenAds(@NonNull Activity activity){
        appOpenAdManager.loadAd(activity);
    }


}
