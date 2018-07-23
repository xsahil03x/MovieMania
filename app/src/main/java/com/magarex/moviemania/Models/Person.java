package com.magarex.moviemania.Models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.magarex.moviemania.R;
import com.magarex.moviemania.Utils.GlideApp;
import com.magarex.moviemania.Utils.ProjectUtils;

public class Person {
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("biography")
    @Expose
    private String biography;
    @SerializedName("place_of_birth")
    @Expose
    private String birthPlace;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    public Person(String birthday, String name, String biography, String birthPlace, String profilePath) {
        this.birthday = birthday;
        this.name = name;
        this.biography = biography;
        this.birthPlace = birthPlace;
        this.profilePath = profilePath;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getNoDataAvailable() {
        return "No Data";
    }
}
