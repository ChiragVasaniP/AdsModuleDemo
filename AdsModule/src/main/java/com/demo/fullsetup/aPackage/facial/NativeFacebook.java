package com.demo.fullsetup.aPackage.facial;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.demo.fullsetup.R;

import java.util.ArrayList;
import java.util.List;

import com.demo.fullsetup.FirebaseConfigConst;
import com.demo.fullsetup.aPackage.gopo.NativeAdmobGoogle;
import com.demo.fullsetup.aPackage.utils.AdsSharedPref;

public class NativeFacebook {
//    public static String FB_NATIVE = "YOUR_PLACEMENT_ID";

    public static void loadNativeAds(Context context, FrameLayout frameLayout, boolean isShowBig) {
        if (isShowBig) {
            NativeAdLayout adView =
                    (NativeAdLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.ad_facebook_native, frameLayout, false);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
            loadNativeAdBig(context, adView,frameLayout);
        } else {
            loadSmallNative(context, frameLayout);
        }

    }

    private static void loadSmallNative(Context context, FrameLayout frameLayout) {
        // Instantiate an NativeBannerAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release

        NativeBannerAd mNativeBannerAd = new NativeBannerAd(context, AdsSharedPref.getInstance(context).getNativeAdvancedAdsFaceBook());

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)==FirebaseConfigConst.FB_FAIL_SHOW_ADMOB){
                    NativeAdmobGoogle.loadNativeAds(context,frameLayout,false);
                }
                Log.e("TAG_facebookerrror", "onError: "+adError.getErrorMessage().toString() );
            }

            @Override
            public void onAdLoaded(Ad ad) {
                View adView = NativeBannerAdView.render(context, mNativeBannerAd, NativeBannerAdView.Type.HEIGHT_100);
//                LinearLayout nativeBannerAdContainer = (LinearLayout) findViewById(R.id.native_banner_ad_container);
//                 Add the Native Banner Ad View to your ad container
                frameLayout.addView(adView);

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onMediaDownloaded(Ad ad) {

            }
        };

        // Initiate a request to load an ad.
        mNativeBannerAd.loadAd(
                mNativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    public static void loadNativeAdBig(Context context, NativeAdLayout nativeAdLayout, FrameLayout frameLayout) {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        NativeAd nativeAd = new NativeAd(context,  AdsSharedPref.getInstance(context).getNativeAdvancedAdsFaceBook());

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e("TAG_Facebook_native", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)==FirebaseConfigConst.FB_FAIL_SHOW_ADMOB){
                    NativeAdmobGoogle.loadNativeAds(context,frameLayout,true);
                }
                // Native ad failed to load
                Log.e("TAG_Facebook_native", "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d("TAG_Facebook_native", "Native ad is loaded and ready to be displayed!");
                if (nativeAd != ad) {
                    return;
                }
                inflateAdBig(context, nativeAd, nativeAdLayout);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d("TAG_Facebook_native", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d("TAG_Facebook_native", "Native ad impression logged!");
            }
        };

        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    public static void inflateAdBig(Context context, NativeAd nativeAd, NativeAdLayout nativeAdLayout) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
//        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.fan_native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer =adView. findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }
}
