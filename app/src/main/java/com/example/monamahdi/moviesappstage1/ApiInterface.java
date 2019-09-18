package com.example.monamahdi.moviesappstage1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mona.mahdi on 3/11/2018.
 */

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<DiscoverMoviesResponse> getTopRated (@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<DiscoverMoviesResponse> getMostPopular (@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call <MovieTrailers> getVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviews> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

}
