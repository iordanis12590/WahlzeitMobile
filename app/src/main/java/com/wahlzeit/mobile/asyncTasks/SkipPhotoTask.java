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
 * Created by iordanis on 08/04/16.
 */
public class SkipPhotoTask extends AsyncTask<Photo, Void, Void> {

    Context mContext;

    public SkipPhotoTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Photo... params) {
        Photo photoToSkip = params[0];
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        try {
            WahlzeitApi.Photos.Skip skipCommand = wahlzeitServiceHandle.photos().skip(photoToSkip);
            Photo skippedPhoto = skipCommand.execute();
            Log.i("Skipping", "");
        } catch (IOException ioe ) {
            Log.e("SkipPhotoTask", ioe.getMessage());
        }
        return null;
    }
}
