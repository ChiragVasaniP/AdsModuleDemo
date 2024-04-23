package com.qa.adsshared.adsPackage.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;

import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.ShowAds;
import com.qa.adsshared.adsPackage.AdsShowingClass;
import com.qa.adsshared.adsPackage.googi.AppOpenAdManager;




public abstract class OnOneOffClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 1000;

    
    public static boolean isViewClicked = false;
    private long mLastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;

        mLastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return;
        }
        if (!isViewClicked) {
            isViewClicked = true;
            startTimer();
        } else {
            return;
        }
        if (AdsSharedPref.getInstance(view.getContext()).getInt(FirebaseConfigConst.AD_CLICK_AND_INTERVAL_COUNTER)==AdsShowingClass.InterStillCounter){
            AdsShowingClass.InterStillCounter=1;
            if (AdsSharedPref.getInstance(view.getContext()).getString(FirebaseConfigConst.CLICK_TIME_ADS_TYPE_NAME).equalsIgnoreCase("Appopen")) {
                Dialog progressDialog = new Dialog(view.getContext());
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.dialog_progress);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                AppOpenAdManager appOpenAdManager = new AppOpenAdManager(view.getContext());
                appOpenAdManager.loadAdWithListener(view.getContext(), () -> appOpenAdManager.showAdIfAvailable((Activity) view.getContext(), new AppOpenAdManager.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        onSingleClick(view);
                    }
                }));
            } else if (AdsSharedPref.getInstance(view.getContext()).getString(FirebaseConfigConst.CLICK_TIME_ADS_TYPE_NAME).equalsIgnoreCase("Interstitial")) {
                AdsShowingClass.showInterStillAdsForClick(view.getContext(), new ShowAds() {
                    @Override
                    public void onAdsFinish() {
                        onSingleClick(view);
                    }
                });
            } else {
                onSingleClick(view);
            }
        }else{
            AdsShowingClass.InterStillCounter++;
            onSingleClick(view);
        }





    }

    
    private void startTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isViewClicked = false;
            }
        }, 600);
    }
}