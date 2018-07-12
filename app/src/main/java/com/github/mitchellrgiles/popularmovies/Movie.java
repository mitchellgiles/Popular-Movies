package com.github.mitchellrgiles.popularmovies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Movie {

    private String movieTitle;

    private String posterPath;

    private String overview;

    private String averageVote;

    private String releaseDate;
    @PrimaryKey @NonNull
    private String movieId;

    public Movie(String movieTitle, String posterPath,@NonNull String movieId) {
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.movieId = movieId;

    }
    @Ignore
    public Movie(String movieTitle, String posterPath, String overview, String averageVote, String releaseDate) {
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.averageVote = averageVote;
        this.releaseDate = releaseDate;
    }

    public String getMovieTitle() {
        return this.movieTitle;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public String getOverview() {
        return this.overview;
    }

    public String getAverageVote() {
        return this.averageVote;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public String getMovieId() {
        return this.movieId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setAverageVote(String averageVote) {
        this.averageVote = averageVote;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }
}
