package com.qa.adsshared.adsPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.net.MalformedURLException;
import java.net.URL;

public class ConsentSDK {

    private static final String ads_preference = "ads_preference";
    private static final String user_status = "user_status";
    private static final boolean PERSONALIZED = true;
    private static final boolean NON_PERSONALIZED = false;
    private static final String preferences_name = "com.ayoubfletcher.consentsdk";
    
    private static final String DUMMY_BANNER = "ca-app-pub-3940256099942544/6300978111";
    public static ConsentSDK consentSDK;
    private final Context context;
    private final String privacyURL;
    private final String publisherId;
    private final SharedPreferences settings;
    private ConsentForm form;
    private String LOG_TAG = "ID_LOG";
    private String DEVICE_ID = "";
    private boolean DEBUG = false;

    
    public ConsentSDK(Context context, String publisherId, String privacyURL, boolean DEBUG) {
        this.context = context;
        this.settings = initPreferences(context);
        this.publisherId = publisherId;
        this.privacyURL = privacyURL;
        this.DEBUG = DEBUG;
        consentSDK = this;
    }

    
    public ConsentSDK(Context context, String publisherId, String privacyURL) {
        this.context = context;
        this.settings = context.getSharedPreferences(preferences_name, Context.MODE_PRIVATE);
        this.publisherId = publisherId;
        this.privacyURL = privacyURL;
        consentSDK = this;
    }

    
    public static void initDummyBanner(Context context) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(DUMMY_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
    }

    
    private static SharedPreferences initPreferences(Context context) {
        return context.getSharedPreferences(preferences_name, Context.MODE_PRIVATE);
    }

    
    public static boolean isConsentPersonalized(Context context) {
        SharedPreferences settings = initPreferences(context);
        return settings.getBoolean(ads_preference, PERSONALIZED);
    }

    
    public static AdRequest getAdRequest(Context context) {
        if (isConsentPersonalized(context)) {
            return new AdRequest.Builder().build();
        } else {
            return new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle())
                    .build();
        }
    }

    
    private static Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        return extras;
    }

    
    public static boolean isUserLocationWithinEea(Context context) {
        return initPreferences(context).getBoolean(user_status, false);
    }

    
    private void consentIsPersonalized() {
        settings.edit().putBoolean(ads_preference, PERSONALIZED).apply();
    }

    
    private void consentIsNonPersonalized() {
        settings.edit().putBoolean(ads_preference, NON_PERSONALIZED).apply();
    }

    
    private void updateUserStatus(boolean status) {
        settings.edit().putBoolean(user_status, status).apply();
    }

    
    private void initConsentInformation(final ConsentInformationCallback callback) {
        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        if (DEBUG) {
            if (!DEVICE_ID.isEmpty()) {
                consentInformation.addTestDevice(DEVICE_ID);
            }
            consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        }
        String[] publisherIds = {publisherId};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                if (callback != null) {
                    callback.onResult(consentInformation, consentStatus);
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String reason) {
                callback.onFailed(consentInformation, reason);
            }
        });
    }

    
    public void isRequestLocationIsEeaOrUnknown(final LocationIsEeaOrUnknownCallback callback) {
        
        initConsentInformation(new ConsentInformationCallback() {
            @Override
            public void onResult(ConsentInformation consentInformation, ConsentStatus consentStatus) {
                callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
            }

            @Override
            public void onFailed(ConsentInformation consentInformation, String reason) {
                callback.onResult(false);
            }
        });
    }

    
    public void checkConsent(final ConsentCallback callback) {
        
        Builder.dialog.show();
        initConsentInformation(new ConsentInformationCallback() {

            @Override
            public void onResult(ConsentInformation consentInformation, ConsentStatus consentStatus) {
                
                switch (consentStatus) {
                    case UNKNOWN:
                        
                        if (DEBUG) {
                            Log.d(LOG_TAG, "Unknown Consent");
                            Log.d(LOG_TAG, "User location within EEA: " + consentInformation.isRequestLocationInEeaOrUnknown());
                        }
                        
                        if (consentInformation.isRequestLocationInEeaOrUnknown()) {

                            requestConsent(new ConsentStatusCallback() {
                                @Override
                                public void onResult(boolean isRequestLocationInEeaOrUnknown, int isConsentPersonalized) {
                                    callback.onResult(isRequestLocationInEeaOrUnknown);
                                }
                            });
                        } else {
                            Builder.dialog.dismiss();
                            consentIsPersonalized();
                            
                            callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
                        }
                        break;
                    case NON_PERSONALIZED:
                        Builder.dialog.dismiss();
                        consentIsNonPersonalized();
                        
                        callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
                        break;
                    default:
                        Builder.dialog.dismiss();
                        consentIsPersonalized();
                        
                        callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
                        break;
                }
                
                updateUserStatus(consentInformation.isRequestLocationInEeaOrUnknown());
            }


            @Override
            public void onFailed(ConsentInformation consentInformation, String reason) {
                Builder.dialog.dismiss();
                if (DEBUG) {
                    Log.d(LOG_TAG, "Failed to update: $reason");
                }
                
                updateUserStatus(consentInformation.isRequestLocationInEeaOrUnknown());
                
                callback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
            }
        });
    }

    
    public void requestConsent(final ConsentStatusCallback callback) {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(privacyURL);
        } catch (MalformedURLException e) {

        }
        form = new ConsentForm.Builder(context, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        if (DEBUG) {
                            Log.d(LOG_TAG, "Consent Form is loaded!");
                        }
                        form.show();
                    }

                    @Override
                    public void onConsentFormError(String reason) {
                        if (DEBUG) {
                            Log.d(LOG_TAG, "Consent Form ERROR: $reason");
                        }
                        
                        if (callback != null) {
                            consentSDK.isRequestLocationIsEeaOrUnknown(new LocationIsEeaOrUnknownCallback() {
                                @Override
                                public void onResult(boolean isRequestLocationInEeaOrUnknown) {
                                    callback.onResult(isRequestLocationInEeaOrUnknown, -1);
                                }
                            });
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        Builder.dialog.dismiss();
                        if (DEBUG) {
                            Log.d(LOG_TAG, "Consent Form is opened!");
                        }
                    }

                    @Override
                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        Builder.dialog.show();
                        if (DEBUG) {
                            Log.d(LOG_TAG, "Consent Form Closed!");
                        }
                        final int isConsentPersonalized;
                        
                        switch (consentStatus) {
                            case NON_PERSONALIZED:
                                consentIsNonPersonalized();
                                isConsentPersonalized = 0;
                                break;
                            default:
                                consentIsPersonalized();
                                isConsentPersonalized = 1;
                                break;
                        }
                        
                        if (callback != null) {
                            consentSDK.isRequestLocationIsEeaOrUnknown(new LocationIsEeaOrUnknownCallback() {
                                @Override
                                public void onResult(boolean isRequestLocationInEeaOrUnknown) {
                                    callback.onResult(isRequestLocationInEeaOrUnknown, isConsentPersonalized);
                                }
                            });
                        }
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();
        form.load();
    }

    public abstract static class ConsentCallback {
        abstract public void onResult(boolean isRequestLocationInEeaOrUnknown);
    }

    public abstract static class ConsentStatusCallback {
        abstract public void onResult(boolean isRequestLocationInEeaOrUnknown, int isConsentPersonalized);
    }

    public abstract static class ConsentInformationCallback {
        abstract public void onResult(ConsentInformation consentInformation, ConsentStatus consentStatus);

        abstract public void onFailed(ConsentInformation consentInformation, String reason);
    }

    public abstract static class LocationIsEeaOrUnknownCallback {
        abstract public void onResult(boolean isRequestLocationInEeaOrUnknown);
    }

    
    public static class Builder {

        public static ProgressDialog dialog;
        private final Context context;
        private String LOG_TAG = "ID_LOG";
        private String DEVICE_ID = "";
        private boolean DEBUG = false;
        private String privacyURL;
        private String publisherId;

        
        public Builder(Context context) {
            this.context = context;
        }

        
        public Builder addTestDeviceId(String device_id) {
            this.DEVICE_ID = device_id;
            this.DEBUG = true;
            return this;
        }

        
        public Builder addPrivacyPolicy(String privacyURL) {
            this.privacyURL = privacyURL;
            return this;
        }

        
        public Builder addPublisherId(String publisherId) {
            this.publisherId = publisherId;
            return this;
        }

        
        public Builder addCustomLogTag(String LOG_TAG) {
            this.LOG_TAG = LOG_TAG;
            return this;
        }

        
        public ConsentSDK build() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);

            if (this.DEBUG) {
                ConsentSDK consentSDK = new ConsentSDK(context, publisherId, privacyURL, true);
                consentSDK.LOG_TAG = this.LOG_TAG;
                consentSDK.DEVICE_ID = this.DEVICE_ID;
                return consentSDK;
            } else {
                return new ConsentSDK(context, publisherId, privacyURL);
            }
        }
    }

}
