package com.salvatierra.vinet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.salvatierra.vinet.R;
import com.salvatierra.vinet.model.BannerMoviews;

import java.util.List;

public class BannerHomePageAdapter extends PagerAdapter {
    private Context context;
    private List<BannerMoviews> bannerMoviewsList;

    public BannerHomePageAdapter(){
    }

    public BannerHomePageAdapter(Context context, List<BannerMoviews> bannerMoviewsList){
        this.context = context;
        this.bannerMoviewsList = bannerMoviewsList;
    }

    @Override
    public int getCount() {
        return this.bannerMoviewsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_movie_layout, null);

        ImageView bannerImage = view.findViewById(R.id.banner_image);

        Glide.with(context).load(bannerMoviewsList.get(position).getImageUrl()).into(bannerImage);
        container.addView(view);

        bannerImage.setOnClickListener(v -> {
            Toast.makeText(context, bannerMoviewsList.get(position).getMovieName(), Toast.LENGTH_LONG).show();
        });

        return view;
    }
}
