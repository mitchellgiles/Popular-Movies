package com.github.mitchellrgiles.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;



public class MoviesDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    TextView movieTitle;
    TextView movieReleaseDate;
    TextView movieRating;
    ImageView moviePoster;
    TextView movieOverview;
    ProgressBar movieDetailPb;
    TextView movieDetailErrorMessage;
    FloatingActionButton addToFavorites;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private AppDatabase db;
    private String movieId;
    private String moviePosterUrl;
    private String movieTitleString;
    private Boolean inDatabase = false;
    private Movie currentMovie;

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
        addToFavorites = findViewById(R.id.add_to_favorites);

        Context context = this;

        db = AppDatabase.getInstance(context);

        Intent intent = getIntent();
        movieId = intent.getStringExtra(getResources().getString(R.string.intent_extra));

        trailerRecyclerView = findViewById(R.id.trailers);

        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);

        trailerAdapter = new TrailerAdapter(context, this);

        trailerRecyclerView.setAdapter(trailerAdapter);

        reviewRecyclerView = findViewById(R.id.reviews);

        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);

        reviewAdapter = new ReviewAdapter(context, this);

        reviewRecyclerView.setAdapter(reviewAdapter);

        URL movieUrl = MoviesUrlBuilder.movieDetailUrlBuilder(this, movieId);
        URL trailerUrl = MoviesUrlBuilder.movieTrailersUrlBuilder(this, movieId);
        URL reviewUrl = MoviesUrlBuilder.movieReviewsUrlBuilder(this, movieId);

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                showErrorMessage(true);
            } else if (connectivityManager.getActiveNetworkInfo().isAvailable()) {
                MovieDetailTask task = new MovieDetailTask();
                task.execute(movieUrl);

                TrailerTask trailerTask = new TrailerTask();
                trailerTask.execute(trailerUrl);

                ReviewTask reviewTask = new ReviewTask();
                reviewTask.execute(reviewUrl);
            }
        } else {
            showErrorMessage(true);
        }

        addToFavorites.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (inDatabase) {
                            db.movieDao().deleteMovie(currentMovie);
                            inDatabase = false;
                            addToFavorites.setImageDrawable(getResources().getDrawable(R.drawable.round_thumb_up_white_18dp));
                        } else {
                            db.movieDao().insertMovie(currentMovie);
                            inDatabase = true;
                            addToFavorites.setImageDrawable(getResources().getDrawable(R.drawable.round_thumb_down_white_18dp));
                        }
                    }
                });
            }
        });

    }

    //On Click for the trailers
    @Override
    public void onClick(String trailerUrl, String movieKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(trailerUrl));
        try {
            MoviesDetailActivity.this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            MoviesDetailActivity.this.startActivity(webIntent);
        }
    }

    @Override
    public void onClick() {
        //On Click for the reviews. Not yet implemented
    }

    public void showErrorMessage(Boolean hideOrShow) {
        if (hideOrShow) {
            movieDetailErrorMessage.setVisibility(View.VISIBLE);
        } else {
            movieDetailErrorMessage.setVisibility(View.INVISIBLE);
        }
    }

    public void populateMovieData(Movie movie) {
        movieTitleString = movie.getMovieTitle();
        movieTitle.setText(movieTitleString);
        movieReleaseDate.setText(movie.getReleaseDate());
        String rating = movie.getAverageVote() + getResources().getString(R.string.ratings);
        movieRating.setText(rating);

        moviePosterUrl = MoviesUrlBuilder.moviePosterUrlBuilder(this, movie.getPosterPath(), "w500");
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
            currentMovie = new Movie(movieTitleString, moviePosterUrl, movieId);
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final String checkMovie = db.movieDao().checkFavoriteMovies(movieId);
                    if (checkMovie == null) {
                    } else {
                        if (checkMovie.equals(currentMovie.getMovieTitle())) {
                            inDatabase = true;
                            addToFavorites.setImageDrawable(getResources().getDrawable(R.drawable.round_thumb_down_white_18dp));
                        }

                    }

                }
            });
        }
    }

    public class TrailerTask extends AsyncTask<URL, Void, List<Trailers>> {
        @Override
        protected List<Trailers> doInBackground(URL... urls) {
            String jsonTrailerListResponse;
            if(urls.length == 0) return null;

            try {
                URL trailersListUrl = urls[0];

                jsonTrailerListResponse = NetworkUtils.getResponseFromHttpsUrl(trailersListUrl);
                return MovieJSONUtils.getTrailersFromJson(jsonTrailerListResponse);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trailers> trailers) {
            if(trailers != null) {
                Log.d("TEST", "onPostExecute: TrailerTask");
                trailerAdapter.setTrailersList(trailers);


            }
        }
    }

    public class ReviewTask extends AsyncTask<URL, Void, List<Reviews>> {
        @Override
        protected List<Reviews> doInBackground(URL... urls) {
            String jsonReviewListResponse;
            if(urls.length == 0) return null;

            try {
                URL reviewListUrl = urls[0];

                jsonReviewListResponse = NetworkUtils.getResponseFromHttpsUrl(reviewListUrl);
                return MovieJSONUtils.getReviewsFromJson(jsonReviewListResponse);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Reviews> reviews) {
            if(reviews != null) {
                Log.d("TEST!@@!@!", "onPostExecute: ReviewTask");
                reviewAdapter.setReviewsList(reviews);
            }
        }
    }


}
