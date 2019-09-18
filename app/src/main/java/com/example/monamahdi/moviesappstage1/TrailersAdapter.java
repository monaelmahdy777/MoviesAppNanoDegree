package com.example.monamahdi.moviesappstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mona.mahdi on 5/24/2018.
 */

public class TrailersAdapter  extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private Context context;
    private ArrayList<trailers> dataSet;
    private OnItemClicked onClick;

    public TrailersAdapter(Context context, ArrayList<trailers> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button playButton ;

        TrailerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            playButton = (Button)view.findViewById(R.id.btn_play);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_item, parent, false);
        TrailerViewHolder movieViewHolder = new TrailerViewHolder(v);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        holder.title.setText(dataSet.get(position).getName());

        holder.playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

}
