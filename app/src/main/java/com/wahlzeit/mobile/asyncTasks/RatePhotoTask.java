package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

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
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        try {
            WahlzeitApi.Photos.Praise praiseCommand = wahlzeitServiceHandle.photos().praise(photoToPraise);
            Photo praisedPhoto = praiseCommand.execute();
            Log.i("Praising", Double.toString(praisedPhoto.getPraise()));
            WahlzeitModel.model.updatePhoto(praisedPhoto);
        } catch (IOException ioe ) {
            Log.e("RatePhotoTask", ioe.getMessage());
        }
        return null;
    }

}
