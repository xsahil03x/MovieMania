package com.magarex.moviemania.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results;

    public TrailerResponse(Integer id, List<Trailer> results) {
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
