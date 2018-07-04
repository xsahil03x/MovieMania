package com.magarex.moviemania.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magarex.moviemania.DetailActivity;
import com.magarex.moviemania.Interface.ItemClickListener;
import com.magarex.moviemania.Models.MovieModel;
import com.magarex.moviemania.Models.Result;
import com.magarex.moviemania.R;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.Utils.GlideApp;

import java.util.List;

class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    CardView moviePosterCardView;
    ImageView moviePoster;
    TextView movieTitle;
    TextView movieReleaseDate;
    TextView movieRating;

    MovieViewHolder(View itemView) {
        super(itemView);
        moviePosterCardView = itemView.findViewById(R.id.cv_movie_item);
        moviePoster = itemView.findViewById(R.id.img_movie_poster);
        movieTitle = itemView.findViewById(R.id.txt_movie_title);
        movieReleaseDate = itemView.findViewById(R.id.txt_release_date);
        movieRating = itemView.findViewById(R.id.txt_movie_rating);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private Activity mActivity;
    private List<Result> mResults;

    public MovieAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void addMoviesList(List<Result> movieList) {
        this.mResults = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {

        GlideApp.with(mActivity)
                .load(ProjectUtils.POSTER_BASE_URL + mResults.get(position).getPosterPath())
                .placeholder(R.drawable.movie_poster_placeholder)
                .error(R.drawable.movie_poster_error)
                .into(holder.moviePoster);

        holder.movieTitle.setText(mResults.get(position).getTitle());
        holder.movieReleaseDate.setText(mResults.get(position).getReleaseDate());
        String rating = Double.toString(mResults.get(position).getVoteAverage());
        holder.movieRating.setText(rating);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                Bundle movieData = new Bundle();
                movieData.putSerializable("data",mResults.get(position));
                intent.putExtras(movieData);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(holder.moviePoster, "posterTransition");
                pairs[1] = new Pair<View, String>(holder.moviePosterCardView, "posterCardViewTransition");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, pairs);
                    mActivity.startActivity(intent, options.toBundle());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;
        } else {
            return mResults.size();
        }
    }
}
