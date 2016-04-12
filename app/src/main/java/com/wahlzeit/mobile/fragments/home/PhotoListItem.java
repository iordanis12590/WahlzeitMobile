package com.wahlzeit.mobile.fragments.home;

import android.graphics.Bitmap;

/**
 * Created by iordanis on 14/03/16.
 */
public class PhotoListItem {
    private String id;
    private String praise, status, uploadDate, photoTags, photoName;
    private Bitmap image;

    public PhotoListItem() {
    }

    public PhotoListItem(String id, String praise, String status, String uploadDate,
                         String photoTags, Bitmap image) {
        super();
        this.id = id;
        this.praise = praise;
        this.status = status;
        this.uploadDate = uploadDate;
        this.photoTags = photoTags;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPhotoTags() {
        return photoTags;
    }

    public void setPhotoTags(String photoTags) {
        this.photoTags = photoTags;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}