package com.magarex.moviemania.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.FavouriteMovie;
import com.magarex.moviemania.Models.Movie;
import com.magarex.moviemania.Models.MovieResponse;
import com.magarex.moviemania.Utils.AppExecutors;
import com.magarex.moviemania.Utils.ProjectUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static MovieRepository mMovieRepository;

    public static MovieRepository getInstance() {
        if (mMovieRepository == null) {
            mMovieRepository = new MovieRepository();
        }
        return mMovieRepository;
    }

    public LiveData<MovieResponse> getMovies(final String filter, String apiKey) {
        final MutableLiveData<MovieResponse> result = new MutableLiveData<>();

        if (filter.equals(ProjectUtils.FILTER_FAVOURITE)) {
            return getAllFavouriteMovies();
        }

        if (!ProjectUtils.isNetworkAvailable()) {
            return getAllMoviesFromDB(filter);
        }

        ProjectUtils.getClient().create(MovieApi.class)
                .getMoviesByPreference(filter, apiKey)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        result.setValue(response.body());
                        saveInDatabase(response.body().getMovies(), filter);
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        call.cancel();
                        Log.v(MovieRepository.class.getName(), "error: " + t.getMessage());
                    }
                });
        return result;
    }

    private LiveData<MovieResponse> getAllMoviesFromDB(String filter) {
        final MediatorLiveData<MovieResponse> mediatorLiveData = new MediatorLiveData<>();
        final LiveData<List<Movie>> moviesLiveData = ProjectUtils.getDbInstance().movieDao().getMoviesByCriteria(filter);

        mediatorLiveData.addSource(moviesLiveData, movies -> {
            mediatorLiveData.removeSource(moviesLiveData);
            if (movies != null && !movies.isEmpty()) {
                mediatorLiveData.setValue(new MovieResponse(movies));
            } else {
                mediatorLiveData.setValue(null);
            }
        });
        return mediatorLiveData;
    }

    private void saveInDatabase(final List<Movie> movies, final String sortCriteria) {
        if (movies != null && !movies.isEmpty()) {
            for (Movie movie : movies) {
                movie.setCriteria(sortCriteria);
            }
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            ProjectUtils.getDbInstance().movieDao().deleteMoviesByCriteria(sortCriteria);
            ProjectUtils.getDbInstance().movieDao().insertMoviesToDb(movies);
        });
    }

    public LiveData<Integer> isFavourite(final int movieId) {
        return ProjectUtils.getDbInstance().movieDao().isFavourite(movieId);
    }

    public void refreshFavouriteMovies(final FavouriteMovie favouriteMovie, boolean isFavourite) {
        if (isFavourite) {
            AppExecutors.getInstance().diskIO().execute(() -> ProjectUtils.getDbInstance().movieDao().deleteFavouriteMovie(favouriteMovie.getMovieId()));
            return;
        }
        AppExecutors.getInstance().diskIO().execute(() -> ProjectUtils.getDbInstance().movieDao().insertFavouriteMovie(favouriteMovie));
    }

    private LiveData<MovieResponse> getAllFavouriteMovies() {
        final MediatorLiveData<MovieResponse> mediatorLiveData = new MediatorLiveData<>();
        final LiveData<List<FavouriteMovie>> moviesLiveData = ProjectUtils.getDbInstance().movieDao().getFavouriteMovies();

        mediatorLiveData.addSource(moviesLiveData, favouriteMovies -> {
            mediatorLiveData.removeSource(moviesLiveData);
            if (favouriteMovies != null && !favouriteMovies.isEmpty()) {
                Gson gson = new Gson();
                String json = gson.toJson(favouriteMovies);
                List<Movie> movies = gson.fromJson(json, new TypeToken<List<Movie>>() {
                }.getType());
                mediatorLiveData.setValue(new MovieResponse(movies));
            } else {
                mediatorLiveData.setValue(null);
            }
        });
        return mediatorLiveData;
    }
}
