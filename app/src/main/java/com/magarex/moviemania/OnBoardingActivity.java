package com.magarex.moviemania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magarex.moviemania.Adapter.SliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    public static final String COMPLETED_ONBOARDING = "completed_onboarding";
    private ViewPager vp_tutorial;
    private LinearLayout dots;
    private ImageButton btnNext, btnBack;
    private int mCurrentPage;
    private SliderAdapter mAdapter;

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPage = position;
            addDotIndicator(position);


            SliderAdapter adapter = (SliderAdapter) vp_tutorial.getAdapter();
            if (adapter != null) {
                adapter.playVideoAtPosition(position);
            }

            if (position == 0) {
                btnBack.setEnabled(false);
                btnBack.setVisibility(View.INVISIBLE);
                btnNext.setEnabled(true);
            } else if (position == 1) {
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
            } else if (position == 2) {
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        vp_tutorial = findViewById(R.id.vp_tutorial);
        dots = findViewById(R.id.dots);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        vp_tutorial.setOffscreenPageLimit(2);

        mAdapter = new SliderAdapter(OnBoardingActivity.this);
        vp_tutorial.setAdapter(mAdapter);
        mAdapter.playVideoAtPosition(0);
        addDotIndicator(0);
        vp_tutorial.addOnPageChangeListener(viewListener);
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(() -> mAdapter.playVideoAtPosition(0));
    }

    public void setNext(View view) {
        if (mCurrentPage == 2) {
            SharedPreferences.Editor sharedPreferencesEditor =
                    PreferenceManager.getDefaultSharedPreferences(OnBoardingActivity.this).edit();
            sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING, true);
            sharedPreferencesEditor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            vp_tutorial.setCurrentItem(mCurrentPage + 1, true);
        }
    }

    public void setBack(View view) {
        vp_tutorial.setCurrentItem(mCurrentPage - 1, true);
    }

    public void addDotIndicator(int position) {
        TextView[] mDots = new TextView[mAdapter.getCount()];
        dots.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(OnBoardingActivity.this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.shimmer_background_color));
            dots.addView(mDots[i]);
        }

        mDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
    }

}
