package com.magarex.moviemania;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.databinding.TabBottomSheetReviewBinding;

public class ReviewFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabBottomSheetReviewBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.tab_bottom_sheet_review, container, false);


        return mBinding.getRoot();
    }

}

