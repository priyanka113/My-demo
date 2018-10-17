package com.piktale.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Education {

    @SerializedName("high_school")
    private List<CommonProfileInfo> highSchool = new ArrayList<>();

    @SerializedName("high_sec_school")
    private List<CommonProfileInfo> highSecSchool = new ArrayList<>();

    private List<CommonProfileInfo> university = new ArrayList<>();

    public Education() {
    }

    public List<CommonProfileInfo> getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(List<CommonProfileInfo> highSchool) {
        this.highSchool = highSchool;
    }

    public List<CommonProfileInfo> getHighSecSchool() {
        return highSecSchool;
    }

    public void setHighSecSchool(List<CommonProfileInfo> highSecSchool) {
        this.highSecSchool = highSecSchool;
    }

    public List<CommonProfileInfo> getUniversity() {
        return university;
    }

    public void setUniversity(List<CommonProfileInfo> university) {
        this.university = university;
    }
}
