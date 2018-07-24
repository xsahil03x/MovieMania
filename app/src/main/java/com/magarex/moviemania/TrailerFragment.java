package com.magarex.moviemania;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.Adapter.TrailerAdapter;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.TrailerResponse;
import com.magarex.moviemania.Utils.GridSpacingItemDecoration;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.databinding.TabBottomSheetTrailerBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class TrailerFragment extends BottomSheetDialogFragment {

    private TrailerAdapter mAdapter;
    private static final String MOVIE_ID_KEY = "movie_id";
    private int mMovieId;

    public static TrailerFragment newInstance(int id) {
        TrailerFragment trailerFragment = new TrailerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_KEY, id);
        trailerFragment.setArguments(bundle);
        return trailerFragment;
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
        TabBottomSheetTrailerBinding mBinding = DataBindingUtil.inflate(inflater,
                R.layout.tab_bottom_sheet_trailer, container, false);

        mAdapter = new TrailerAdapter(getActivity());
        int recyclerViewSpanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
        RecyclerView rv_Trailers = mBinding.rvTrailers;
        rv_Trailers.setLayoutManager(new GridLayoutManager(getActivity(),recyclerViewSpanCount));
        rv_Trailers.addItemDecoration(
                new GridSpacingItemDecoration(recyclerViewSpanCount, ProjectUtils.dpToPx(), true));
        rv_Trailers.setItemAnimator(new DefaultItemAnimator());
        rv_Trailers.setAdapter(mAdapter);

        ProjectUtils.getClient()
                .create(MovieApi.class)
                .getTrailers(mMovieId, ProjectUtils.API_KEY)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailerResponse> call,
                                           @NonNull Response<TrailerResponse> response) {
                        Log.d(TAG, "onResponse: " + response.message());
                        mAdapter.addTrailerToList(response.body().getResults());
                    }

                    @Override
                    public void onFailure(@NonNull Call<TrailerResponse> call,
                                          @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mBinding.getRoot();
    }
}
