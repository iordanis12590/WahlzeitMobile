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
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by iordanis on 28/02/16.
 */
public class ListAllPhotosTask extends AsyncTask<Void,Void, Void> {
    WahlzeitApi wahlzeitServiceHandle;
    Context mContext;

    public ListAllPhotosTask(Context context) {
        this.mContext = context;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            List<Photo> downloadedPhotos = downloadAllPhotos();
            if(downloadedPhotos != null) {
                for(Photo photo: downloadedPhotos) {
                    String photoId = photo.getIdAsString();
                    if(WahlzeitModel.model.photoExists(photo)) {
                        continue;
                    }
                    WahlzeitModel.model.addPhoto(photo);
                    if(WahlzeitModel.model.imagesExist(photoId)){
                        continue;
                    }
                    ImageCollection tempImages = downloadImages(photoId);
                    WahlzeitModel.model.addImages(photoId, tempImages);
                }
            }
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while fetching fotos/images", ioe);
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
        WahlzeitApi.Photos.Pagination.List getAllPhotosCommand = wahlzeitServiceHandle.photos().pagination().list().setLimit(WahlzeitModel.model.getAllPhotosLimit());
        String previousNextPageToken = WahlzeitModel.model.getAllPhotosNextPageToken();
        if(previousNextPageToken != null && previousNextPageToken != "") {
            getAllPhotosCommand.setCursor(previousNextPageToken);
        }
        CollectionResponsePhoto allPhotosCollectionResponse = getAllPhotosCommand.execute();
        String nextPageToken = allPhotosCollectionResponse.getNextPageToken();
        WahlzeitModel.model.setAllPhotosNextPageToken(nextPageToken);
        return allPhotosCollectionResponse.getItems();
    }

}
