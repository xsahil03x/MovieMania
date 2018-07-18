package com.magarex.moviemania.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private int movieId;
    private String apiKey;

    public DetailsViewModelFactory(int movieId, String apiKey) {
        this.movieId = movieId;
        this.apiKey = apiKey;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(movieId, apiKey);
    }
}
