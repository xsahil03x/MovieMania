package com.magarex.moviemania.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.magarex.moviemania.R;
import com.magarex.moviemania.Utils.GlideApp;
import com.magarex.moviemania.Utils.ProjectUtils;

import java.io.Serializable;

@Entity(tableName = "casts")
public class Cast implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("character")
    @Expose
    private String character;

    public Cast(@NonNull final Integer id, String name, String profilePath, String character) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
        this.character = character;
    }

    @BindingAdapter({"android:castImage"})
    public static void loadCastImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(ProjectUtils.POSTER_BASE_URL + imageUrl)
                .placeholder(R.drawable.movie_poster_placeholder)
                .error(R.drawable.movie_poster_error)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                .into(view);
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
