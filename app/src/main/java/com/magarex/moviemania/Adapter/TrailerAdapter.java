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

class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    TrailerItemBinding mBinding;
    private ItemClickListener itemClickListener;

    TrailerViewHolder(TrailerItemBinding itemBinding) {
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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private Activity mActivity;
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
        return new TrailerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {
        holder.mBinding.setTrailer(mTrailer.get(position));
        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (!mTrailer.isEmpty() && mTrailer.get(position).getSite().equals("YouTube")) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_URL + mTrailer.get(position).getKey()));
                mActivity.startActivity(intent);
            }
        });
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