package com.wahlzeit.mobile.fragments;

import android.graphics.Bitmap;

/**
 * Created by iordanis on 11/03/16.
 */
public class CardModel {

    private String mTitle;
    private Bitmap mPhotoImage;
    private Bitmap mFlagImage;

    public CardModel(String title, Bitmap photoImage) {
        this.mTitle = title;
        this.mPhotoImage = photoImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Bitmap getmPhotoImage() {
        return mPhotoImage;
    }

    public void setmPhotoImage(Bitmap mPhotoImage) {
        this.mPhotoImage = mPhotoImage;
    }

    public Bitmap getmFlagImage() {
        return mFlagImage;
    }

    public void setmFlagImage(Bitmap mFlagImage) {
        this.mFlagImage = mFlagImage;
    }

}
