package com.magarex.moviemania.Interface;

import com.magarex.moviemania.Models.CastResponse;
import com.magarex.moviemania.Models.GenreResponse;
import com.magarex.moviemania.Models.MovieResponse;
import com.magarex.moviemania.Models.ReviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/{filter}")
    Call<MovieResponse> getMoviesByPreference(@Path("filter") String filter, @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(@Query("language") String language);

    @GET("movie/{movieId}/credits")
    Call<CastResponse> getCast(@Path(value = "movieId", encoded = true) int movieId,
                               @Query("api_key") String apiKey);

//    @GET("movie/{movieId}/videos")
//    Call<Video> getVideos(@Path(value = "movieId", encoded = true) int movieId,
//                                                   @Query("language") String language);

    @GET("movie/{movieId}/reviews")
    Call<ReviewResponse> getReviews(@Path(value = "movieId", encoded = true) int movieId,
                                    @Query("language") String language);

}