package com.qa.adsshared.adsPackage.faci;

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
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.googi.NativeAdmobGoogle;
import com.qa.adsshared.adsPackage.qureks.NativeQureka;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

import java.util.ArrayList;
import java.util.List;

public class NativeFacebook {


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
        
        
        
        

        NativeBannerAd mNativeBannerAd = new NativeBannerAd(context, AdsSharedPref.getInstance(context).getNativeAdvancedAdsFaceBook());

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)==FirebaseConfigConst.FB_FAIL_SHOW_ADMOB){
                    NativeAdmobGoogle.loadNativeAds(context,frameLayout,false);
                }else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA){
                    NativeQureka.loadNativeQureka(context,frameLayout,false);
                }else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                    NativeAdmobGoogle.loadNativeAds(context,frameLayout,false);
                }
                Log.e("TAG_facebookerrror", "onError: "+adError.getErrorMessage().toString() );
            }

            @Override
            public void onAdLoaded(Ad ad) {
                View adView = NativeBannerAdView.render(context, mNativeBannerAd, NativeBannerAdView.Type.HEIGHT_100);


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

        
        mNativeBannerAd.loadAd(
                mNativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    public static void loadNativeAdBig(Context context, NativeAdLayout nativeAdLayout, FrameLayout frameLayout) {
        
        
        
        
        
        NativeAd nativeAd = new NativeAd(context,  AdsSharedPref.getInstance(context).getNativeAdvancedAdsFaceBook());

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                
                Log.e("TAG_Facebook_native", "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE)==FirebaseConfigConst.FB_FAIL_SHOW_ADMOB){
                    NativeAdmobGoogle.loadNativeAds(context,frameLayout,true);
                }else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA){
                    NativeQureka.loadNativeQureka(context,frameLayout,true);
                }else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                    NativeAdmobGoogle.loadNativeAds(context,frameLayout,true);
                }
                
                Log.e("TAG_Facebook_native", "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                
                Log.d("TAG_Facebook_native", "Native ad is loaded and ready to be displayed!");
                if (nativeAd != ad) {
                    return;
                }
                inflateAdBig(context, nativeAd, nativeAdLayout);
            }

            @Override
            public void onAdClicked(Ad ad) {
                
                Log.d("TAG_Facebook_native", "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                
                Log.d("TAG_Facebook_native", "Native ad impression logged!");
            }
        };

        
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    public static void inflateAdBig(Context context, NativeAd nativeAd, NativeAdLayout nativeAdLayout) {

        nativeAd.unregisterView();

        

        LayoutInflater inflater = LayoutInflater.from(context);
        
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.fan_native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        
        LinearLayout adChoicesContainer =adView. findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }
}
