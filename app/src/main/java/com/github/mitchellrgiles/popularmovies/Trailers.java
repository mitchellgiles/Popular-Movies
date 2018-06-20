package com.github.mitchellrgiles.popularmovies;

public class Trailers {

    private String movieKey;

    private String trailerName;

    private String thumbnailUrl;

    private String videoUrl;

    public Trailers(String movieKey, String trailerName) {
        this.movieKey = movieKey;
        this.trailerName = trailerName;

        this.thumbnailUrl = MoviesUrlBuilder.youtubeThumnailUrlBuilder(movieKey);
        this.videoUrl = MoviesUrlBuilder.youtubeVideoUrlBuilder(movieKey);
    }

    public String getTrailerName() {
        return this.trailerName;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }
}
