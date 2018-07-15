package com.magarex.moviemania.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.magarex.moviemania.Interface.MovieApi;
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

//        if (criteria.equals(AppConstants.FAVOURITE_MOVIES)) {
//            return getAllFavouriteMovies();
//        }

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
//                .enqueue(new Callback<MovieResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
//                        result.setValue(response.body());
//                        saveInDatabase(response.body().getMovies(), filter);
//                        Log.d(TAG, "onResponse: "+response.body());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
//                        call.cancel();
//                        Log.v(MovieRepository.class.getName(), "error: " + t.getMessage());
//                    }
//                });
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

//    public LiveData<Integer> isFavourite(final int movieId) {
//        return Global.getDbInstance().movieDao().isFavourite(movieId);
//    }

//    public void refreshFavouriteMovies(final FavouriteMovie favouriteMovie, boolean isFavourite) {
//        if (isFavourite) {
//            AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    Global.getDbInstance().movieDao().deleteFavouriteMovie(favouriteMovie.getMovieId());
//                }
//            });
//            return;
//        }
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                Global.getDbInstance().movieDao().insertFavouriteMovie(favouriteMovie);
//            }
//        });
//    }

//    private LiveData<MovieResult> getAllFavouriteMovies() {
//        final MediatorLiveData<MovieResult> mediatorLiveData = new MediatorLiveData<>();
//        final LiveData<List<FavouriteMovie>> moviesLiveData = Global.getDbInstance().movieDao().getFavouriteMovies();
//
//        mediatorLiveData.addSource(moviesLiveData, new Observer<List<FavouriteMovie>>() {
//            @Override
//            public void onChanged(@Nullable List<FavouriteMovie> favouriteMovies) {
//                mediatorLiveData.removeSource(moviesLiveData);
//                if (favouriteMovies != null && !favouriteMovies.isEmpty()) {
//                    Gson gson = new Gson();
//                    String json = gson.toJson(favouriteMovies);
//                    List<Movie> movies = gson.fromJson(json, new TypeToken<List<Movie>>() {
//                    }.getType());
//                    mediatorLiveData.setValue(new MovieResult(movies));
//                } else {
//                    mediatorLiveData.setValue(null);
//                }
//            }
//        });
//        return mediatorLiveData;
//    }
}
