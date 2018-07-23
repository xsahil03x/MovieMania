package com.magarex.moviemania;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.Adapter.ReviewAdapter;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.ReviewResponse;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.databinding.TabBottomSheetReviewBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class ReviewFragment extends BottomSheetDialogFragment {

    private ReviewAdapter mAdapter;
    private static final String MOVIE_ID_KEY = "movie_id";
    private int mMovieId;

    public static ReviewFragment newInstance(int id) {
        ReviewFragment reviewFragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_KEY, id);
        reviewFragment.setArguments(bundle);
        return reviewFragment;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabBottomSheetReviewBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.tab_bottom_sheet_review, container, false);
        setRetainInstance(true);

        mAdapter = new ReviewAdapter(getActivity());
        RecyclerView rv_Reviews = mBinding.rvReviews;
        rv_Reviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_Reviews.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        rv_Reviews.setItemAnimator(new DefaultItemAnimator());
        rv_Reviews.setAdapter(mAdapter);

        ProjectUtils.getClient()
                .create(MovieApi.class)
                .getReviews(mMovieId, ProjectUtils.API_KEY)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                        Log.d(TAG, "onResponse: " + response.message());
                        if(response.body().getTotalResults() > 0){
                        mAdapter.addReviewToList(response.body().getResults());}
                        else{
                            mBinding.rvReviews.setVisibility(View.GONE);
                            mBinding.noReviewGroup.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        return mBinding.getRoot();
    }
}


