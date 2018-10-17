package com.piktale.models;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;

/**
 * @author Varun on 01/07/15.
 */
public class ThumbnailItem {
    public Bitmap image;
    public Filter filter;
    public String name;
    public float alpha = 1.0f;
    public ThumbnailItem() {
        image = null;
        filter = new Filter();
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}