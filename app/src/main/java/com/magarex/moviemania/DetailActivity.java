package com.magarex.moviemania;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.magarex.moviemania.Models.Result;
import com.magarex.moviemania.Utils.GlideApp;
import com.magarex.moviemania.Utils.ProjectUtils;

public class DetailActivity extends AppCompatActivity {

    private TextView tv_rating,tv_release,tv_synopsis,tv_movie_name;
    private KenBurnsView imv_backdrop;
    private ImageView imv_poster;
    private RatingBar movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imv_backdrop = findViewById(R.id.img_movie_backdrop);
        imv_poster = findViewById(R.id.img_movie_poster);
        tv_rating =findViewById(R.id.txt_movie_rating);
        tv_release = findViewById(R.id.txt_movie_release);
        tv_movie_name = findViewById(R.id.txt_movie_name);
        tv_synopsis = findViewById(R.id.txt_movie_synopsis);
        movieRating = findViewById(R.id.movieRatingBar);

        Result movie = (Result) getIntent().getExtras().getSerializable("data");
        if (movie != null) {
            GlideApp.with(this)
                    .load(ProjectUtils.BACKDROP_BASE_URL+movie.getBackdropPath())
                    .into(imv_backdrop);

            GlideApp.with(this)
                    .load(ProjectUtils.POSTER_BASE_URL+movie.getPosterPath())
                    .into(imv_poster);
            tv_movie_name.setText(movie.getTitle());
            tv_rating.setText(Double.toString(movie.getVoteAverage()));
            tv_release.setText(movie.getReleaseDate());
            tv_synopsis.setText(movie.getOverview());
            movieRating.setRating((Float.parseFloat(Double.toString(movie.getVoteAverage())))/2);
        }

    }
}
