package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.CollectionResponsePhoto;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.ModelManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by iordanis on 01/06/16.
 */
public class GetFilteredPhotosTask extends AsyncTask<String,Void,Void> {
    WahlzeitApi wahlzeitServiceHandle;
    Context mContext;
    String filterTags;

    public GetFilteredPhotosTask(Context context) {
        this.mContext = context;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());
    }


    @Override
    protected Void doInBackground(String... params) {
        try {
            if(params.length > 0) {
                filterTags = params[0];
            }
            List<Photo> downloadedPhotos = downloadAllPhotos();
            if(downloadedPhotos != null) {
                for(Photo photo: downloadedPhotos) {
                    String photoId = photo.getIdAsString();
                    if(ModelManager.manager.photoExists(photoId)) {
                        continue;
                    }
                    ModelManager.manager.addPhoto(photo);
                    if(ModelManager.manager.imagesExist(photoId)){
                        continue;
                    }
                    ImageCollection tempImages = downloadImages(photoId);
                    ModelManager.manager.addImages(photoId, tempImages);
                }
            }
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while filtering fotos/images", ioe);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent populateCardStack = new Intent("populate_photo_card_stack");
//        Intent populateUserPhotos = new Intent("populate_user_photos");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(populateCardStack);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(populateUserPhotos);
    }

    private ImageCollection downloadImages(String photoId) throws IOException{
        ImageCollection result = null;
        WahlzeitApi.Images getImagesCommand = wahlzeitServiceHandle.images(photoId);
        result = getImagesCommand.execute();
        return result;
    }

    private List<Photo> downloadAllPhotos() throws IOException {
        WahlzeitApi.Photos.Pagination.List getFilteredPhotosCommand = wahlzeitServiceHandle.photos().pagination().list();
        if(filterTags != null) {
            getFilteredPhotosCommand.setFilter(filterTags);
        }
        CollectionResponsePhoto response =  getFilteredPhotosCommand.execute();
//        WahlzeitApi.Photos.Pagination.List getAllPhotosCommand = wahlzeitServiceHandle.photos().pagination().list().setLimit(ModelManager.manager.getAllPhotosLimit());
//        String previousNextPageToken = ModelManager.manager.getAllPhotosNextPageToken();
//        if(previousNextPageToken != null && previousNextPageToken != "") {
//            getAllPhotosCommand.setCursor(previousNextPageToken);
//        }
//        CollectionResponsePhoto allPhotosCollectionResponse = getAllPhotosCommand.execute();
//        String nextPageToken = allPhotosCollectionResponse.getNextPageToken();
//        ModelManager.manager.setAllPhotosNextPageToken(nextPageToken);
        return response.getItems();
    }

}
