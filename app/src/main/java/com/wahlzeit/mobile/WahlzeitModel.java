package com.wahlzeit.mobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCollection;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by iordanis on 24/02/16.
 */
public class WahlzeitModel {

    // Singleton
    public static WahlzeitModel model = new WahlzeitModel();

    GoogleAccountCredential credential;
    String accountName;
    JSONObject profileData;
    Client currentClient;
    PhotoCollection photoCache;
    Map<String, ImageCollection> images;


    public Map<String, ImageCollection> getImages() {
        return images;
    }

    public Bitmap getImageBitmapOfSize(String photoId, int size) {
        Bitmap result = null;
        List<Image> photoImages = images.get(photoId).getItems();
        Image image = photoImages.get(size);
        // get smaller image if chosen size is not available
        while (image.isEmpty() && size >= 0) {
            --size;
            image = photoImages.get(size);
        }
        if(!image.isEmpty()) {
            byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
            result = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }
        return result;
    }

    public void setImages(Map<String, ImageCollection> images) {
        this.images = images;
    }

    public PhotoCollection getPhotoCache() {
        return photoCache;
    }

    public Photo getPhotoFromId(String photoId) {
        Photo result = null;
        for(Photo photo: photoCache.getItems()) {
            if (photo.getIdAsString().equals(photoId)) {
                result = photo;
                break;
            }
        }
        return result;
    }

    public void updatePhoto(Photo updatedPhoto) {
        for (Photo photo: photoCache.getItems()) {
            if(photo.getIdAsString().equals(updatedPhoto.getIdAsString())) {
                photoCache.getItems().remove(photo);
                photoCache.getItems().add(updatedPhoto);
                break;
            }
        }
    }

    public String getPhotoTagsAsString(Photo photo) {
        String result = "";
        List<String> tagsList;
        if(photo.getTags().getSize() != 0) {
            tagsList = photo.getTags().getTags();
            Iterator<String> tagsListIterator = tagsList.iterator();
            while (tagsListIterator.hasNext()) {
                result += tagsListIterator.next();
                if(tagsListIterator.hasNext()) {
                    result += ",";
                } else {
                    break;
                }
            }
        } else {
            result = "(none)";
        }
        return result;
    }

    public void setPhotoCache(PhotoCollection photoCache) {
        this.photoCache = photoCache;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    public JSONObject getProfileData() {
        return profileData;
    }

    public void setProfileData(JSONObject profileData) {
        this.profileData = profileData;
    }

    public String getGoogleUserValue(String key) throws JSONException {
        if (profileData != null) {
            if(profileData.has(key)) {
                return profileData.getString(key);
            }
        }
        return "";
    }

}
