package com.magarex.moviemania.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.magarex.moviemania.utils.GlideApp;

import java.io.Serializable;

import static com.magarex.moviemania.utils.ProjectUtils.YOUTUBE_THUMBNAIL;

@Entity(tableName = "trailers")
//        indices = @Index("fav_movie_id"),
//        foreignKeys = @ForeignKey(
//                entity = FavMovieEntity.class,
//                parentColumns = "movieId",
//                childColumns = "fav_movie_id",
//                onDelete = CASCADE))
public class Trailer implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final String id;
    @SerializedName("key")
    @Expose
    private final String key;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("site")
    @Expose
    private final String site;
    @SerializedName("type")
    @Expose
    private final String type;
    @ColumnInfo(name = "fav_movie_id")
    private final Integer favMovieId;

    public Trailer(@NonNull String id, String key, String name, String site, String type, Integer favMovieId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.favMovieId = favMovieId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public Integer getFavMovieId() {
        return favMovieId;
    }

    @BindingAdapter({"android:trailerImage"})
    public static void loadTrailerImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(String.format(YOUTUBE_THUMBNAIL, imageUrl))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                .into(view);
    }
}
