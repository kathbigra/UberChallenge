package com.example.faisal.uberchallenge.components;

import android.graphics.Bitmap;

public class GridItem {
    private String url;
    private String title;

    public GridItem() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
