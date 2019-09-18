package com.example.monamahdi.moviesappstage1;

/**
 * Created by mona.mahdi on 5/24/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieTrailers {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<trailers> results = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<trailers> getResults() {
        return results;
    }

    public void setResults(List<trailers> results) {
        this.results = results;
    }

}
