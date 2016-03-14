package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCollection;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iordanis on 28/02/16.
 */
public class ListAllPhotosTask extends AsyncTask<Void,Void, Void> {
//    CardsDataAdapter mCardAdapter;
//    CardStack mCardStack;
    WahlzeitApi wahlzeitServiceHandle;
    Context mContext;

    public ListAllPhotosTask(Context context) {
        this.mContext = context;
//        this.mCardAdapter = (CardsDataAdapter) adapter;
//        this.mCardStack = (CardStack) container;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            PhotoCollection photoCache = downloadPhotos();
            Map<String, ImageCollection> images = new HashMap<String, ImageCollection>();
            for(Photo photo: photoCache.getItems()) {
                String photoId = photo.getIdAsString();
                ImageCollection tempImages = downloadImages(photoId);
                images.put(photoId, tempImages);
            }
            WahlzeitModel.model.setPhotoCache(photoCache);
            WahlzeitModel.model.setImages(images);
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while fetching fotos/images", ioe);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent populateCardStack = new Intent("populate_photo_card_stack");
        Intent populateUserPhotos = new Intent("populate_user_photos");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(populateCardStack);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(populateUserPhotos);
    }

    private ImageCollection downloadImages(String photoId) throws IOException{
        ImageCollection result = null;
        WahlzeitApi.Images getImagesCommand = wahlzeitServiceHandle.images(photoId);
        result = getImagesCommand.execute();
        return result;
    }

    private PhotoCollection downloadPhotos() throws IOException {
        PhotoCollection result = null;
        WahlzeitApi.Photos.List getPhotosCommand = wahlzeitServiceHandle.photos().list();
        result = getPhotosCommand.execute();
//        PhotoCollection result = list.getItems();
        return result;
    }
}
