package com.magarex.moviemania.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.magarex.moviemania.Database.MovieRepository;

public class DetailsViewModel extends ViewModel {
    private LiveData<Integer> favourite;

    DetailsViewModel(int movieId, String apiKey) {
        if (favourite == null) {
            favourite = MovieRepository.getInstance().isFavourite(movieId);
        }
    }

    public LiveData<Integer> getFavourite() {
        return favourite;
    }

}