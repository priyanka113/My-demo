package com.piktale.listeners;

import com.piktale.models.PhotoAlbum;

import java.util.List;

public interface OnPhotoAlbumLoadedListener {
    public void onPhotosLoaded(List<PhotoAlbum> albums);
    public void onPhotosError(String error);
}
