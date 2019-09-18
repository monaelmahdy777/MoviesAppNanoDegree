package com.example.monamahdi.moviesappstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mona.mahdi on 3/8/2018.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.movieViewHolder> {

   private List<Result> movies = new ArrayList<>();
    public static final String IMAGES_BASE_URL = "http://image.tmdb.org/t/p/";
   private Context context;
   private onItemClicked onItemClicked;

    public MoviesRecyclerAdapter(List<Result> movies, Context context){
        this.movies = movies;
        this.context = context;
    }

    public void updateDataSet(List<Result> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public movieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new movieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final movieViewHolder holder,final int position) {
        Result allMovies = movies.get(position);
        String imagePath = allMovies.getPosterPath();
        String fullPath = IMAGES_BASE_URL + "w185" + imagePath;
        Glide.with(context)
                .load(fullPath)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked.onClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class movieViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public movieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.movie_image);
        }


    }

    public interface onItemClicked{
        void onClicked (int position);
    }

    public void setOnClick(onItemClicked onClick){
        this.onItemClicked = onClick;

    }

}
