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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String clientId = WahlzeitModel.model.getCurrentClient().getId();
            WahlzeitApi.Photos.Pagination.List getClientsPhotoCommand = wahlzeitServiceHandle.photos().pagination().list().setFromClient(clientId).setLimit(2);
            CollectionResponsePhoto clientsPhotosCollectionResponse = getClientsPhotoCommand.execute();
            String cursor = clientsPhotosCollectionResponse.getNextPageToken();
            List<Photo> clientPhotos = clientsPhotosCollectionResponse.getItems();
            Map<String, ImageCollection> images = new HashMap<String, ImageCollection>();
            for(Photo photo: clientPhotos) {
                String photoId = photo.getIdAsString();
                ImageCollection tempImages = downloadImages(photoId);
                images.put(photoId, tempImages);
            }
            WahlzeitModel.model.setClientsPhotos(clientPhotos);
            WahlzeitModel.model.setImages(images);
        } catch (IOException ioe) {
        Log.e(this.getClass().getName(), "Exception while fetching client's fotos/images", ioe);
        }
        return null;
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
