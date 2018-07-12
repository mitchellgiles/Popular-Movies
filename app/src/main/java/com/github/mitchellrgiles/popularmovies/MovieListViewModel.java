package com.github.mitchellrgiles.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieListViewModel extends ViewModel {

    private LiveData<List<Movie>> favoriteMovies;
    private MutableLiveData<ArrayList<Movie>> movieList = new MutableLiveData<>();
    private AppDatabase database;
    private URL queryUrl;


    public MovieListViewModel(AppDatabase database) {
        this.database = database;
    }

    public void setQueryUrl(URL url) {
        this.queryUrl = url;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        favoriteMovies = database.movieDao().loadFavoriteMovies();
        return favoriteMovies;
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        MovieListTask movieListTask = new MovieListTask();
        movieListTask.execute(queryUrl);
        return movieList;
    }

    public class MovieListTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
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
        protected void onPostExecute(ArrayList<Movie> movies) {
            movieList.setValue(movies);
        }
    }


}
