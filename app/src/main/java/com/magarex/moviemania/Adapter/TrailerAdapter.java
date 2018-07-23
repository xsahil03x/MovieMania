package com.magarex.moviemania.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magarex.moviemania.Interface.ItemClickListener;
import com.magarex.moviemania.Models.Trailer;
import com.magarex.moviemania.R;
import com.magarex.moviemania.databinding.TrailerItemBinding;

import java.util.List;

import static com.magarex.moviemania.Utils.ProjectUtils.YOUTUBE_URL;

class TrailerViewHolder extends RecyclerView.ViewHolder {

    final TrailerItemBinding mBinding;

    TrailerViewHolder(TrailerItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.mBinding = itemBinding;
    }
}

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private final Activity mActivity;
    private List<Trailer> mTrailer;

    public TrailerAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void addTrailerToList(List<Trailer> trailerList) {
        this.mTrailer = trailerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrailerItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.trailer_item, parent, false);
        TrailerViewHolder holder = new TrailerViewHolder(binding);
        binding.getRoot().setOnClickListener(view -> {
            if (!mTrailer.isEmpty() && mTrailer.get(holder.getAdapterPosition()).getSite().equals("YouTube")) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_URL + mTrailer.get(holder.getAdapterPosition()).getKey()));
                mActivity.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {
        holder.mBinding.setTrailer(mTrailer.get(position));
    }

    @Override
    public int getItemCount() {
        if (mTrailer == null) {
            return 0;
        } else {
            return mTrailer.size();
        }
    }

}