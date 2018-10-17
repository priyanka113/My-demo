package com.piktale.models;

public class ProfileTag {

    private String id;
    private String picture;
    private int resId;

    public ProfileTag() {
    }

    public ProfileTag(String id, String picture) {
        this.id = id;
        this.picture = picture;
    }

    public ProfileTag(String id, String picture, int resId) {
        this.id = id;
        this.picture = picture;
        this.resId = resId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
