package com.wahlzeit.mobile.network.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.network.CommunicationManager;
import com.wahlzeit.mobile.model.ModelManager;

import java.io.IOException;

/**
 * Created by iordanis on 07/04/16.
 */
public class RatePhotoTask extends AsyncTask<Photo, Void, Void> {

    Context context;

    public RatePhotoTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Photo... params) {
        Photo photoToPraise = params[0];
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());
        try {
            WahlzeitApi.Photos.Praise praiseCommand = wahlzeitServiceHandle.photos().praise(photoToPraise.getIdAsString(), photoToPraise);
            Photo praisedPhoto = praiseCommand.execute();
            Log.i("Praising", Double.toString(praisedPhoto.getPraise()));
            ModelManager.manager.updatePhoto(praisedPhoto);
        } catch (IOException ioe ) {
            Log.e("RatePhotoTask", ioe.getMessage());
        }
        return null;
    }

}
