package com.wahlzeit.mobile.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCaseCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoId;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCollection;

/**
 * A class that provides access, manages and temporary stores the applications content
 * Created by iordanis on 24/02/16.
 */
public class ModelManager {

    // Singleton
    public static ModelManager manager = new ModelManager();

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
//    PhotoCollection photoCache;
    PhotoCaseCollection photoCaseCache;
    Map<String, PhotoCase> photoCaseCacheAsMap;
    Map<String, ImageCollection> images = new HashMap<String, ImageCollection>();

    public int getWelcomeFragmentPosition() {
        if (getCurrentClient().getAccessRights().toLowerCase().equals("guest")) {
            return 0;
        } else {
            return 2;
        }
    }

    public List<String> getPraisedPhotoIds() {
        List<String> praisedPhotoIdsAsString = new ArrayList<>();
        List<PhotoId> praisedPhotos = currentClient.getPraisedPhotoIds();
        if(praisedPhotos != null) {
            for(PhotoId photoId: praisedPhotos) {
                praisedPhotoIdsAsString.add(photoId.getStringValue());
            }
        }
        return praisedPhotoIdsAsString;
    }

    public List<String> getSkippedPhotoIds() {
        List<String> skippedPhotoIdsAsString = new ArrayList<>();
        List<PhotoId> skippedPhotos = currentClient.getSkippedPhotoIds();
        if(skippedPhotos != null) {
            for(PhotoId photoId: skippedPhotos) {
                skippedPhotoIdsAsString.add(photoId.getStringValue());
            }
        }
        return skippedPhotoIdsAsString;
    }

    public void setPraisedPhoto(String photoIdAsString) {
        PhotoId photoId = new PhotoId().setStringValue(photoIdAsString);
        List<PhotoId> alreadyPraisedPhotos = this.currentClient.getPraisedPhotoIds();
        if(alreadyPraisedPhotos == null) {
            alreadyPraisedPhotos = new ArrayList<PhotoId>();
        }
        alreadyPraisedPhotos.add(photoId);
        currentClient.setPraisedPhotoIds(alreadyPraisedPhotos);
    }

    public void setSkippedPhoto(String photoIdAsString) {
        final PhotoId photoId = new PhotoId().setStringValue(photoIdAsString);
        List<PhotoId> alreadySkippedPhotos = currentClient.getSkippedPhotoIds();
        if(alreadySkippedPhotos == null) {
            alreadySkippedPhotos = new ArrayList<PhotoId>();
        }
        alreadySkippedPhotos.add(photoId);
        currentClient.setSkippedPhotoIds(alreadySkippedPhotos);
    }

    public void deletePhoto(String photoId) {
        if(photoExists(photoId)) {
            allPhotos.remove(photoId);
        }
        if(clientsPhotosExists(photoId)) {
            clientsPhotos.remove(photoId);
        }
        deleteImages(photoId);
    }

    public void deleteImages(String photoId) {
        if(images.containsKey(photoId)) {
            images.remove(photoId);
        }
    }

    public void setClientsPhotosLimit(int clientsPhotosLimit) {
        this.clientsPhotosLimit = clientsPhotosLimit;
    }

    public int getClientsPhotosLimit() {
        return clientsPhotosLimit;
    }

    public void setAllPhotosLimit(int allPhotosLimit) {
        this.allPhotosLimit = allPhotosLimit;
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
        Image image = photoImages.get(photoImages.size() - 1);
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
    }



    public Photo getPhotoFromId(String photoId) {
        return allPhotos.get(photoId);
    }

    public Photo getClientsPhotoFromId(String photoId) {
        return clientsPhotos.get(photoId);
    }

    public void updatePhoto(Photo updatedPhoto) {
        if(allPhotos.containsKey(updatedPhoto.getIdAsString())) {
            allPhotos.put(updatedPhoto.getIdAsString(), updatedPhoto);
        }
        if (clientsPhotos.containsKey(updatedPhoto.getIdAsString())) {
            clientsPhotos.put(updatedPhoto.getIdAsString(), updatedPhoto);
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

    public String[] getTagsFromText(String tagsText) {
        String[] result;
        String consistentTagsText = tagsText.replaceAll("\\s+", "");
        result = tagsText.split(",");
        return result;
    }

    public void setClientsPhotos(List<Photo> photos) {
        for(Photo photo: photos) {
            clientsPhotos.put(photo.getIdAsString(), photo);
        }
    }

    public void addPhoto(Photo photo) {
//        if(!photoExists(photo.getIdAsString())) {
            allPhotos.put(photo.getIdAsString(), photo);
//        }
    }

    public void addClientsPhoto(Photo photo) {
//        if(!clientsPhotosExists(photo.getIdAsString())) {
            clientsPhotos.put(photo.getIdAsString(), photo);
//        }
    }

    public void addImages(String photoId, ImageCollection images) {
        if(!imagesExist(photoId)){
            this.images.put(photoId, images);
        }
    }

    public boolean photoExists(String photoId) {
        return allPhotos.containsKey(photoId);
    }

    public boolean clientsPhotosExists(String photoId) {
        return clientsPhotos.containsKey(photoId);
    }

    public boolean imagesExist(String photoId) {
        return images.containsKey(photoId);
    }

//    public void setPhotoCache(PhotoCollection photoCache) {
//        this.photoCache = photoCache;
//    }

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
