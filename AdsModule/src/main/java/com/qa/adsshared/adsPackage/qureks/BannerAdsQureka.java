package com.qa.adsshared.adsPackage.qureks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.faci.BannerAdsFaceBook;
import com.qa.adsshared.adsPackage.googi.BannerAdsAdmobGoogle;
import com.qa.adsshared.adsPackage.utils.AdsDialog;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;
import com.qa.adsshared.adsPackage.utils.Utils;

public class BannerAdsQureka {

    public static void loadBannerQureka(Context context, FrameLayout frameLayout) {
        if (Utils.appInstalledOrNot("com.android.chrome", context)) {
            loadBannerAdsQuereka(context, frameLayout);
        } else {
            if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB) {
                BannerAdsAdmobGoogle.loadAdvanceBannerAds(context, frameLayout);
            } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.QUERKA_FAIL_SHOW_FB) {
                BannerAdsFaceBook.loadBanner90(context, frameLayout);
            }else{
                loadBannerAdsQuereka(context,frameLayout);
            }
        }
    }

    private static void loadBannerAdsQuereka(Context context, FrameLayout frameLayout) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.banner_qureka, (ViewGroup) frameLayout, false);
        frameLayout.addView(view);

        ImageView imgQuereka = view.findViewById(R.id.qureka_imageview);

        Glide
                .with(context)
                .load(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.QUREKA_BANNER_IMAGE_URL))
                .into(imgQuereka);


        Button btnQurekaSmall = view.findViewById(R.id.btn_qureka_small);

        btnQurekaSmall.setVisibility(View.VISIBLE);
        btnQurekaSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.appInstalledOrNot("com.android.chrome", context)){
                    Utils.openChromeCustomTabPrediction(context, AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.QUREKA_CLICK_URL));
                }else{
                    AdsDialog.showErrorDialog(context);
                }
            }
        });
    }


}
