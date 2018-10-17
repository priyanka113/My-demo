package com.piktale.models;

import java.util.ArrayList;
import java.util.List;

public class PhotoAlbum {
    private int id;
    private String name;

    public PhotoAlbum(String name, int id) {
        this.name = name;
        this.id = id;
    }

    private String coverPhoto;
    private List<Photo> photos = new ArrayList<>();

    public PhotoAlbum() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
