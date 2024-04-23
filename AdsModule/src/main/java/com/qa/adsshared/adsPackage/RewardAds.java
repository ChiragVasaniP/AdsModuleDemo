package com.qa.adsshared.adsPackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.qa.adsshared.R;
import com.qa.adsshared.ShowRewardAds;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

public class RewardAds {
    private static RewardedAd rewardedAd;

    public static void loadReward(final Context context, ShowRewardAds interfaceAds) {
        AdsSharedPref sharedPref = AdsSharedPref.getInstance(context);
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        if (rewardedAd == null) {
            RewardedAd.load(context, sharedPref.getRewardAdsGoogleAdmob(),
                    ConsentSDK.getAdRequest(context), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            
                            Log.d("TAG_reward", loadAdError.toString());
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            interfaceAds.showRewardAdsValue(false);
                            rewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            rewardedAd = ad;
                            ShowRewardAds(context, interfaceAds, progressDialog);
                            Log.d("TAG", "Ad was loaded.");
                        }
                    });
        } else {
            ShowRewardAds(context, interfaceAds, progressDialog);
        }

    }

    private static void ShowRewardAds(Context context, ShowRewardAds interfaceAds, Dialog progressDialog) {

        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                
                Log.d("TAG_reward", "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Log.d("TAG_reward", "Ad dismissed fullscreen content.");
                rewardedAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                
                interfaceAds.showRewardAdsValue(false);
                Log.e("TAG_reward", "Ad failed to show fullscreen content.");
                rewardedAd = null;
            }

            @Override
            public void onAdImpression() {
                Log.d("TAG_reward", "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("TAG_reward", "Ad showed fullscreen content.");
            }
        });

        rewardedAd.show((Activity) context, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Log.d("TAG_reward", "Ad recorded an Reward Item.");
               interfaceAds.showRewardAdsValue(true);
            }
        });
    }
}
