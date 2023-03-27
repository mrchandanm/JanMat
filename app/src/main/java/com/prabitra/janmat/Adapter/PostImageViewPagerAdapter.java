package com.prabitra.janmat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.prabitra.janmat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostImageViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> imagesList;

    public PostImageViewPagerAdapter(Context mContext, ArrayList<String> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = LayoutInflater.from(mContext).inflate(R.layout.upload_image_viewpager_layout, container, false);
        assert imageLayout != null;
        ImageView imageView = imageLayout.findViewById(R.id.cart_upload_image_new_imageview);
        Picasso.get().load(imagesList.get(position)).into(imageView);
        imageView.setDrawingCacheEnabled(true);
        container.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
