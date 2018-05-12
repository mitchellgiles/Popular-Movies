package com.github.mitchellrgiles.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        recyclerView = findViewById(R.id.recyclerview_movies);
        progressBar = findViewById(R.id.pb_loading_indicator_movie_list);
        errorMessageTextView = findViewById(R.id.tv_error_message_display_movie_list);

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
        URL movieListUrl =  MoviesUrlBuilder.moviesListUrlBuilder(this);

        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo().isAvailable()) {
            MovieListTask task = new MovieListTask();
            task.execute(movieListUrl);
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

    public class MovieListTask extends AsyncTask<URL, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected List<Movie> doInBackground(URL... urls) {
            Log.d("TEST", "doInBackground: was called");
            String jsonMovieListResponse;
            if (urls.length == 0) return null;

            try {
                URL movieListUrl = urls[0];

                jsonMovieListResponse = NetworkUtils.getResponseFromHttpsUrl(movieListUrl);
                return MovieJSONUtils.getMovieListFromJson(jsonMovieListResponse);

             } catch (Exception e) {
                Log.e("ERROR", "doInBackground: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                showMovieList();
                movieAdapter.setMovie(movies);
            } else {
                showErrorMessage();
            }
        }
    }
}
