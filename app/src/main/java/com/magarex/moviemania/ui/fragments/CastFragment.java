package com.magarex.moviemania.ui.fragments;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.R;
import com.magarex.moviemania.adapter.CastAdapter;
import com.magarex.moviemania.interfaces.MovieApi;
import com.magarex.moviemania.models.CastResponse;
import com.magarex.moviemania.utils.GridSpacingItemDecoration;
import com.magarex.moviemania.utils.ProjectUtils;
import com.magarex.moviemania.databinding.TabBottomSheetCastBinding;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.magarex.moviemania.utils.ProjectUtils.dpToPx;

public class CastFragment extends BottomSheetDialogFragment {

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

        mBinding.shimmerCastLoading.startShimmer();
        int recyclerViewSpanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        mAdapter = new CastAdapter(getActivity());
        RecyclerView rv_casts = mBinding.rvCasts;
        rv_casts.setLayoutManager(new GridLayoutManager(getActivity(), recyclerViewSpanCount));
        rv_casts.addItemDecoration(new GridSpacingItemDecoration(recyclerViewSpanCount, dpToPx(), true));
        rv_casts.setItemAnimator(new DefaultItemAnimator());
        rv_casts.setAdapter(mAdapter);

        ProjectUtils.getClient()
                .create(MovieApi.class)
                .getCasts(mMovieId, ProjectUtils.API_KEY)
                .enqueue(new Callback<CastResponse>() {
                    @Override
                    public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                        Log.d(TAG, "onResponse: "+response.message());
                        mBinding.shimmerCastLoading.stopShimmer();
                        mBinding.shimmerCastLoading.setVisibility(View.GONE);
                        mAdapter.addCastToList(response.body().getCast());
                        mBinding.rvCasts.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<CastResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        mBinding.shimmerCastLoading.stopShimmer();
                        mBinding.shimmerCastLoading.setVisibility(View.GONE);
                    }
                });

        return mBinding.getRoot();
    }
}
