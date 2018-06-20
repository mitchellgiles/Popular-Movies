package com.github.mitchellrgiles.popularmovies;

public class Movie {

    private String movieTitle;

    private String posterPath;

    private String overview;

    private String averageVote;

    private String releaseDate;

    private String movieId;

    public Movie(String movieTitle, String posterPath, String movieiId) {
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.movieId = movieiId;

    }

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
}
