package com.magarex.moviemania.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final String filter;
    private final String apiKey;

    public MovieViewModelFactory(String filter, String apiKey) {
        this.filter = filter;
        this.apiKey = apiKey;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(filter, apiKey);
    }

}
