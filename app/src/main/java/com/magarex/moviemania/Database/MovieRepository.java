package com.magarex.moviemania.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.magarex.moviemania.Interface.MovieApi;
import com.magarex.moviemania.Models.MovieModel;
import com.magarex.moviemania.Models.Result;
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

    public LiveData<MovieModel> getMovies(final String filter, String apiKey) {
        final MutableLiveData<MovieModel> result = new MutableLiveData<>();

//        if (criteria.equals(AppConstants.FAVOURITE_MOVIES)) {
//            return getAllFavouriteMovies();
//        }

//        if (!ProjectUtils.isNetworkAvailable()) {
//            return getAllMoviesFromDB(filter);
//        }

        ProjectUtils.getClient().create(MovieApi.class)
                .getMoviesByPreference(filter, apiKey)
                .enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                        result.setValue(response.body());
                        saveInDatabase(response.body().getResults(), filter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                        call.cancel();
                        Log.v(MovieRepository.class.getName(), "error: " + t.getMessage());
                    }
                });
        return result;
    }

    private LiveData<MovieModel> getAllMoviesFromDB(String filter) {
        final MediatorLiveData<MovieModel> mediatorLiveData = new MediatorLiveData<>();
        final LiveData<List<Result>> moviesLiveData = ProjectUtils.getDbInstance().movieDao().getMoviesByCriteria(filter);

        mediatorLiveData.addSource(moviesLiveData, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> movies) {
                mediatorLiveData.removeSource(moviesLiveData);
                if (movies != null && !movies.isEmpty()) {
                    mediatorLiveData.setValue(new MovieModel(movies));
                } else {
                    mediatorLiveData.setValue(null);
                }
            }
        });
        return mediatorLiveData;
    }

    private void saveInDatabase(final List<Result> movies, final String sortCriteria) {
        for (Result movie : movies) {
            movie.setCriteria(sortCriteria);
        }
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProjectUtils.getDbInstance().movieDao().deleteMoviesByCriteria(sortCriteria);
                ProjectUtils.getDbInstance().movieDao().insertMoviesToDb(movies);
            }
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
