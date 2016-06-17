package com.wahlzeit.mobile.model;

import android.graphics.Bitmap;

/**
 * Created by iordanis on 16/04/16.
 */
public class PhotoCaseListItem  {
    private String id, description, tags, flagger, reason, exaplanation;
    private Bitmap image;

    public PhotoCaseListItem() {
    }

    public PhotoCaseListItem(String id, String description, String tags, String flagger, String reason, String exaplanation, Bitmap image) {
        this.id = id;
        this.description = description;
        this.tags = tags;
        this.flagger = flagger;
        this.reason = reason;
        this.exaplanation = exaplanation;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getFlagger() {
        return flagger;
    }

    public void setFlagger(String flagger) {
        this.flagger = flagger;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExaplanation() {
        return exaplanation;
    }

    public void setExaplanation(String exaplanation) {
        this.exaplanation = exaplanation;
    }
}
