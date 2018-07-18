package com.magarex.moviemania.Interface;

import com.magarex.moviemania.Models.CastResponse;
import com.magarex.moviemania.Models.MovieResponse;
import com.magarex.moviemania.Models.Person;
import com.magarex.moviemania.Models.ReviewResponse;
import com.magarex.moviemania.Models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/{filter}")
    Call<MovieResponse> getMoviesByPreference(@Path("filter") String filter, @Query("api_key") String apiKey);

    @GET("movie/{movieId}/credits")
    Call<CastResponse> getCasts(@Path(value = "movieId", encoded = true) int movieId,
                                @Query("api_key") String apiKey);

    @GET("movie/{movieId}/videos")
    Call<TrailerResponse> getTrailers(@Path(value = "movieId", encoded = true) int movieId,
                                      @Query("api_key") String apiKey);

    @GET("person/{personId}")
    Call<Person> getPerson(@Path(value = "personId", encoded = true) int personId,
                           @Query("api_key") String apiKey);

    @GET("movie/{movieId}/reviews")
    Call<ReviewResponse> getReviews(@Path(value = "movieId", encoded = true) int movieId,
                                    @Query("api_key") String apiKey);

}