package com.github.mitchellrgiles.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView errorMessageTextView;
    private AppDatabase db;
    private MovieListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        recyclerView = findViewById(R.id.recyclerview_movies);
        progressBar = findViewById(R.id.pb_loading_indicator_movie_list);
        errorMessageTextView = findViewById(R.id.tv_error_message_display_movie_list);

        db = AppDatabase.getInstance(getApplicationContext());

        MovieListViewModelFactory factory = new MovieListViewModelFactory(db);
        this.viewModel = ViewModelProviders.of(this, factory).get(MovieListViewModel.class);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(getApplicationContext(),this);

        recyclerView.setAdapter(movieAdapter);

        startTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getResources().getString(R.string.sort_by),
                        getResources().getString(R.string.popular)).apply();
                startTask();
                break;
            case R.id.action_top_rated:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getResources().getString(R.string.sort_by),
                        getResources().getString(R.string.top_rated)).apply();
                startTask();
                break;
            case R.id.action_favorite:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getResources().getString(R.string.sort_by), getResources().getString(R.string.favorite)).apply();
                viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(@Nullable List<Movie> movies) {
                        movieAdapter.setMovie(movies);
                    }
                });


        }
        return true;
    }

    @Override
    public void onClick(String movieId) {
        Intent intent = new Intent(MoviesListActivity.this, MoviesDetailActivity.class);
        intent.putExtra(getResources().getString(R.string.intent_extra), movieId);
        startActivity(intent);
    }

    private void showMovieList() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void startTask() {
        final URL movieListUrl =  MoviesUrlBuilder.moviesListUrlBuilder(this);
        viewModel.setQueryUrl(movieListUrl);
        Boolean isFavorites = false;
        if (movieListUrl.toString().contains("favorite")) {
            isFavorites = true;
        }

        Context context = this;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                showErrorMessage();
            } else if (connectivityManager.getActiveNetworkInfo().isAvailable()) {

                if (isFavorites) {
                    viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
                        @Override
                        public void onChanged(@Nullable List<Movie> movies) {
                            movieAdapter.setMovie(movies);
                        }
                    });
                } else {
                    viewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
                        @Override
                        public void onChanged(@Nullable ArrayList<Movie> movies) {
                            movieAdapter.setMovie(movies);
                        }
                    });
                }
            }
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);

    }

}
