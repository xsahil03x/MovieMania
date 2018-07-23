package com.magarex.moviemania;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.Person;
import com.magarex.moviemania.Utils.GlideApp;
import com.magarex.moviemania.Utils.ProjectUtils;
import com.magarex.moviemania.databinding.ActivityCastDetailBinding;

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
        String castDp = getIntent().getStringExtra("castDp");
        int id = getIntent().getIntExtra("castId", 0);
        if (id != 0) {
            GlideApp.with(this)
                    .load(ProjectUtils.POSTER_BASE_URL + castDp)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.movie_poster_placeholder)
                    .error(R.drawable.movie_poster_error)
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
