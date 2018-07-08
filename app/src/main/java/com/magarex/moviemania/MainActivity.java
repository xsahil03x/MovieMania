package com.magarex.moviemania;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.magarex.moviemania.Adapter.MovieAdapter;
import com.magarex.moviemania.Database.MovieDatabase;
import com.magarex.moviemania.Models.MovieModel;
import com.magarex.moviemania.Utils.GridSpacingItemDecoration;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.ViewModels.MovieViewModel;
import com.magarex.moviemania.ViewModels.MovieViewModelFactory;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_movies, rv_favourites;
    private MovieAdapter mAdapter;
    private ShimmerFrameLayout mShimmerLayout;
    private MovieDatabase mMovieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if (!sharedPreferences.getBoolean(OnBoardingActivity.COMPLETED_ONBOARDING, false)) {
//            // This is the first time running the app, let's go to onboarding
//            startActivity(new Intent(this, OnBoardingActivity.class));
//        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMovieDatabase = MovieDatabase.getInstance(this);
        mShimmerLayout = findViewById(R.id.shimmerloading);
        mShimmerLayout.startShimmer();
        mAdapter = new MovieAdapter(this);
        rv_favourites = findViewById(R.id.rv_favourites);
        rv_favourites.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        rv_favourites.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_favourites.setItemAnimator(new DefaultItemAnimator());
        rv_favourites.setAdapter(mAdapter);
        rv_movies = findViewById(R.id.rv_movies);
        rv_movies.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        rv_movies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_movies.setItemAnimator(new DefaultItemAnimator());
        rv_movies.setAdapter(mAdapter);
        loadFromSharedPrefs();
    }

    private void loadFromSharedPrefs() {
        //noInternet.setVisibility(View.GONE);

        int loadingIdentifier = ProjectUtils.SharedPreferenceHelper.contains(ProjectUtils.PREF_FILTER, this) ? 2 : 1;

        switch (loadingIdentifier) {
            case 1:
                ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR, this);
                loadMovies(ProjectUtils.FILTER_POPULAR);
                break;
            case 2:
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null, this).equals(ProjectUtils.FILTER_TOP_RATED)) {
                    loadMovies(ProjectUtils.FILTER_TOP_RATED);
                } else if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null, this).equals(ProjectUtils.FILTER_POPULAR)) {
                    loadMovies(ProjectUtils.FILTER_POPULAR);
                }
                break;
            default:
                break;
        }
    }

    private void loadMovies(String filter) {
        MovieViewModelFactory mFactory = new MovieViewModelFactory(filter, ProjectUtils.API_KEY);
        MovieViewModel mViewModel = ViewModelProviders.of(this, mFactory).get(MovieViewModel.class);

        mViewModel.getResults().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(@Nullable MovieModel movieModel) {
                if (movieModel != null) {
//                    rv_favourites.setVisibility(View.VISIBLE);
                    rv_movies.setVisibility(View.VISIBLE);
                    mAdapter.addMoviesList(movieModel.getResults());
                } else {
                    mAdapter.addMoviesList(null);
                    //mMainBinding.noConnectionLl.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem sort = menu.findItem(R.id.action_sort);
        sort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_sort) {
                    filterMovies();
                }
                return false;
            }
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

        final int filterIdentifier = ProjectUtils.SharedPreferenceHelper.contains(ProjectUtils.PREF_FILTER, this) ? 2 : 1;

        switch (filterIdentifier) {
            case 1:
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                break;
            case 2:
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null, this).equals(ProjectUtils.FILTER_TOP_RATED)) {
                    imvTopRated.setVisibility(View.VISIBLE);
                    imvPopular.setVisibility(View.GONE);
                } else if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(ProjectUtils.PREF_FILTER, null, this).equals(ProjectUtils.FILTER_POPULAR)) {
                    imvTopRated.setVisibility(View.GONE);
                    imvPopular.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

        txvPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_movies.setVisibility(View.GONE);
                mShimmerLayout.setVisibility(View.VISIBLE);
                mShimmerLayout.startShimmer();
                ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR, MainActivity.this);
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                loadMovies(ProjectUtils.FILTER_POPULAR);
                dialog.dismiss();
            }
        });

        txvTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_movies.setVisibility(View.GONE);
                mShimmerLayout.setVisibility(View.VISIBLE);
                mShimmerLayout.startShimmer();
                ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_TOP_RATED, MainActivity.this);
                imvPopular.setVisibility(View.GONE);
                imvTopRated.setVisibility(View.VISIBLE);
                loadMovies(ProjectUtils.FILTER_TOP_RATED);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

//    private void loadMovies(final String filter, int loadingIdentifier) {
//        Retrofit retrofit = ProjectUtils.getClient();
//        MovieApi client = retrofit.create(MovieApi.class);
//        Call<MovieModel> call = client.getMoviesByPreference(filter, ProjectUtils.API_KEY);
//        call.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    mShimmerLayout.stopShimmer();
//                    mShimmerLayout.setVisibility(View.GONE);
//                    rv_movies.setVisibility(View.VISIBLE);
//                    List<Result> res = response.body().getResults();
//                    for (Result movie : res) {
//                        movie.setCriteria(filter);
//                    }
//                    mAdapter.addMoviesList(res);
//                    if (mMovieDatabase.movieDao().getMoviesByCriteria(filter).getValue() != null && !mMovieDatabase.movieDao().getMoviesByCriteria(filter).getValue().isEmpty()) {
//                        mMovieDatabase.movieDao().deleteMoviesByCriteria(filter);
//                        mMovieDatabase.movieDao().insertMoviesToDb(res);
//                    } else {
//                        mMovieDatabase.movieDao().insertMoviesToDb(res);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//                Log.d("Error", t.getMessage());
//                mShimmerLayout.stopShimmer();
//                mShimmerLayout.setVisibility(View.GONE);
//            }
//
//        });
//    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }
}
