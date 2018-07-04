package com.magarex.moviemania.Interface;



import com.magarex.moviemania.Models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MovieApi {

    @GET("movie/{filter}")
    Call<MovieModel> getMoviesByPreference(@Path("filter") String filter, @Query("api_key") String apiKey);

}