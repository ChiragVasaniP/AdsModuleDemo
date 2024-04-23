package com.qa.adsshared.adsPackage.appLo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

public class BannerAdsAppLovin {


    public static void  callBannerAd(Context context, FrameLayout banner_container) {

            MaxAdView appLovinBannerAdd = new MaxAdView(AdsSharedPref.getInstance(context).getBannerAdsApplovin() , context);
            appLovinBannerAdd.setId(ViewCompat.generateViewId());
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int heightDp = MaxAdFormat.BANNER.getAdaptiveSize((Activity) context).getHeight();
            int heightPx = AppLovinSdkUtils.dpToPx(context, heightDp);
            appLovinBannerAdd.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
            appLovinBannerAdd.setExtraParameter("adaptive_banner", "true");
            appLovinBannerAdd.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {

                }

                @Override
                public void onAdLoaded(MaxAd ad) {
                    banner_container.removeAllViews();
                    banner_container.addView(appLovinBannerAdd);
                    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
                    banner_container.setPadding(0, margin, 0, 0);
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {

                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    Log.e("TAG_applovin_fail", "preloadAppLovinBannerAd onError: " + error.toString());
                    banner_container.removeAllViews();
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    banner_container.removeAllViews();
                }
            });
            appLovinBannerAdd.loadAd();

    }
}
