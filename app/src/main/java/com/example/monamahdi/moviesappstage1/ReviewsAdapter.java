package com.example.monamahdi.moviesappstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mona.mahdi on 5/24/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Reviews> dataSet;

    public ReviewsAdapter(Context context, ArrayList<Reviews> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item, parent, false);
        ViewHolder movieViewHolder = new ViewHolder(v);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {

        Reviews reviews = dataSet.get(position);

        holder.title.setText(reviews.getAuthor());
        holder.content.setText(reviews.getContent());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.reviewTitle);
            content = (TextView) itemView.findViewById(R.id.reviewContent);
        }
    }
}

