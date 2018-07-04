package com.magarex.moviemania;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.magarex.moviemania.Adapter.MovieAdapter;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.MovieModel;
import com.magarex.moviemania.Models.Result;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.Utils.GridSpacingItemDecoration;

import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_movies;
    private MovieAdapter mAdapter;
    private ShimmerFrameLayout mShimmerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mShimmerLayout = findViewById(R.id.shimmerloading);
        mShimmerLayout.startShimmer();
        mAdapter = new MovieAdapter(this);
        rv_movies = findViewById(R.id.rv_movies);
        rv_movies.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        rv_movies.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_movies.setItemAnimator(new DefaultItemAnimator());
        rv_movies.setAdapter(mAdapter);
        loadFromSharedPrefs();
    }

    private void loadFromSharedPrefs() {
        //noInternet.setVisibility(View.GONE);

        int loadingIdentifier = ProjectUtils.SharedPreferenceHelper.contains(MainActivity.this,ProjectUtils.PREF_FILTER) ? 2 : 1;

        switch (loadingIdentifier) {
            case 1:
                ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR);
                loadMovies(ProjectUtils.FILTER_POPULAR, loadingIdentifier);
                break;
            case 2:
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_TOP_RATED)) {
                    loadMovies(ProjectUtils.FILTER_TOP_RATED, 2);
                } else if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_POPULAR)) {
                    loadMovies(ProjectUtils.FILTER_POPULAR, 2);
                }
                break;
            default:
                break;
        }
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

        int filterIdentifier = ProjectUtils.SharedPreferenceHelper.contains(MainActivity.this,ProjectUtils.PREF_FILTER) ? 2 : 1;

        switch (filterIdentifier) {
            case 1:
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                break;
            case 2:
                if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_TOP_RATED)) {
                    imvTopRated.setVisibility(View.VISIBLE);
                    imvPopular.setVisibility(View.GONE);
                } else if (ProjectUtils.SharedPreferenceHelper.getSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, null).equals(ProjectUtils.FILTER_POPULAR)) {
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
                if (ProjectUtils.isNetworkAvailable(MainActivity.this)) {
                    rv_movies.setVisibility(View.GONE);
                    mShimmerLayout.setVisibility(View.VISIBLE);
                    mShimmerLayout.startShimmer();
                    ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_POPULAR);
                    imvPopular.setVisibility(View.VISIBLE);
                    imvTopRated.setVisibility(View.GONE);
                    loadMovies(ProjectUtils.FILTER_POPULAR, 1);
                }
                dialog.dismiss();
            }
        });

        txvTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProjectUtils.isNetworkAvailable(MainActivity.this)) {
                    rv_movies.setVisibility(View.GONE);
                    mShimmerLayout.setVisibility(View.VISIBLE);
                    mShimmerLayout.startShimmer();
                    ProjectUtils.SharedPreferenceHelper.setSharedPreferenceString(MainActivity.this,ProjectUtils.PREF_FILTER, ProjectUtils.FILTER_TOP_RATED);
                    imvPopular.setVisibility(View.GONE);
                    imvTopRated.setVisibility(View.VISIBLE);
                    loadMovies(ProjectUtils.FILTER_TOP_RATED, 2);
                }
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

    private void loadMovies(String filterPopular, int loadingIdentifier) {
        Retrofit retrofit = ProjectUtils.getClient();
        MovieApi client = retrofit.create(MovieApi.class);
        Call<MovieModel> call = client.getMoviesByPreference(filterPopular, ProjectUtils.API_KEY);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful()) {
                    mShimmerLayout.stopShimmer();
                    mShimmerLayout.setVisibility(View.GONE);
                    rv_movies.setVisibility(View.VISIBLE);
                }
                List<Result> res = response.body().getResults();
                mAdapter.addMoviesList(res);
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.d("Error", t.getMessage());
                mShimmerLayout.stopShimmer();
                mShimmerLayout.setVisibility(View.GONE);
            }

        });
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }
}
