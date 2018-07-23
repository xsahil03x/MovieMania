package com.magarex.moviemania.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.magarex.moviemania.Models.FavouriteMovie;
import com.magarex.moviemania.Models.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies WHERE criteria = :criteria")
    LiveData<List<Movie>> getMoviesByCriteria(String criteria);

    @Query("DELETE FROM movies WHERE criteria = :criteria")
    void deleteMoviesByCriteria(String criteria);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMoviesToDb(List<Movie> movieList);

    @Query("SELECT * FROM favourites")
    LiveData<List<FavouriteMovie>> getFavouriteMovies();

    @Query("SELECT COUNT(movie_id) FROM favourites WHERE movie_id = :movieId")
    LiveData<Integer> isFavourite(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMovie(FavouriteMovie favouriteMovie);

    @Query("DELETE FROM favourites WHERE movie_id = :movieId")
    void deleteFavouriteMovie(int movieId);

}