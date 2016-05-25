package com.wahlzeit.mobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCaseCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCollection;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by iordanis on 24/02/16.
 */
public class WahlzeitModel {

    // Singleton
    public static WahlzeitModel model = new WahlzeitModel();

    String clientsPhotosNextPageToken;
    String allPhotosNextPageToken;
    int clientsPhotosLimit = 2;
    int allPhotosLimit = 5;
    GoogleAccountCredential credential;
    String accountName;
    JSONObject profileData;
    Client currentClient;
    Map<String, Photo> allPhotos = new HashMap<String, Photo>();
    Map<String, Photo> clientsPhotos = new HashMap<String, Photo>();
    PhotoCollection photoCache;
    PhotoCaseCollection photoCaseCache;
    Map<String, PhotoCase> photoCaseCacheAsMap;
    Map<String, ImageCollection> images = new HashMap<String, ImageCollection>();

    public int getClientsPhotosLimit() {
        return clientsPhotosLimit;
    }

    public int getAllPhotosLimit() {
        return allPhotosLimit;
    }

    public String getClientsPhotosNextPageToken() {
        return clientsPhotosNextPageToken;
    }

    public void setClientsPhotosNextPageToken(String clientsPhotosNextPageToken) {
        this.clientsPhotosNextPageToken = clientsPhotosNextPageToken;
    }

    public String getAllPhotosNextPageToken() {
        return allPhotosNextPageToken;
    }

    public void setAllPhotosNextPageToken(String allPhotosNextPageToken) {
        this.allPhotosNextPageToken = allPhotosNextPageToken;
    }

    public Map<String, Photo> getAllPhotos() {
        return this.allPhotos;
    }

    public Map<String, Photo> getClientsPhotos() {
        return this.clientsPhotos;
    }

    public PhotoCaseCollection getPhotoCaseCache() {
        return photoCaseCache;
    }

    public Map<String, PhotoCase> getPhotoCaseCacheAsMap() {
        return photoCaseCacheAsMap;
    }

    public void setPhotoCaseCacheAsMap(PhotoCaseCollection photoCaseCollection) {
        this.photoCaseCache = photoCaseCollection;
        this.photoCaseCacheAsMap = new HashMap<String, PhotoCase>();
        for(PhotoCase photoCase: photoCaseCollection.getItems()) {
            this.photoCaseCacheAsMap.put(photoCase.getIdAsString(), photoCase);
        }
    }

    public PhotoCase getPhotoCaseFromId(String id) {
        PhotoCase result;
        if (photoCaseCacheAsMap.containsKey(id)) {
            result = photoCaseCacheAsMap.get(id);;
        } else {
            result = null;
        }
        return result;
    }

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
        for(Map.Entry<String, ImageCollection> entry: images.entrySet()) {
            if(!imagesExist(entry.getKey())) {
                this.images.put(entry.getKey(), entry.getValue());
            }
        }
//        this.images = images;
    }

    public PhotoCollection getPhotoCache() {
        return photoCache;
    }

    public Photo getPhotoFromId(String photoId) {
        return allPhotos.get(photoId);
//        Photo result = null;
//        for(Photo photo: photoCache.getItems()) {
//            if (photo.getIdAsString().equals(photoId)) {
//                result = photo;
//                break;
//            }
//        }
//        return result;
    }

    public void updatePhoto(Photo updatedPhoto) {
        allPhotos.put(updatedPhoto.getIdAsString(), updatedPhoto);
//        for (Photo photo: photoCache.getItems()) {
//            if(photo.getIdAsString().equals(updatedPhoto.getIdAsString())) {
//                photoCache.getItems().remove(photo);
//                photoCache.getItems().add(updatedPhoto);
//                break;
//            }
//        }
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

    public void setClientsPhotos(List<Photo> photos) {
        for(Photo photo: photos) {
            clientsPhotos.put(photo.getIdAsString(), photo);
        }
    }

    public void addPhoto(Photo photo) {
        if(!photoExists(photo)) {
            allPhotos.put(photo.getIdAsString(), photo);
        }
    }

    public void addImages(String photoId, ImageCollection images) {
        if(!imagesExist(photoId)){
            this.images.put(photoId, images);
        }
    }

    public boolean photoExists(Photo photo) {
        return allPhotos.containsKey(photo.getIdAsString());
    }

    public boolean imagesExist(String photoId) {
        return images.containsKey(photoId);
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
