package com.github.mitchellrgiles.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class MoviesDetailActivity extends AppCompatActivity {

    TextView movieTitle;
    TextView movieReleaseDate;
    TextView movieRating;
    ImageView moviePoster;
    TextView movieOverview;
    ProgressBar movieDetailPb;
    TextView movieDetailErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        movieTitle = findViewById(R.id.movie_detail_title);
        movieReleaseDate = findViewById(R.id.movie_detail_release_date);
        movieRating = findViewById(R.id.movie_detail_rating);
        moviePoster = findViewById(R.id.movie_detail_poster);
        movieOverview = findViewById(R.id.movie_detail_synopsis);
        movieDetailPb = findViewById(R.id.movie_detail_pb);
        movieDetailErrorMessage = findViewById(R.id.movie_detail_error_message);


        Intent intent = getIntent();
        String movieId = intent.getStringExtra(getResources().getString(R.string.intent_extra));

        URL movieUrl = MoviesUrlBuilder.movieDetailUrlBuilder(this, movieId);
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo().isAvailable()) {
            MovieDetailTask task = new MovieDetailTask();
            task.execute(movieUrl);
        } else {
            showErrorMessage(true);
        }
    }

    public void showErrorMessage(Boolean hideOrShow) {
        if (hideOrShow) {
            movieDetailErrorMessage.setVisibility(View.VISIBLE);
        } else {
            movieDetailErrorMessage.setVisibility(View.INVISIBLE);
        }
    }

    public void populateMovieData(Movie movie) {
        movieTitle.setText(movie.getMovieTitle());
        movieReleaseDate.setText(movie.getReleaseDate());
        String rating = movie.getAverageVote() + getResources().getString(R.string.ratings);
        movieRating.setText(rating);

        String moviePosterUrl = MoviesUrlBuilder.moviePosterUrlBuilder(this, movie.getPosterPath(), "w500");
        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).load(moviePosterUrl).placeholder(R.mipmap.ic_launcher).into(moviePoster);

        movieOverview.setText(movie.getOverview());
    }

    public void showProgressBar(Boolean hideOrShow) {
        if (hideOrShow) {
            movieDetailPb.setVisibility(View.VISIBLE);
        } else {
            movieDetailPb.setVisibility(View.INVISIBLE);
        }
    }

    public class MovieDetailTask extends AsyncTask<URL, Void, Movie> {
        @Override
        protected Movie doInBackground(URL... urls) {

            if (urls.length == 0) {
                return null;
            }
            try {
                URL movieDetailUrl = urls[0];
                String jsonMovieDetailResponse = NetworkUtils.getResponseFromHttpsUrl(movieDetailUrl);
                return MovieJSONUtils.getDetailedMovie(jsonMovieDetailResponse);
            } catch (Exception e) {
                Log.e("TEST", "doInBackground: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movie) {
            showProgressBar(false);
            populateMovieData(movie);
        }
    }


}
