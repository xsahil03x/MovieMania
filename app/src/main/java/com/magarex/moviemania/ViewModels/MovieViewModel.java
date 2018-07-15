package com.magarex.moviemania.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.magarex.moviemania.Database.MovieRepository;
import com.magarex.moviemania.Models.MovieResponse;

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
