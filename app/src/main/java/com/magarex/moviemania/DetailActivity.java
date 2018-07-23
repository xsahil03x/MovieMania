package com.magarex.moviemania;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.magarex.moviemania.Database.MovieRepository;
import com.magarex.moviemania.Models.FavouriteMovie;
import com.magarex.moviemania.Models.Movie;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.ViewModels.DetailsViewModel;
import com.magarex.moviemania.ViewModels.DetailsViewModelFactory;
import com.magarex.moviemania.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding mBinding;
    TabLayout tabs;
    ViewPager viewPager;
    Movie movie;
    private boolean isFavourite;
    ConstraintLayout mBottomSheet;
    BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        movie = (Movie) getIntent().getExtras().getSerializable("data");
        if (movie != null) {
            mBinding.setMovie(movie);

                mBottomSheet = findViewById(R.id.btmSheet);
                mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
                viewPager = findViewById(R.id.viewpager);
                viewPager.setOffscreenPageLimit(2);
                setupViewPager(viewPager);
                tabs = findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager, true);

                DetailsViewModelFactory factory = new DetailsViewModelFactory(movie.getId(),
                        ProjectUtils.API_KEY);
                DetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(
                        DetailsViewModel.class);
                viewModel.getFavourite().observe(this, integer -> {
                    if (integer != null) {
                        isFavourite = integer != 0;
                    }
                    mBinding.imgFavourite.setBackground(isFavourite ? ContextCompat.
                            getDrawable(DetailActivity.this, R.drawable.ic_favorite_black_24dp) :
                            ContextCompat.getDrawable(DetailActivity.this, R.drawable.ic_favorite_border_black_24dp));
                    Gson gson = new Gson();
                    String json = gson.toJson(movie);
                    final FavouriteMovie favouriteMovie = gson.fromJson(json, FavouriteMovie.class);
                    mBinding.imgFavourite.setOnClickListener(v -> {
                        //toggleFavouriteIcon((FloatingActionButton) v);
                        MovieRepository.getInstance().refreshFavouriteMovies(favouriteMovie,
                                isFavourite);
                        if (isFavourite) {
                            Toast.makeText(DetailActivity.this, "Movie Removed Trom Favourites",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Movie Added To Favourites",
                                    Toast.LENGTH_SHORT).show();
                        }
                        isFavourite = !isFavourite;
                    });
                });

        }
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CastFragment.newInstance(movie.getId()), "Casts");
        adapter.addFragment(TrailerFragment.newInstance(movie.getId()), "Trailers");
        adapter.addFragment(ReviewFragment.newInstance(movie.getId()), "Reviews");
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
