package com.magarex.moviemania.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favourites")
public class FavouriteMovie {
    @PrimaryKey(autoGenerate = true)
    private int uId;
    @SerializedName("id")
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterUrl;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private float voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @Ignore
    public FavouriteMovie(String originalTitle, String title, String releaseDate, String posterUrl, float voteAverage,
                          String overview, String backdropPath) {
        this.originalTitle = originalTitle;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.backdropPath = backdropPath;
    }

    public FavouriteMovie(int uId, int movieId, String originalTitle, String title, String releaseDate, String posterUrl,
                          float voteAverage, String overview, String backdropPath) {
        this.uId = uId;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.backdropPath = backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
