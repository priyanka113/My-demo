package com.piktale.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.piktale.listeners.OnPhotoAlbumLoadedListener;
import com.piktale.models.Photo;
import com.piktale.models.PhotoAlbum;

import java.util.ArrayList;
import java.util.List;

public class DevicePhotoManager {

    public DevicePhotoManager() {
    }

    public void getPhotoAlbums(Context context, OnPhotoAlbumLoadedListener listener) {
        // Creating vectors to hold the final albums objects and albums names
        List<PhotoAlbum> phoneAlbums = new ArrayList<>();
        List<String> albumsNames = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };

        // content: style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        if (cur != null && cur.getCount() > 0) {
            Log.i("DeviceImageManager", " query count=" + cur.getCount());

            if (cur.moveToFirst()) {
                String bucketName;
                String data;
                String imageId;
                int bucketNameColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int imageUriColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA);

                int imageIdColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID);

                do {
                    // Get the field values
                    bucketName = cur.getString(bucketNameColumn);
                    data = cur.getString(imageUriColumn);
                    imageId = cur.getString(imageIdColumn);

                    // Adding a new PhonePhoto object to phonePhotos vector
                    Photo phonePhoto = new Photo();
                    phonePhoto.setAlbumName(bucketName);
                    phonePhoto.setUri(data);
                    phonePhoto.setId(Integer.valueOf(imageId));

                    if (albumsNames.contains(bucketName)) {
                        for (PhotoAlbum album : phoneAlbums) {
                            if (album.getName().equals(bucketName)) {
                                album.getPhotos().add(phonePhoto);
                                Log.i("DeviceImageManager", "A photo was added to album => " + bucketName);
                                break;
                            }
                        }
                    } else {
                        PhotoAlbum album = new PhotoAlbum();
                        Log.i("DeviceImageManager", "A new album was created => " + bucketName);
                        album.setId(phonePhoto.getId());
                        album.setName(bucketName);
                        album.setCoverPhoto(phonePhoto.getUri());
                        album.getPhotos().add(phonePhoto);
                        Log.i("DeviceImageManager", "A photo was added to album => " + bucketName);
                        phoneAlbums.add(album);
                        albumsNames.add(bucketName);
                    }

                } while (cur.moveToNext());
            }

            cur.close();
            listener.onPhotosLoaded(phoneAlbums);
        } else {
            listener.onPhotosError("You dont have any photos to load");
        }

    }


}
