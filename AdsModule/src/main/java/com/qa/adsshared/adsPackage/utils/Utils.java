package com.qa.adsshared.adsPackage.utils;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import java.util.List;

public class Utils {


    public static void openChromeCustomTabPrediction(Context context, String getquereka_link) {

        try {
            if (appInstalledOrNot("com.android.chrome", context)) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                int coolorInt = Color.parseColor("#448AFF");
                builder.setToolbarColor(coolorInt);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(context, Uri.parse(getquereka_link));
            } else {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                int coolorInt = Color.parseColor("#448AFF");
                builder.setToolbarColor(coolorInt);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(context, Uri.parse(getquereka_link));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static final boolean ENABLED = true;
    public static final boolean DISABLED = false;
    public static final boolean NOT_INSTALLED = false;


    public static boolean appInstalledOrNot(@NonNull String packageName, @NonNull Context context) {
        final PackageManager packageManager = context.getPackageManager();


        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return NOT_INSTALLED;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {

            return NOT_INSTALLED;
        } else {

            int appEnabledSetting = packageManager.getApplicationEnabledSetting(packageName);
            if (appEnabledSetting == COMPONENT_ENABLED_STATE_DISABLED ||
                    appEnabledSetting == COMPONENT_ENABLED_STATE_DISABLED_USER) {
                return DISABLED;
            } else {
                return ENABLED;
            }
        }
    }
}
