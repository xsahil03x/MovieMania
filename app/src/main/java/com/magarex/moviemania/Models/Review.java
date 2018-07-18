package com.magarex.moviemania.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "reviews")
//        ,indices = @Index("fav_movie_id"),
//        foreignKeys = @ForeignKey(
//                entity = FavMovieEntity.class,
//                parentColumns = "movieId",
//                childColumns = "fav_movie_id",
//                onDelete = CASCADE))
public class Review implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final String id;
    @SerializedName("author")
    @Expose
    private final String author;
    @SerializedName("content")
    @Expose
    private final String content;
    @SerializedName("url")
    @Expose
    private final String url;
    @ColumnInfo(name = "fav_movie_id")
    private final Integer favMovieId;

    public Review(String author, String content, @NonNull String id, String url, final Integer favMovieId) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
        this.favMovieId = favMovieId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Integer getFavMovieId() {
        return favMovieId;
    }
}
