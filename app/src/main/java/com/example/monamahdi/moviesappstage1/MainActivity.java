package com.example.monamahdi.moviesappstage1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.monamahdi.moviesappstage1.providers.MovieContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesRecyclerAdapter.onItemClicked {

    private RecyclerView moviesRecyclerView;
    private MoviesRecyclerAdapter moviesRecyclerAdapter;
    private int mNumber = 20;
    private static final String API_KEY = "";
    private List<Result> results = new ArrayList<>();
    public static final int TOP_RATED = 0;
    public static final int MOST_POPULAR = 1;
    private int state = MOST_POPULAR;
    private static final int MY_FAVOURITE = 2;
    private ArrayList<Result> allMovies;
    String mRecyclerPositionKey;
    Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = (RecyclerView)findViewById(R.id.RV_movies);
        int numberOfColumns = 2;
       // moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);


        gridLayoutManager.onSaveInstanceState();

        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(results,getApplicationContext());
        moviesRecyclerAdapter.setOnClick(this);
        moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
        allMovies = new ArrayList<>(4);

        if(savedInstanceState!= null){
            state = savedInstanceState.getInt("state");
        }
        if(state ==MOST_POPULAR){
            loadMovies(MOST_POPULAR);
            state = MOST_POPULAR;
        }else if(state == TOP_RATED){
            loadMovies(TOP_RATED);
            state = TOP_RATED;
        }else if (state == MY_FAVOURITE) {

            allMovies = getAllFavouriteMovies();
            moviesRecyclerAdapter.updateDataSet(allMovies);
            state = MY_FAVOURITE;
        }
    }

    public void loadMovies(int index){
        Call<DiscoverMoviesResponse> call = null;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if(index == TOP_RATED){
            call = apiInterface.getTopRated(API_KEY);
        }else if(index == MOST_POPULAR){
            call = apiInterface.getMostPopular(API_KEY);
        }
        call.enqueue(new Callback<DiscoverMoviesResponse>() {
            @Override
            public void onResponse(Call<DiscoverMoviesResponse> call, Response<DiscoverMoviesResponse> response) {
                DiscoverMoviesResponse response1 = response.body();
                results = (ArrayList<Result>) response1.getResults();
                moviesRecyclerAdapter.updateDataSet((ArrayList<Result>) response1.getResults());
            }

            @Override
            public void onFailure(Call<DiscoverMoviesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed to connect to API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClicked(int position) {
        if(isNetworkAvailable()){
            String details = new Gson().toJson(results.get(position));
            Intent intent = new Intent(MainActivity.this,MoviesDetailsActivity.class);
            intent.putExtra("details",details);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, "failed to connect to API", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.top_rated:
                loadMovies(TOP_RATED);
                state = TOP_RATED;
                return true;
            case R.id.most_popular:
                loadMovies(MOST_POPULAR);
                state = MOST_POPULAR;
                return true;
            case R.id.fav:
                allMovies = getAllFavouriteMovies();
                moviesRecyclerAdapter.updateDataSet(allMovies);
                state = MY_FAVOURITE;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayList<Result> getAllFavouriteMovies() {
        ArrayList<Result> movies = new ArrayList<Result>();

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Result movie = new Result();
                movie.setId(Integer.parseInt(cursor.getString(0)));
                movie.setVoteCount(Integer.parseInt(cursor.getString(1)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(2)));
                movie.setTitle(cursor.getString(3));
                movie.setPopularity(Double.parseDouble(cursor.getString(4)));
                movie.setPosterPath(cursor.getString(5));
                movie.setOriginalLanguage(cursor.getString(6));
                movie.setOriginalTitle(cursor.getString(7));
                movie.setOverview(cursor.getString(8));
                movie.setReleaseDate(cursor.getString(9));

                movies.add(movie);
            } while (cursor.moveToNext());
        }

        return movies;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("state", state);
        outState.putParcelable(mRecyclerPositionKey,
                moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        listState = state.getParcelable(mRecyclerPositionKey);
        moviesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
