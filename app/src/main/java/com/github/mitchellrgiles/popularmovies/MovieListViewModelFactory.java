package com.github.mitchellrgiles.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase database;


    public MovieListViewModelFactory(AppDatabase database) {

        this.database = database;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieListViewModel(database);
    }
}
