package com.github.mitchellrgiles.popularmovies;

public class Reviews {
    private String reviewAuthor;

    private String reviewContent;

    public Reviews(String reviewAuthor, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    public String getReviewAuthor() {
        return this.reviewAuthor;
    }

    public String getReviewContent() {
        return this.reviewContent;
    }
}
