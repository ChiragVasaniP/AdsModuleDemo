package com.qa.adsshared.adsPackage.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.qa.adsshared.R;
import com.qa.adsshared.ShowRewardAds;
import com.qa.adsshared.adsPackage.RewardAds;



public class AdsDialog {

    public static void showPremiumDialog(Context context, PremiumDialogClick listener) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_premium);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCanceledOnTouchOutside(false);

        AppCompatButton btnCancel = dialog.findViewById(R.id.btn_cancel);
        AppCompatButton btnWatch = dialog.findViewById(R.id.btn_watch);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.isRewardApproved(false);
                dialog.dismiss();
            }
        });
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardAds.loadReward(context, new ShowRewardAds() {
                    @Override
                    public void showRewardAdsValue(boolean isRewardable) {
                        if (isRewardable){
                            listener.isRewardApproved(true);
                        }else{
                            listener.isRewardApproved(false);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
/*        btnWatch.setOnClickListener(v -> RewardAds.loadReward(context, isRewardable -> {
            if (isRewardable) {
                listener.isRewardApproved(true);
            } else {
                listener.isRewardApproved(false);
                Toast.makeText(context, context.getString(R.string.str_reward_failed), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }));*/
        dialog.show();
    }


    public static void showInternetConnectionDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_internet_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCanceledOnTouchOutside(false);

        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        TextView txt_retry = dialog.findViewById(R.id.txt_retry);

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finishAffinity();
                dialog.dismiss();
            }
        });

        txt_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          if (InternetChecker.getInstance().isConnectingToInternet(context)){
              dialog.dismiss();
              ((Activity)context).recreate();
          }else {
              Toast.makeText(context, "No Internet Found", Toast.LENGTH_SHORT).show();
          }
            }
        });
        dialog.show();
    }



    public static void showErrorDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custome);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCanceledOnTouchOutside(false);

        TextView textViewLater = dialog.findViewById(R.id.textViewLater);
        TextView textViewUpdate = dialog.findViewById(R.id.textViewUpdate);

        textViewLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

   

            }
        });

        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.android.chrome"));
                    appStoreIntent.setPackage("com.android.vending");
                    context.startActivity(appStoreIntent);
                } catch (android.content.ActivityNotFoundException exception) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.android.chrome")));
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface PremiumDialogClick {
        void isRewardApproved(boolean isShowOrNotApproved);
    }
}