package com.qa.adsshared.adsPackage.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.ShowAds;
import com.qa.adsshared.adsPackage.AdsShowingClass;






@SuppressLint({"WrongConstant", "ResourceType", "MissingPermission", "SetTextI18n"})
public class RateMeNowDialog {
    private static Dialog rateUsDialog = null;
    private static float rating_count = 3.0f;

    public static void showRateDialog(@NonNull final Activity activity, int i,String appName) {
        rating_count = i;
        Dialog dialog = new Dialog(activity);
        rateUsDialog = dialog;
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        rateUsDialog.setContentView(R.layout.rateme_dialog);
        rateUsDialog.setCancelable(true);
        final EditText editText = rateUsDialog.findViewById(R.id.ratingEt);
        final ImageView imageView = rateUsDialog.findViewById(R.id.emoji);
        ((RatingBar) rateUsDialog.findViewById(R.id.ratingBar)).setOnRatingBarChangeListener((ratingBar, f, z) -> {
            if (f == 0.0f) {
                ratingBar.setRating(1.0f);
            } else if (f == 1.0f) {
                imageView.setImageResource(R.drawable.emoji_1);
                RateMeNowDialog.rateUsDialog.dismiss();
                editText.setVisibility(8);
            } else if (f == 2.0f) {
                imageView.setImageResource(R.drawable.emoji_2);
                RateMeNowDialog.rateUsDialog.dismiss();
                editText.setVisibility(8);
            } else if (f == 3.0f) {
                imageView.setImageResource(R.drawable.emoji_3);
                RateMeNowDialog.rateUsDialog.dismiss();
                editText.setVisibility(8);
            } else if (f == 4.0f) {
                imageView.setImageResource(R.drawable.emoji_4);
                editText.setVisibility(8);
            } else if (f == 5.0f) {
                imageView.setImageResource(R.drawable.emoji_5);
                editText.setVisibility(8);
            }
        });
        rateUsDialog.findViewById(R.id.rateCancel).setOnClickListener(view -> {
            if (AdsSharedPref.getInstance(activity ).getABoolean(FirebaseConfigConst.IS_EXIT_DIALOG_INTERSTITIAL_SHOW)){
                AdsShowingClass.showInterStillAds(activity , new ShowAds() {
                    @Override
                    public void onAdsFinish() {
                        RateMeNowDialog.rateUsDialog.dismiss();

                    }
                });
            }else{
                RateMeNowDialog.rateUsDialog.dismiss();

            }


        });
        rateUsDialog.findViewById(R.id.rateSubmit).setOnClickListener(view -> {
            if (RateMeNowDialog.rating_count >= 2.0f) {
                RateMeNowDialog.rateUsDialog.dismiss();
                setRateUs(activity, true);
                RateMeNowDialog.RateUs(activity);
                return;
            }
            RateMeNowDialog.rateUsDialog.dismiss();
            RateMeNowDialog.gotoFeedBack(activity, (int) RateMeNowDialog.rating_count, editText.getText().toString().trim(),appName);
        });
        rateUsDialog.show();
    }

    public static void RateUs(@NonNull Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (Exception unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void gotoFeedBack(@NonNull Context context, int i, @Nullable String str,String appName) {
        String str2;
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        ResolveInfo resolveInfo = null;
        for (ResolveInfo resolveInfo2 : context.getPackageManager().queryIntentActivities(intent, 0)) {
            if (resolveInfo2.activityInfo.packageName.endsWith(".gm") || resolveInfo2.activityInfo.name.toLowerCase().contains("gmail")) {
                resolveInfo = resolveInfo2;
            }
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            str2 = packageInfo.versionName + "." + packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            str2 = "";
        }
        intent.putExtra("android.intent.extra.EMAIL", new String[]{"sitaramappstudio@gmail.com"});
        if (i == 0 && str == null) {
            intent.putExtra("android.intent.extra.TEXT", "Version :- " + str2);
        } else if (i != -1) {
            intent.putExtra("android.intent.extra.TEXT", "Version :- " + str2 + "\n Rating :- " + i + "\n\n " + str);
        } else {
            intent.putExtra("android.intent.extra.TEXT", "Version :- " + str2);
        }
        intent.putExtra("android.intent.extra.SUBJECT", "Feedback for " +appName + " app");
        intent.setPackage("com.google.android.gm");
        if (resolveInfo != null) {
            intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        try {
            context.startActivity(intent);
            setRateUs(context, true);
        } catch (ActivityNotFoundException unused) {
        }
    }

    public static boolean getRate(@NonNull Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean("shared_pradefs_rate_us", false);
    }

    public static void setRateUs(@NonNull Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putBoolean("shared_pradefs_rate_us", z);
        edit.commit();
    }


}
