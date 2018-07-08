package com.magarex.moviemania.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.magarex.moviemania.R;
import com.magarex.moviemania.Utils.GlideApp;

public class SliderAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private int[] gifs = {
            R.drawable.gifone,
            R.drawable.giftwo,
            R.drawable.gifthree
    };

    public SliderAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return gifs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflator.inflate(R.layout.activity_on_boarding_item, container, false);

        ImageView vp_item = view.findViewById(R.id.vp_item);
        GlideApp.with(mContext)
                .asGif()
                .load(gifs[position])
                .into(vp_item);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
