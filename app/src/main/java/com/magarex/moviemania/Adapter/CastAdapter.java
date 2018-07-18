package com.magarex.moviemania.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.CastDetailActivity;
import com.magarex.moviemania.DetailActivity;
import com.magarex.moviemania.Interface.ItemClickListener;
import com.magarex.moviemania.Models.Cast;
import com.magarex.moviemania.R;
import com.magarex.moviemania.databinding.CastItemBinding;

import java.util.List;

class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    CastItemBinding mBinding;
    private ItemClickListener itemClickListener;

    CastViewHolder(CastItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.mBinding = itemBinding;
        mBinding.getRoot().setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

public class CastAdapter extends RecyclerView.Adapter<CastViewHolder> {

    private Activity mActivity;
    private List<Cast> mCasts;

    public CastAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void addCastToList(List<Cast> castList) {
        this.mCasts = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CastItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cast_item, parent, false);
        return new CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastViewHolder holder, int position) {
        holder.mBinding.setCast(mCasts.get(position));
        holder.setItemClickListener((view, position1, isLongClick) -> {
            Intent intent = new Intent(mActivity, CastDetailActivity.class);
            intent.putExtra("castId", mCasts.get(position).getId());

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(holder.mBinding.imgCastImage, "castTransition");
            pairs[1] = new Pair<View, String>(holder.mBinding.txtCastName, "castNameTransition");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, pairs);
                mActivity.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCasts == null) {
            return 0;
        } else {
            return mCasts.size();
        }
    }
}
