package com.shared.ads.libs.adsPackage.qureks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shared.ads.libs.FirebaseConfigConst;
import com.shared.ads.libs.R;
import com.shared.ads.libs.ShowAds;
import com.shared.ads.libs.adsPackage.faci.InterstitialFaceBook;
import com.shared.ads.libs.adsPackage.googi.InterstitialAdmobGoogle;
import com.shared.ads.libs.adsPackage.utils.AdsDialog;
import com.shared.ads.libs.adsPackage.utils.AdsSharedPref;
import com.shared.ads.libs.adsPackage.utils.Utils;

public class InterstitialQuark {


    public static void loadInterstitialQuark(Context context, ShowAds interfaceAds) {
        if ( Utils.appInstalledOrNot("com.android.chrome", context)) {
            openInterstitialAds(context, interfaceAds);
        } else {
            if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB) {
                InterstitialAdmobGoogle.loadInterstitialSingleShow(context, interfaceAds);
            } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.QUERKA_FAIL_SHOW_FB) {
                InterstitialFaceBook.showInterStillAds(context, interfaceAds);
            }else{
                openInterstitialAds(context, interfaceAds);

            }
        }


    }

    private static void openInterstitialAds(Context context, ShowAds interfaceAds){
        Dialog dialog= new Dialog(context);
        dialog.setContentView(R.layout.dialog_interstitial_quark);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        ImageView imgQuereka=dialog.findViewById(R.id.qureka_imageview);


        Glide
                .with(context)
                .load(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.QUREKA_INTERSTITIAL_IMAGE_URL))
                .into(imgQuereka);
        ImageView imgClose=dialog.findViewById(R.id.img_close_int);
        Button btnPlayInter=dialog.findViewById(R.id.btn_play_inter);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                interfaceAds.onAdsFinish();
            }
        });


        btnPlayInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.appInstalledOrNot("com.android.chrome", context)){
                    Utils.openChromeCustomTabPrediction(context, AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.QUREKA_CLICK_URL));
                }else{
                    AdsDialog.showErrorDialog(context);
                }

            }
        });
        dialog.show();
    }



}
