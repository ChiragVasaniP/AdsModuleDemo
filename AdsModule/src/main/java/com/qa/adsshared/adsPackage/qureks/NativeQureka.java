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
import com.qa.adsshared.adsPackage.faci.NativeFacebook;
import com.qa.adsshared.adsPackage.googi.NativeAdmobGoogle;
import com.qa.adsshared.adsPackage.utils.AdsDialog;
import com.qa.adsshared.adsPackage.utils.AdsSharedPref;
import com.qa.adsshared.adsPackage.utils.Utils;

public class NativeQureka {

    public static void loadNativeQureka(Context context, FrameLayout frameLayout, boolean isShowBig) {
        if (Utils.appInstalledOrNot("com.android.chrome", context)) {
            showNativeQuereka(context, frameLayout, isShowBig);
        } else {
            if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.QUERKA_FAIL_SHOW_ADMOB) {
                NativeAdmobGoogle.loadNativeAds(context, frameLayout, isShowBig);
            } else if (AdsSharedPref.getInstance(context).getInt(FirebaseConfigConst.ADS_SEQUENCE) == FirebaseConfigConst.QUERKA_FAIL_SHOW_FB) {
                NativeFacebook.loadNativeAds(context, frameLayout, isShowBig);
            }else{
                showNativeQuereka(context, frameLayout, isShowBig);
            }
        }
    }

    private static void showNativeQuereka(Context context, FrameLayout frameLayout, boolean isShowBig){
        if (isShowBig){
            LayoutInflater inflater = LayoutInflater.from(context);
            RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.native_qureka, (ViewGroup) frameLayout, false);
            frameLayout.addView(view);
            ImageView imgQuereka = view.findViewById(R.id.qureka_imageview);
            Button btnQurekaBig = view.findViewById(R.id.btn_qureka);
            Glide
                    .with(context)
                    .load(AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.QUREKA_NATIVE_IMAGE_URL))
                    .into(imgQuereka);

            btnQurekaBig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.appInstalledOrNot("com.android.chrome", context)){
                        Utils.openChromeCustomTabPrediction(context, AdsSharedPref.getInstance(context).getString(FirebaseConfigConst.QUREKA_CLICK_URL));
                    }else{
                        AdsDialog.showErrorDialog(context);
                    }

                }
            });
        }else{
            BannerAdsQureka.loadBannerQureka(context,frameLayout);
        }
    }
}
