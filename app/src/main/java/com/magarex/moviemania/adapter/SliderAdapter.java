//package com.magarex.moviemania.Adapter;
//
//import android.content.Context;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v4.view.PagerAdapter;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.VideoView;
//
//import com.magarex.moviemania.R;
//
//import java.util.ArrayList;
//
//public class SliderAdapter extends PagerAdapter {
//    private static final String TAG = "SliderAdapter";
//
//    private ArrayList<VideoView> videoViews = new ArrayList<>();
//    private Context mContext;
//    private boolean isPlaying = false;
//    private VideoView mPlayingView;
//    private int[] videos = {
//            R.raw.sample1,
//            R.raw.sample3,
//            R.raw.sampletwo
//    };
//
//    public SliderAdapter(Context context) {
//        this.mContext = context;
//    }
//
//    @Override
//    public int getCount() {
//        return videos.length;
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == object;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        LayoutInflater mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = mInflator.inflate(R.layout.activity_on_boarding_item, container, false);
//
//        VideoView vp_item = view.findViewById(R.id.vp_item);
////        MediaController mediaController = new MediaController(mContext);
////        mediaController.setAnchorView(vp_item);
////        vp_item.setMediaController(mediaController);
//        vp_item.setVideoURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + videos[position]));
//
//        //vp_item.start();
//
//        container.addView(view);
//        videoViews.add(vp_item);
//        Log.i(TAG, "instantiateItem: here");
//
//        return view;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((RelativeLayout) object);
//    }
//
//    public void playVideoAtPosition(int position) {
//        if (isPlaying) {
//            mPlayingView.pause();
//            isPlaying = false;
//        }
//        if (videoViews.size() > 0) {
//            mPlayingView = videoViews.get(position);
//            mPlayingView.start();
//            isPlaying = true;
//        }
//    }
//}
