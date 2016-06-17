package com.wahlzeit.mobile.model;

import android.graphics.Bitmap;

/**
 * A POJO class holding  information for each card
 * Created by iordanis on 11/03/16.
 */
public class CardModel {

    private String photoId;
    private String ownerName;
    private Bitmap mPhotoImage;

    public CardModel(String photoId, Bitmap photoImage) {
        this.photoId = photoId;
        this.mPhotoImage = photoImage;
    }

    public Bitmap getmPhotoImage() {
        return mPhotoImage;
    }

    public void setmPhotoImage(Bitmap mPhotoImage) {
        this.mPhotoImage = mPhotoImage;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
