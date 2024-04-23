package com.qa.adsshared.adsPackage.googi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.ConsentSDK;
import com.qa.adsshared.adsPackage.appLo.NativeAppLovin;
import com.qa.adsshared.adsPackage.faci.NativeFacebook;
import com.qa.adsshared.adsPackage.qureks.NativeQureka;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;

import java.util.ArrayList;
import java.util.Locale;

public class NativeAdmobGoogle {


    public static ArrayList<NativeAd> nativeAdsArray;
    public static AdLoader adLoader;
    private static int MAX_AD_COUNT=0;

    public static void loadNativeAds(Context context, FrameLayout frameLayout, boolean isShowBig) {
        AdsSharedPref sharedPref = AdsSharedPref.getInstance(context);
        if (sharedPref.getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (nativeAdsArray != null) {
                if (nativeAdsArray.size() > 0) {
                    Log.e("TAG_PreLoadNative", "Native ads from array " + nativeAdsArray.size());
                    loadNativeAdsFromList(context, frameLayout, isShowBig);
                } else {

                    loadNativeAdsPreload(context, frameLayout, isShowBig);
                }
            } else {
                nativeAdsArray = new ArrayList<>();
                Log.e("TAG_PreLoadNative", "Native ads first time ");
                loadNativeAdsPreload(context, frameLayout, isShowBig);
            }

        }
    }

    public static void loadNativeAdsWithoutPreload(Context context, FrameLayout frameLayout, boolean isShowBig) {
        AdsSharedPref sharedPref = AdsSharedPref.getInstance(context);
        if (sharedPref.getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {
            if (nativeAdsArray != null) {
                if (nativeAdsArray.size() > 0) {
                    Log.e("TAG_PreLoadNative", "Native ads from array " + nativeAdsArray.size());
                    loadNativeAdsFromList(context, frameLayout, isShowBig);
                } else {

                    loadNativeAdsNoPreload(context, frameLayout, isShowBig);
                }
            } else {
                nativeAdsArray = new ArrayList<>();
                Log.e("TAG_PreLoadNative", "Native ads first time ");
                loadNativeAdsNoPreload(context, frameLayout, isShowBig);
            }

        }
    }

    public static void loadNativeAdsFromList(Context context, FrameLayout frameLayout, boolean isShowBig) {
        NativeAd nativeAd = nativeAdsArray.get(nativeAdsArray.size() - 1);
        nativeAdsArray.remove(nativeAdsArray.size() - 1);
        if (isShowBig) {
            NativeAdView adView =
                    (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.ad_unified, frameLayout, false);
            assert nativeAd != null;
            populateNativeAdViewBig(nativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } else {
            NativeAdView nativeAdView =
                    (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.small_native_layouts, frameLayout, false);
            assert nativeAd != null;
            populateNativeAdViewSmall(nativeAd, nativeAdView);
            frameLayout.removeAllViews();
            frameLayout.addView(nativeAdView);
        }
    }

    private static void loadNativeAdsPreload(Context context, FrameLayout frameLayout, boolean isShowBig) {

        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        if ( AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.NATIVE_PRELOAD_COUNT)>1){
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }


        AdLoader.Builder builder = new AdLoader.Builder(context, AdsSharedPref.getInstance(context).getNativeAdvancedAdsGoogleAdmob());
        adLoader = builder
                .forNativeAd(nativeAd -> {
                    nativeAdsArray.add(nativeAd);
                    if (!adLoader.isLoading()) {
                        loadNativeAdsFromList(context, frameLayout, isShowBig);
                    }
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB) {
                            NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                        } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA) {
                            NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                        } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                            NativeQureka.loadNativeQureka(context, frameLayout, false);
                        } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN) {
                            NativeAppLovin.loadAppLovinNativeBannerAd(context, frameLayout);
                        }
                        String error = String.format(Locale.getDefault(), "domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.e("TAG_", "onAdFailedToLoad: " + error);
//                                        Toast.makeText(context, "Failed to load native ad with error " + error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                }).withNativeAdOptions(
                        new NativeAdOptions.Builder().setRequestCustomMuteThisAd(true)
                                .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build()
                )
                .build();

