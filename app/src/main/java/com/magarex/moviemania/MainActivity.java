package com.magarex.moviemania;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.Group;
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
import android.widget.Toast;

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
    private Group noInternetGroup, noFavouriteGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        rv_movies = mBinding.rvMovies;
        mShimmerLayout = mBinding.shimmerloading;
        noInternetGroup = mBinding.noInternetGroup;
        noFavouriteGroup = mBinding.noFavouriteGroup;
        setSupportActionBar(mBinding.toolbar);
        initViews();

        mBinding.btnTryAgain.setOnClickListener(view -> {
            if (ProjectUtils.isNetworkAvailable()) {
                loadMovies(ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR), true);
            } else {
                Toast.makeText(this, "Oops no Internet, Check your Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        mAdapter = new MovieAdapter(this);
        rv_movies.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        rv_movies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_movies.setItemAnimator(new DefaultItemAnimator());
        rv_movies.setAdapter(mAdapter);
        loadFromSharedPrefs();
    }

    private void loadFromSharedPrefs() {
        int loadingIdentifier = ProjectUtils.SharedPreferenceHelper.contains(
                ProjectUtils.PREF_FILTER) ? 2 : 1;

        switch (loadingIdentifier) {
            case 1:
                ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(
                        ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR);
                loadMovies(ProjectUtils.FILTER_POPULAR, false);
                break;
            case 2:
                switch (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(
                        ProjectUtils.PREF_FILTER, null)) {
                    case ProjectUtils.FILTER_TOP_RATED:
                        loadMovies(ProjectUtils.FILTER_TOP_RATED, false);
                        break;
                    case ProjectUtils.FILTER_POPULAR:
                        loadMovies(ProjectUtils.FILTER_POPULAR, false);
                        break;
                    case ProjectUtils.FILTER_FAVOURITE:
                        loadMovies(ProjectUtils.FILTER_FAVOURITE, false);
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void loadMovies(String filter, Boolean isFilterChanged) {
        mShimmerLayout.startShimmer();
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
                mShimmerLayout.stopShimmer();
                mShimmerLayout.setVisibility(View.GONE);
                mAdapter.addMoviesList(null);
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_FAVOURITE)) {
                    noFavouriteGroup.setVisibility(View.VISIBLE);
                } else {
                    noInternetGroup.setVisibility(View.VISIBLE);
                }
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

        final BottomSheetDialog dialog = new BottomSheetDialog(this,
                R.style.BottomSheetDialogTheme);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        TextView txvPopular = view.findViewById(R.id.txv_popular);
        TextView txvTopRated = view.findViewById(R.id.txv_top_rated);
        TextView txvFavourite = view.findViewById(R.id.txv_favourite);
        final ImageView imvPopular = view.findViewById(R.id.imv_popular);
        final ImageView imvTopRated = view.findViewById(R.id.imv_top_rated);
        final ImageView imvFavourite = view.findViewById(R.id.imv_favourite);
        ImageView close = view.findViewById(R.id.imv_close);

        final int filterIdentifier = ProjectUtils.SharedPreferenceHelper.contains(
                ProjectUtils.PREF_FILTER) ? 2 : 1;

        switch (filterIdentifier) {
            case 1:
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                imvFavourite.setVisibility(View.GONE);
                break;
            case 2:
                switch (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(
                        ProjectUtils.PREF_FILTER, null)) {
                    case ProjectUtils.FILTER_TOP_RATED:
                        imvTopRated.setVisibility(View.VISIBLE);
                        imvPopular.setVisibility(View.GONE);
                        imvFavourite.setVisibility(View.GONE);
                        break;
                    case ProjectUtils.FILTER_POPULAR:
                        imvTopRated.setVisibility(View.GONE);
                        imvPopular.setVisibility(View.VISIBLE);
                        imvFavourite.setVisibility(View.GONE);
                        break;
                    case ProjectUtils.FILTER_FAVOURITE:
                        imvFavourite.setVisibility(View.VISIBLE);
                        imvPopular.setVisibility(View.GONE);
                        imvTopRated.setVisibility(View.GONE);
                        break;
                }
                break;
            default:
                break;
        }

        txvPopular.setOnClickListener(v -> {
            showShimmer();
            noFavouriteGroup.setVisibility(View.GONE);
            ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER,
                    ProjectUtils.FILTER_POPULAR);
            imvPopular.setVisibility(View.VISIBLE);
            imvTopRated.setVisibility(View.GONE);
            imvFavourite.setVisibility(View.GONE);
            loadMovies(ProjectUtils.FILTER_POPULAR, true);
            dialog.dismiss();
        });

        txvTopRated.setOnClickListener(v -> {
            showShimmer();
            noFavouriteGroup.setVisibility(View.GONE);
            ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER,
                    ProjectUtils.FILTER_TOP_RATED);
            imvPopular.setVisibility(View.GONE);
            imvFavourite.setVisibility(View.GONE);
            imvTopRated.setVisibility(View.VISIBLE);
            loadMovies(ProjectUtils.FILTER_TOP_RATED, true);
            dialog.dismiss();
        });

        txvFavourite.setOnClickListener(v -> {
            showShimmer();
            ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER,
                    ProjectUtils.FILTER_FAVOURITE);
            imvPopular.setVisibility(View.GONE);
            imvTopRated.setVisibility(View.GONE);
            imvFavourite.setVisibility(View.VISIBLE);
            loadMovies(ProjectUtils.FILTER_FAVOURITE, true);
            dialog.dismiss();
        });

        close.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showShimmer() {
        rv_movies.setVisibility(View.GONE);
        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_FAVOURITE)) {
            loadMovies(ProjectUtils.FILTER_FAVOURITE, true);
        }
    }
}
