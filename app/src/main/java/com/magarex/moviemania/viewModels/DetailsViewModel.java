package com.magarex.moviemania.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.magarex.moviemania.database.MovieRepository;

public class DetailsViewModel extends ViewModel {
    private LiveData<Integer> favourite;

    DetailsViewModel(int movieId) {
        if (favourite == null) {
            favourite = MovieRepository.getInstance().isFavourite(movieId);
        }
    }

    public LiveData<Integer> getFavourite() {
        return favourite;
    }

}