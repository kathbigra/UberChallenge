package com.example.faisal.uberchallenge;

import android.graphics.Bitmap;

public class GridItem {
    private String image;

    public Bitmap getBitMap() {
        return bitMap;
    }

    private Bitmap bitMap;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBitMap(Bitmap bitMap) {
        this.bitMap = bitMap;
    }
}
