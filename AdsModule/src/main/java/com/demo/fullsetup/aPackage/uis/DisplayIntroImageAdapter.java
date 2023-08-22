package com.demo.fullsetup.aPackage.uis;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.demo.fullsetup.R;

import java.util.List;


public class DisplayIntroImageAdapter extends PagerAdapter {
    Context context;
    List<Drawable> imageList;
    LayoutInflater layoutInflater;

    public DisplayIntroImageAdapter(@NonNull Context context, List<Drawable> list) {
        this.context = context;
        this.imageList = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public int getCount() {
        return this.imageList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        View inflate = this.layoutInflater.inflate(R.layout.item_intro_screen_image, viewGroup, false);
        ImageView gestureImageView = inflate.findViewById(R.id.iv_display);
        Glide.with(this.context)
                .load(this.imageList.get(i))
                .into(gestureImageView);
        viewGroup.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((RelativeLayout) obj);
    }
}
