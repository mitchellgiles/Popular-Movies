package com.github.mitchellrgiles.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieJSONUtils {

    public static List<Movie> getMovieListFromJson(String movieJsonString) throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        JSONObject movieListJsonObject = new JSONObject(movieJsonString);
        JSONArray movieListJsonArray = movieListJsonObject.getJSONArray("results");

        for(int i = 0; i < movieListJsonArray.length(); i++) {
            JSONObject movieJsonObject = movieListJsonArray.getJSONObject(i);

            String movieTitle = movieJsonObject.getString("title");
            String moviePosterPath = movieJsonObject.getString("poster_path");
            String movieId = movieJsonObject.getString("id");

            movieList.add(new Movie(movieTitle, moviePosterPath, movieId));
        }

        return movieList;
    }

    public static Movie getDetailedMovie(String movieJsonString) throws JSONException {

        JSONObject movieJsonObject = new JSONObject(movieJsonString);
        String movieTitle = movieJsonObject.getString("title");
        String moviePosterPath = movieJsonObject.getString("poster_path");
        Log.d("TEST", "getDetailedMovie: " + moviePosterPath);
        String movieOverview = movieJsonObject.getString("overview");
        String movieAverageVote = movieJsonObject.getString("vote_average");
        String movieReleaseDate = movieJsonObject.getString("release_date");

        return new Movie(movieTitle, moviePosterPath, movieOverview, movieAverageVote, movieReleaseDate);
    }
}
