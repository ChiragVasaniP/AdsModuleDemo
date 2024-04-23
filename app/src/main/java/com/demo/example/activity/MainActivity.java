package com.demo.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qa.adsshared.adsPackage.AdsShowingClass;
import com.qa.adsshared.adsPackage.utils.OnOneOffClickListener;
import com.demo.example.R;

public class MainActivity extends AppCompatActivity {

    int txtclickCount = 0;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCount = findViewById(R.id.txt_click_count);

        AdsShowingClass.showNativeAds(MainActivity.this, findViewById(R.id.frame_native_big), true);

        addTextClick();

        findViewById(R.id.btn_show_inter).setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                txtclickCount++;
                addTextClick();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    private void addTextClick() {
        txtCount.setText(String.valueOf(txtclickCount));
    }
}