package com.magarex.moviemania.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Review> results;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public ReviewResponse(Integer id, Integer page, List<Review> results, Integer totalPages, Integer totalResults) {
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPage() {
        return page;
    }

    public List<Review> getResults() {
        return results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }
}
