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
 * Created by iordanis on 23/05/16.
 */
public class GetClientsPhotoTask extends AsyncTask<Void, Void, Void> {

    Context mContext;
    WahlzeitApi wahlzeitServiceHandle;


    public GetClientsPhotoTask(Context context) {
        this.mContext = context;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            List<Photo> clientPhotos = downloadClientsPhotos();
            if(clientPhotos != null) {
                for(Photo photo: clientPhotos) {
                    String photoId = photo.getIdAsString();
                    if(WahlzeitModel.model.clientsPhotosExists(photoId)){
                        continue;
                    }
                    WahlzeitModel.model.addClientsPhoto(photo);
                    if(WahlzeitModel.model.imagesExist(photoId)) {
                        continue;
                    }
                    ImageCollection tempImages = downloadImages(photoId);
                    WahlzeitModel.model.addImages(photoId, tempImages);
                }
            }
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while fetching client's fotos/images", ioe);
        }
        return null;
    }

    private List<Photo> downloadClientsPhotos() throws IOException {
        String clientId = WahlzeitModel.model.getCurrentClient().getId();
        WahlzeitApi.Photos.Pagination.List getClientsPhotoCommand = wahlzeitServiceHandle.photos().pagination().list().setFromClient(clientId).setLimit(WahlzeitModel.model.getClientsPhotosLimit());
        String previousNextPageToken = WahlzeitModel.model.getClientsPhotosNextPageToken();
        if(previousNextPageToken != null && previousNextPageToken != "") {
            getClientsPhotoCommand.setCursor(previousNextPageToken);
        }
        CollectionResponsePhoto clientsPhotosCollectionResponse = getClientsPhotoCommand.execute();
        // resend request and get all photos
        if(clientsPhotosCollectionResponse.getItems() == null) {
            WahlzeitModel.model.setClientsPhotosNextPageToken("");
            WahlzeitModel.model.setClientsPhotosLimit(100);
            getClientsPhotoCommand = wahlzeitServiceHandle.photos().pagination().list().setFromClient(clientId);
            clientsPhotosCollectionResponse = getClientsPhotoCommand.execute();
        } else {
            String nextPageToken = clientsPhotosCollectionResponse.getNextPageToken();
            WahlzeitModel.model.setClientsPhotosNextPageToken(nextPageToken);
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
        WahlzeitApi.Images getImagesCommand = wahlzeitServiceHandle.images(photoId);
        result = getImagesCommand.execute();
        return result;
    }


}