        adLoader.loadAds(ConsentSDK.getAdRequest(context), AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.NATIVE_PRELOAD_COUNT));

    }

    private static void loadNativeAdsNoPreload(Context context, FrameLayout frameLayout, boolean isShowBig) {

        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        AdLoader.Builder builder = new AdLoader.Builder(context, AdsSharedPref.getInstance(context).getNativeAdvancedAdsGoogleAdmob());
        adLoader = builder
                .forNativeAd(nativeAd -> {
                    nativeAdsArray.add(nativeAd);
                    if (!adLoader.isLoading()) {
                        loadNativeAdsFromList(context, frameLayout, isShowBig);
                    }
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB) {
                            NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                        } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA) {
                            NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                        } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                            NativeQureka.loadNativeQureka(context, frameLayout, false);
                        } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN) {
                            NativeAppLovin.loadAppLovinNativeBannerAd(context, frameLayout);
                        }
                        String error = String.format(Locale.getDefault(), "domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.e("TAG_", "onAdFailedToLoad: " + error);
//                                        Toast.makeText(context, "Failed to load native ad with error " + error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                }).withNativeAdOptions(
                        new NativeAdOptions.Builder().setRequestCustomMuteThisAd(true)
                                .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT).build()
                )
                .build();

            adLoader.loadAds(ConsentSDK.getAdRequest(context),1);

    }


/*    public static void loadNativeAds(Context context, FrameLayout frameLayout, boolean isShowBig) {
        AdsSharedPref sharedPref = AdsSharedPref.getInstance(context);
        if (sharedPref.getABoolean(FirebaseConfigConst.IS_ADD_SHOW)) {


            Dialog progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.dialog_progress);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            AdLoader.Builder builder = new AdLoader.Builder(context, sharedPref.getNativeAdvancedAdsGoogleAdmob());

                builder.forNativeAd(nativeAd -> {
                    if (isShowBig) {
                        NativeAdView adView =
                                (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.ad_unified, frameLayout, false);
                        assert nativeAd != null;
                        populateNativeAdViewBig(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    } else {
                        NativeAdView nativeAdView =
                                (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.small_native_layouts, frameLayout, false);
                        assert nativeAd != null;
                        populateNativeAdViewSmall(nativeAd, nativeAdView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(nativeAdView);
                    }

                });


            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB) {
                        NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                    } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_FB_FAIL_QUEREKA) {
                        NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
                    } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.FB_FAIL_SHOW_ADMOB_FAIL_QUREKA) {
                        NativeQureka.loadNativeQureka(context, frameLayout, false);
                    } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.ADMOB_FAIL_SHOW_APPLOVIN) {
                        NativeAppLovin.loadAppLovinNativeBannerAd(context, frameLayout);
                    }
                    String error = String.format(Locale.getDefault(), "domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                    Log.e("TAG_", "onAdFailedToLoad: " + error);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }).build();

            adLoader.loadAd(ConsentSDK.getAdRequest(context));
        }

    }*/


    private static void populateNativeAdViewBig(NativeAd nativeAd, NativeAdView adView) {

        adView.setMediaView(adView.findViewById(R.id.ad_media));


        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());


        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            if (adView.getCallToActionView() instanceof Button) {
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            } else {
                ((AppCompatTextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }


        adView.setNativeAd(nativeAd);


        VideoController vc = nativeAd.getMediaContent().getVideoController();


        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {

            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {

                    super.onVideoEnd();
                }
            });
        }
    }


    private static void populateNativeAdViewSmall(NativeAd nativeAd, NativeAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        VideoController vc = nativeAd.getMediaContent().getVideoController();
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }


        adView.setNativeAd(nativeAd);
    }


    public static String getActivityName(Context context) {

        Activity activity = (Activity) context;
        activity.getClass().getName();
        Log.i("CURRENT_Activity ", activity.getClass().getSimpleName());
        ;
        return activity.getClass().getSimpleName();
    }

}
