package com.qa.adsshared.adsPackage.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qa.adsshared.FirebaseConfigConst;
import com.qa.adsshared.R;
import com.qa.adsshared.adsPackage.DialogDismissUpdate;

@SuppressLint({"WrongConstant", "ResourceType", "MissingPermission"})

public class RemoteUpdateDialog extends Dialog {
    private final Context ctx;
    String appName = "";
    String versionName = "";
    String applicationId = "";
    Drawable appIcon = null;
    DialogDismissUpdate listener =null;

    public RemoteUpdateDialog(Context context, String appName, String versionName, String applicationId, Drawable appIcon, DialogDismissUpdate listener) {
        super(context);
        this.ctx = context;
        this.appName = appName;
        this.versionName = versionName;
        this.applicationId = applicationId;
        this.appIcon = appIcon;
        this.listener = listener;

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_update);

        ImageView imageViewIcon = findViewById(R.id.imageViewIcon);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewUpdate = findViewById(R.id.textViewUpdate);
        TextView textViewLater = findViewById(R.id.textViewLater);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        textViewTitle.setText(appName + " " + versionName);
        if (AdsSharedPref.getInstance(ctx).getABoolean(FirebaseConfigConst.IS_APP_UPDATE_FORCEFULLY_NOT)) {
            textViewLater.setVisibility(View.GONE);
            setCancelable(false);
        } else {
            textViewLater.setVisibility(View.VISIBLE);
        }
        textViewLater.setOnClickListener(view -> RemoteUpdateDialog.this.m107lambda$onCreate$0$commelaappcutoutdialogRemoteUpdateDialog(view));
        textViewUpdate.setOnClickListener(v -> RemoteUpdateDialog.this.ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" +applicationId))));
        getWindow().setLayout(-1, -2);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        textViewDescription.setText(String.format(getContext().getString(R.string.picasy_update), appName));
        imageViewIcon.setImageDrawable(appIcon);
    }


    public void m107lambda$onCreate$0$commelaappcutoutdialogRemoteUpdateDialog(View v) {
        if (listener!=null){
            listener.dismissDialogClick();
        }
        dismiss();
    }

    @Override
    public void onBackPressed() {
        if (AdsSharedPref.getInstance(ctx).getABoolean(FirebaseConfigConst.IS_APP_UPDATE_FORCEFULLY_NOT)) {
            ((Activity) this.ctx).finish();
        }
    }
}
