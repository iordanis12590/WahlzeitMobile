package com.wahlzeit.mobile;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCollection;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import org.json.JSONException;
import org.json.JSONObject;

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
