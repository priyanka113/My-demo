package com.piktale.models;

public class ProfileGallery {

    private String id;
    private String picture;
    private int resId;
    private int width = 150;
    private int height = (int) (50 + Math.random() * 250);

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

    public ProfileGallery(String id, String picture, int resId) {
        this.id = id;
        this.picture = picture;
        this.resId = resId;
    }

    public int getResId() {

        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
}
