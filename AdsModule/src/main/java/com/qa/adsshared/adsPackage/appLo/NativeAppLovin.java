package com.qa.adsshared.adsPackage.appLo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

public class NativeAppLovin {
    public static void  loadAppLovinNativeBannerAd(Context context,FrameLayout layout) {
            MaxNativeAdLoader maxNativeAdLoader = new MaxNativeAdLoader(AdsSharedPref.getInstance(context).getNativeAdvancedAdsApplovin(), context);
            maxNativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                    super.onNativeAdLoaded(maxNativeAdView, maxAd);
                    layout.removeAllViews();
                    inflate_NB_APPLOVIN(maxNativeAdView, layout);
                }

                @Override
                public void onNativeAdLoadFailed(String s, MaxError maxError) {
                    super.onNativeAdLoadFailed(s, maxError);
                    Log.e("TAG_applovin", "preloadAppLovinNativeAd onError: " + maxError.getMessage());
                    layout.removeAllViews();
                }
            });
            maxNativeAdLoader.loadAd(createNativeBannerAdView(context));


    }


    private static MaxNativeAdView createNativeBannerAdView(Context context) {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.ads_nb_app_lovin)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();

        return new MaxNativeAdView(binder, context);
    }


    public static void inflate_NB_APPLOVIN(MaxNativeAdView nativeAd, ViewGroup cardView) {
        cardView.setVisibility(View.VISIBLE);
        cardView.addView(nativeAd);
    }
}
