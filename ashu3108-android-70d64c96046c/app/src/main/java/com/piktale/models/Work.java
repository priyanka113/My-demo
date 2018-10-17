package com.piktale.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Work {
    @SerializedName("company")
    private List<CommonProfileInfo> companies = new ArrayList<>();

    public Work() {
    }

    public List<CommonProfileInfo> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CommonProfileInfo> companies) {
        this.companies = companies;
    }
}
