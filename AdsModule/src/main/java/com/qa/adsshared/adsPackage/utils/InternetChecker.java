package com.qa.adsshared.adsPackage.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class InternetChecker {

    public static InternetChecker internetChecker;

    public static InternetChecker getInstance() {
        if (internetChecker == null) {
            internetChecker = new InternetChecker();
        }
        return internetChecker;
    }

    public boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

                if (capabilities != null) {
                    if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_SUPL))
                        return true;
                    else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                        return true;
                    else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                        return true;
                    else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                        return true;
                    else return false;
                }

            } else {
                try {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    return networkInfo != null && networkInfo.isConnected();
                } catch (NullPointerException e) {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
