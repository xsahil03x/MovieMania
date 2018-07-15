package com.magarex.moviemania;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.magarex.moviemania.Models.Movie;
import com.magarex.moviemania.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding mBinding;
    TabLayout tabs;
    ViewPager viewPager;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        movie = (Movie) getIntent().getExtras().getSerializable("data");
        if (movie != null) {
            mBinding.setMovie(movie);
            viewPager = findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager, true);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CastFragment.newInstance(movie.getId()), "Cast");
        adapter.addFragment(new ReviewFragment(), "Review");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
