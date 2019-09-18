package com.example.monamahdi.moviesappstage1;


public class TrailersAndReviews {

    private MovieReviews reviews ;
    private  MovieTrailers trailers ;

    public TrailersAndReviews(MovieReviews reviews, MovieTrailers trailers) {
        this.reviews = reviews;
        this.trailers = trailers;
    }

    public MovieReviews getReviews() {
        return reviews;
    }

    public void setReviews(MovieReviews reviews) {
        this.reviews = reviews;
    }

    public MovieTrailers getTrailers() {
        return trailers;
    }

    public void setTrailers(MovieTrailers trailers) {
        this.trailers = trailers;
    }
}
