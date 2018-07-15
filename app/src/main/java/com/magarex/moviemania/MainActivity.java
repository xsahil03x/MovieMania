package com.magarex.moviemania;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.magarex.moviemania.Adapter.MovieAdapter;
import com.magarex.moviemania.Utils.GridSpacingItemDecoration;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.ViewModels.MovieViewModel;
import com.magarex.moviemania.ViewModels.MovieViewModelFactory;
import com.magarex.moviemania.databinding.ActivityMainBinding;

import java.util.Objects;

import static com.magarex.moviemania.Utils.ProjectUtils.dpToPx;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mAdapter;
    private RecyclerView rv_movies;
    private ShimmerFrameLayout mShimmerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean(OnBoardingActivity.COMPLETED_ONBOARDING, false)) {
            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();
        }
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        rv_movies = mBinding.rvMovies;
        mShimmerLayout = mBinding.shimmerloading;
        setSupportActionBar(mBinding.toolbar);
        initViews();
    }

    private void initViews() {
        mShimmerLayout.startShimmer();
        mAdapter = new MovieAdapter(this);
        rv_movies.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        rv_movies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_movies.setItemAnimator(new DefaultItemAnimator());
        rv_movies.setAdapter(mAdapter);
        loadFromSharedPrefs();
    }

    private void loadFromSharedPrefs() {
        //noInternet.setVisibility(View.GONE);

        int loadingIdentifier = ProjectUtils.SharedPreferenceHelper.contains(ProjectUtils.PREF_FILTER) ? 2 : 1;

        switch (loadingIdentifier) {
            case 1:
                ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR);
                loadMovies(ProjectUtils.FILTER_POPULAR, false);
                break;
            case 2:
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_TOP_RATED)) {
                    loadMovies(ProjectUtils.FILTER_TOP_RATED, false);
                } else if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_POPULAR)) {
                    loadMovies(ProjectUtils.FILTER_POPULAR, false);
                }
                break;
            default:
                break;
        }
    }

    private void loadMovies(String filter, Boolean isFilterChanged) {
        MovieViewModelFactory mFactory = new MovieViewModelFactory(filter, ProjectUtils.API_KEY);
        MovieViewModel mViewModel = ViewModelProviders.of(this, mFactory).get(MovieViewModel.class);

        if (isFilterChanged) {
            mViewModel.loadFromNetwork(filter, ProjectUtils.API_KEY);
        }
        mViewModel.getResults().observe(this, movieModel -> {
            if (movieModel != null) {
                mShimmerLayout.stopShimmer();
                mShimmerLayout.setVisibility(View.GONE);
                rv_movies.setVisibility(View.VISIBLE);
                mAdapter.addMoviesList(movieModel.getMovies());
            } else {
                mAdapter.addMoviesList(null);
                //mMainBinding.noConnectionLl.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem sort = menu.findItem(R.id.action_sort);
        sort.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_sort) {
                filterMovies();
            }
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filterMovies() {
        View view = View.inflate(this, R.layout.custom_bottom_sheet, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        TextView txvPopular = view.findViewById(R.id.txv_popular);
        TextView txvTopRated = view.findViewById(R.id.txv_top_rated);
        final ImageView imvPopular = view.findViewById(R.id.imv_popular);
        final ImageView imvTopRated = view.findViewById(R.id.imv_top_rated);
        ImageView close = view.findViewById(R.id.imv_close);

        final int filterIdentifier = ProjectUtils.SharedPreferenceHelper.contains(ProjectUtils.PREF_FILTER) ? 2 : 1;

        switch (filterIdentifier) {
            case 1:
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                break;
            case 2:
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_TOP_RATED)) {
                    imvTopRated.setVisibility(View.VISIBLE);
                    imvPopular.setVisibility(View.GONE);
                } else if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_POPULAR)) {
                    imvTopRated.setVisibility(View.GONE);
                    imvPopular.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

        txvPopular.setOnClickListener(v -> {
            rv_movies.setVisibility(View.GONE);
            mShimmerLayout.setVisibility(View.VISIBLE);
            mShimmerLayout.startShimmer();
            ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR);
            imvPopular.setVisibility(View.VISIBLE);
            imvTopRated.setVisibility(View.GONE);
            loadMovies(ProjectUtils.FILTER_POPULAR, true);
            dialog.dismiss();
        });

        txvTopRated.setOnClickListener(v -> {
            rv_movies.setVisibility(View.GONE);
            mShimmerLayout.setVisibility(View.VISIBLE);
            mShimmerLayout.startShimmer();
            ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_TOP_RATED);
            imvPopular.setVisibility(View.GONE);
            imvTopRated.setVisibility(View.VISIBLE);
            loadMovies(ProjectUtils.FILTER_TOP_RATED, true);
            dialog.dismiss();
        });

        close.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }
}
