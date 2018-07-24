package com.magarex.moviemania.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
