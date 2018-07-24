package com.magarex.moviemania.ui.activities;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.magarex.moviemania.R;
import com.magarex.moviemania.databinding.ActivityCastDetailBinding;
import com.magarex.moviemania.interfaces.MovieApi;
import com.magarex.moviemania.models.Person;
import com.magarex.moviemania.utils.GlideApp;
import com.magarex.moviemania.utils.ProjectUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class CastDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCastDetailBinding mBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_cast_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        String castDp = getIntent().getStringExtra("castDp");
        int id = getIntent().getIntExtra("castId", 0);
        if (id != 0) {
            GlideApp.with(this)
                    .load(ProjectUtils.POSTER_BASE_URL + castDp)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.movie_poster_placeholder)
                    .error(R.drawable.movie_poster_error)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(mBinding.imgPersonImage);

            ProjectUtils.getClient()
                    .create(MovieApi.class)
                    .getPerson(id, ProjectUtils.API_KEY)
                    .enqueue(new Callback<Person>() {
                        @Override
                        public void onResponse(Call<Person> call, Response<Person> response) {
                            Log.d(TAG, "onResponse: " + response.message());
                            mBinding.setPerson(response.body());
                        }

                        @Override
                        public void onFailure(Call<Person> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
        }
    }
}
