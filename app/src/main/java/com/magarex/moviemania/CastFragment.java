package com.magarex.moviemania;

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

import com.magarex.moviemania.Adapter.CastAdapter;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.CastResponse;
import com.magarex.moviemania.Utils.GridSpacingItemDecoration;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.databinding.TabBottomSheetCastBinding;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.magarex.moviemania.Utils.ProjectUtils.dpToPx;

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
        mAdapter = new CastAdapter(getActivity());
        RecyclerView rv_casts = mBinding.rvCasts;
        rv_casts.setAdapter(mAdapter);
        rv_casts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_casts.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        rv_casts.setItemAnimator(new DefaultItemAnimator());

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
