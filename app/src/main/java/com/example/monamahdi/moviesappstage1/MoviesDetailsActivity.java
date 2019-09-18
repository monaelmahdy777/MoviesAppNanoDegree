package com.example.monamahdi.moviesappstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.monamahdi.moviesappstage1.providers.MovieContract;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesDetailsActivity extends AppCompatActivity implements TrailersAdapter.OnItemClicked {

    public static final String IMAGES_BASE_URL = "http://image.tmdb.org/t/p/";
    private TextView original_title,overview,vote_average,release_date,trailersTitle,reviewsTitle;
    private ImageView poster_path;
    private Result result;
    private Retrofit retrofit;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "719170d3b1619059bd135db28c268572";
    Button favBtn;
    private RecyclerView recyclerViewTrailers;
    private ArrayList<trailers> allTrailers;
    //reviews
    private RecyclerView recyclerViewReviews;
    private ArrayList<Reviews> allReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        original_title = (TextView)findViewById(R.id.original_title);
        overview = (TextView)findViewById(R.id.overview);
        vote_average = (TextView)findViewById(R.id.vote_average);
        release_date = (TextView)findViewById(R.id.release_date);
        trailersTitle = (TextView) findViewById(R.id.trailers);
        favBtn = (Button)findViewById(R.id.favBtn);
        recyclerViewTrailers = (RecyclerView) findViewById(R.id.RV_trailers);
        reviewsTitle = (TextView) findViewById(R.id.reviews);

        recyclerViewReviews = (RecyclerView) findViewById(R.id.RV_reviews);


        poster_path = (ImageView) findViewById(R.id.poster_path);

        String details = (String) getIntent().getExtras().get("details");
        Gson gson = new Gson();
        result = gson.fromJson(details, Result.class);

        drawFavourite();

        original_title.setText(result.getOriginalTitle());
        overview.setText(result.getOverview());
        vote_average.setText(result.getVoteAverage().toString());
        release_date.setText(result.getReleaseDate().substring(0, 4));
        String imagePath = result.getPosterPath();
        String fullPath = IMAGES_BASE_URL + "w185" + imagePath;
        Glide.with(getApplicationContext())
                .load(fullPath)
                .into(poster_path);
        int movieId = -1;
        if (result != null) {
            movieId = result.getId();

            getMovieData(movieId);
        }
    }

        private void getMovieData(final int movieId) {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface movieAPI = retrofit.create(ApiInterface.class);

        Call<MovieTrailers> call = movieAPI.getVideos(movieId, API_KEY);

        call.enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(Call<MovieTrailers> call, Response<MovieTrailers> response) {

                final MovieTrailers trailers = response.body();

                Call<MovieReviews> ReviewsCall = movieAPI.getReviews(movieId, API_KEY);
                ReviewsCall.enqueue(new Callback<MovieReviews>() {
                    @Override
                    public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {

                        MovieReviews reviews = response.body();

                        drawData(new TrailersAndReviews(reviews, trailers));
                    }

                    @Override
                    public void onFailure(Call<MovieReviews> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<MovieTrailers> call, Throwable t) {

            }
        });


    }

    private void drawData(TrailersAndReviews trailersAndReviews) {

        if (trailersAndReviews.getTrailers() != null) {
            if (trailersAndReviews.getTrailers().getResults() != null) {
                if (trailersAndReviews.getTrailers().getResults().size() > 0) {


                    trailersTitle.setVisibility(View.VISIBLE);
                    recyclerViewTrailers.setVisibility(View.VISIBLE);
                    allTrailers = (ArrayList<trailers>) trailersAndReviews.getTrailers().getResults();
                    TrailersAdapter mRecyclerViewTrailersAdapter =
                            new TrailersAdapter(this, allTrailers);
                    recyclerViewTrailers.setAdapter(mRecyclerViewTrailersAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewTrailers.setLayoutManager(mLayoutManager);
                    mRecyclerViewTrailersAdapter.setOnClick(this); // Bind the listener

                } else {
                    displayToast("NO Trailers Attached");
                }
            } else {
                displayToast("NO Trailers Attached");
            }
        } else {
            displayToast("NO Trailers Attached");
        }
        ///////////////


        if (trailersAndReviews.getReviews() != null) {
            if (trailersAndReviews.getReviews().getResults() != null) {
                if (trailersAndReviews.getReviews().getResults().size() > 0) {


                    reviewsTitle.setVisibility(View.VISIBLE);
                    recyclerViewReviews.setVisibility(View.VISIBLE);
                    allReviews = (ArrayList<Reviews>) trailersAndReviews.getReviews().getResults();
                    ReviewsAdapter mRecyclerViewReviewsAdapter =
                            new ReviewsAdapter(this, allReviews);
                    recyclerViewReviews.setAdapter(mRecyclerViewReviewsAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewReviews.setLayoutManager(mLayoutManager);

                } else {
                    displayToast("NO Reviews Attached");
                }
            } else {
                displayToast("NO Reviews Attached");
            }
        } else {
            displayToast("NO Reviews Attached");
        }


    }

    private void displayToast(String msg) {

    }

    @Override
    public void onItemClick(int position) {

        trailers selectedTrailer = allTrailers.get(position);

        if (selectedTrailer.getSite().equalsIgnoreCase("youtube")) {
            openYouTubeVideo(selectedTrailer.getKey());
        }
    }

    private void openYouTubeVideo(String videoID) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoID)));
    }

    public void favourite(View view) {

        boolean isExist = checkIfFavourite(result);

        if (!isExist) {
            addMovieToContentProvider(result);
        } else {
            deleteFavourite(result);
        }
    }


    public boolean checkIfFavourite(Result result) {
        boolean isExist = false;
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.column_id,
                null,
                null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(0));
            if (id == result.getId()) {
                isExist = true;
            }
        }
        return isExist;
    }

    public void drawFavourite() {

        boolean isExist = checkIfFavourite(result);

        if (isExist) {
            favBtn.setBackgroundColor(Color.parseColor("#555DBCD2"));
        } else {
            favBtn.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }
    }


    private void addMovieToContentProvider(Result movie) {

        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.column_id, movie.getId());
        values.put(MovieContract.MovieEntry.column_voteCount, movie.getVoteCount());
        values.put(MovieContract.MovieEntry.column_voteAverage, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.column_title, movie.getTitle());
        values.put(MovieContract.MovieEntry.column_popularity, movie.getPopularity());
        values.put(MovieContract.MovieEntry.column_posterPath, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.column_originalLanguage, movie.getOriginalLanguage());
        values.put(MovieContract.MovieEntry.column_originalTitle, movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.column_overview, movie.getOverview());
        values.put(MovieContract.MovieEntry.column_releaseDate, movie.getReleaseDate());

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        if (uri != null) {
            displayToast("Successfully Marked");
            favBtn.setBackgroundColor(Color.parseColor("#555DBCD2"));
        }
    }

    private boolean deleteFavourite(Result movie) {

        int deleted = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.column_id + "=" + movie.getId(),
                null);

        if (deleted != -1) {
            favBtn.setBackgroundColor(Color.parseColor("#e6e6e6"));
            return true;
        } else {
            return false;
        }
    }


}
