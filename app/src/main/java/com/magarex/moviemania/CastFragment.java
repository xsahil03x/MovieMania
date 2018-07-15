package com.magarex.moviemania;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.Adapter.CastAdapter;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.CastResponse;
import com.magarex.moviemania.Utils.GridSpacingItemDecoration;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.databinding.TabBottomSheetCastBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.magarex.moviemania.Utils.ProjectUtils.dpToPx;

public class CastFragment extends Fragment {

    private static final String MOVIE_ID_KEY = "movie_id";
    private CastAdapter mAdapter;
    private int mMovieId;

    public static CastFragment newInstance(int id) {
        CastFragment castFragment = new CastFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_KEY, id);
        castFragment.setArguments(bundle);
        return castFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovieId = bundle.getInt(MOVIE_ID_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TabBottomSheetCastBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.tab_bottom_sheet_cast, container, false);

        mAdapter = new CastAdapter(getActivity());
        RecyclerView rv_casts = mBinding.rvCasts;
        rv_casts.setAdapter(mAdapter);
        rv_casts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_casts.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_casts.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = ProjectUtils.getClient();
        MovieApi client = retrofit.create(MovieApi.class);
        client.getCast(mMovieId, ProjectUtils.API_KEY).enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                mAdapter.addCastToList(response.body().getCast());
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {

            }
        });

        return mBinding.getRoot();
    }

//    private void loadMovies(String movieId) {
//        MovieViewModelFactory mFactory = new MovieViewModelFactory(filter, ProjectUtils.API_KEY);
//        MovieViewModel mViewModel = ViewModelProviders.of(this, mFactory).get(MovieViewModel.class);
//
//        mViewModel.getResults().observe(this, movieModel -> {
//            if (movieModel != null) {
////                mShimmerLayout.stopShimmer();
////                mShimmerLayout.setVisibility(View.GONE);
////                rv_movies.setVisibility(View.VISIBLE);
//                mAdapter.addMoviesList(movieModel.getMovies());
//            } else {
//                mAdapter.addMoviesList(null);
//                //mMainBinding.noConnectionLl.setVisibility(View.VISIBLE);
//            }
//        });
//    }

}
