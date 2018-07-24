package com.magarex.moviemania.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.magarex.moviemania.database.MovieRepository;
import com.magarex.moviemania.models.MovieResponse;

public class MovieViewModel extends ViewModel {
    private LiveData<MovieResponse> results;

    MovieViewModel(String filter, String apiKey) {
        if (results != null) {
            return;
        }
        loadFromNetwork(filter, apiKey);
    }

    public void loadFromNetwork(String filter, String apiKey) {
        results = MovieRepository.getInstance().getMovies(filter, apiKey);
    }

    public LiveData<MovieResponse> getResults() {
        return results;
    }

}
