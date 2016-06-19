package com.wahlzeit.mobile.network.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.CollectionResponsePhoto;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.network.CommunicationManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iordanis on 23/05/16.
 */
public class GetClientsPhotoTask extends AsyncTask<Void, Void, Void> {

    Context mContext;
    WahlzeitApi wahlzeitServiceHandle;


    public GetClientsPhotoTask(Context context) {
        this.mContext = context;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            List<Photo> clientPhotos = downloadClientsPhotos();
            if(clientPhotos != null) {
                for(Photo photo: clientPhotos) {
                    String photoId = photo.getIdAsString();
                    if(ModelManager.manager.clientsPhotosExists(photoId)){
                        continue;
                    }
                    ModelManager.manager.addClientsPhoto(photo);
                    if(ModelManager.manager.imagesExist(photoId)) {
                        continue;
                    }
                    ImageCollection tempImages = downloadImages(photoId);
                    ModelManager.manager.addImages(photoId, tempImages);
                }
            }
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while fetching client's fotos/images", ioe);
        }
        return null;
    }

    private List<Photo> downloadClientsPhotos() throws IOException {
        String clientId = ModelManager.manager.getCurrentClient().getId();
        WahlzeitApi.Photos.List getClientsPhotoCommand = wahlzeitServiceHandle.photos().list();
        String previousNextPageToken = ModelManager.manager.getClientsPhotosNextPageToken();
        if(previousNextPageToken != null && previousNextPageToken != "") {
            getClientsPhotoCommand.setCursor(previousNextPageToken);
        }
        CollectionResponsePhoto clientsPhotosCollectionResponse = getClientsPhotoCommand.execute();
        // resend request and get all photos
        if(clientsPhotosCollectionResponse.getItems() == null) {
            ModelManager.manager.setClientsPhotosNextPageToken("");
            ModelManager.manager.setClientsPhotosLimit(100);
            getClientsPhotoCommand = wahlzeitServiceHandle.photos().list().setFromClient(clientId);
            clientsPhotosCollectionResponse = getClientsPhotoCommand.execute();
        } else {
            String nextPageToken = clientsPhotosCollectionResponse.getNextPageToken();
            ModelManager.manager.setClientsPhotosNextPageToken(nextPageToken);
        }
        return clientsPhotosCollectionResponse.getItems();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent populateUserPhotos = new Intent("populate_user_photos");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(populateUserPhotos);

    }

    private ImageCollection downloadImages(String photoId) throws IOException{
        ImageCollection result = null;
        WahlzeitApi.Images getImagesCommand = wahlzeitServiceHandle.images(photoId).setImageSizes(Arrays.asList("MEDIUM", "SMALL"));
        result = getImagesCommand.execute();
        return result;
    }


}
