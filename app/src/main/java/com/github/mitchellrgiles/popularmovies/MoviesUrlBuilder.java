package com.github.mitchellrgiles.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

public class MoviesUrlBuilder {



    public static URL moviesListUrlBuilder(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortByString = sharedPreferences.getString(context.getResources().getString(R.string.sort_by),
                context.getResources().getString(R.string.popular));
        URL movieListUrl = null;
        Uri.Builder movieListUri = new Uri.Builder();
        movieListUri.scheme(context.getResources().getString(R.string.url_scheme))
                .encodedAuthority(context.getResources().getString(R.string.movie_url_base))
                .appendPath(sortByString)
                .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_value))
                .appendQueryParameter(context.getResources().getString(R.string.language), context.getResources().getString(R.string.language_us));

        try {
            movieListUrl = new URL(movieListUri.toString());
            Log.d("Test", "moviesListUrlBuilder: " + movieListUrl);
        } catch (IOException e) {
            Log.e("test", "moviesListUrlBuilder: ", e);
        }
        return  movieListUrl;
    }

    public static URL movieDetailUrlBuilder(Context context, String movieId) {
        URL movieUrl = null;
        Uri.Builder movieUri = new Uri.Builder();
        movieUri.scheme(context.getResources().getString(R.string.url_scheme))
                .encodedAuthority(context.getResources().getString(R.string.movie_url_base))
                .appendPath(movieId)
                .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_value));

        try {
            movieUrl = new URL(movieUri.toString());
            Log.d("Test", "movieDetailUrlBuilder: " + movieUrl);
        } catch (IOException e) {
            Log.e("Test", "movieDetailUrlBuilder: ", e);
        }

        return movieUrl;
    }

    public static URL movieTrailersUrlBuilder(Context context, String movieId) {
        URL movieUrl = null;
        Uri.Builder movieUri = new Uri.Builder();
        movieUri.scheme(context.getResources().getString(R.string.url_scheme))
                .encodedAuthority(context.getResources().getString(R.string.movie_url_base))
                .appendPath(context.getResources().getString(R.string.trailers))
                .appendPath(movieId)
                .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_value));

        try {
            movieUrl = new URL(movieUri.toString());
            Log.d("Test", "movieDetailUrlBuilder: " + movieUrl);
        } catch (IOException e) {
            Log.e("Test", "movieDetailUrlBuilder: ", e);
        }

        return movieUrl;
    }

    public static String youtubeVideoUrlBuilder(String youtubeVideoId) {
        return "https://www.youtube.com/watch?v=" + youtubeVideoId;

    }

    public static String youtubeThumnailUrlBuilder(String youtubeVideoId) {
        return "https://img.youtube.com/vi/n" + youtubeVideoId
                + "/0.jpg";
    }

    public static URL movieReviewsUrlBuilder(Context context, String movieId) {
        URL movieUrl = null;
        Uri.Builder movieUri = new Uri.Builder();
        movieUri.scheme(context.getResources().getString(R.string.url_scheme))
                .encodedAuthority(context.getResources().getString(R.string.movie_url_base))
                .appendPath(movieId)
                .appendPath(context.getResources().getString(R.string.reviews))
                .appendQueryParameter(context.getResources().getString(R.string.api_key), context.getResources().getString(R.string.api_key_value));

        try {
            movieUrl = new URL(movieUri.toString());
            Log.d("Test", "movieDetailUrlBuilder: " + movieUrl);
        } catch (IOException e) {
            Log.e("Test", "movieDetailUrlBuilder: ", e);
        }

        return movieUrl;
    }

    public static String moviePosterUrlBuilder(Context context, String moviePosterPath, String imageSize) {
        return context.getResources().getString(R.string.url_scheme) + "://"
                + context.getResources().getString(R.string.image_url_base) + imageSize + moviePosterPath;
    }

}
