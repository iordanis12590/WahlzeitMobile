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
 * Created by iordanis on 14/04/16.
 */
public class DeletePhotoTask extends AsyncTask<Photo, Void, String> {

    Context mContent;

    public DeletePhotoTask(Context mContent) {
        this.mContent = mContent;
    }

    @Override
    protected String doInBackground(Photo... params) {
        Photo photoToDelete = params[0];
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());
        try {
            WahlzeitApi.Photos.SetStatusAsDeleted deleteCommand = wahlzeitServiceHandle.photos().setStatusAsDeleted(photoToDelete);
            Photo photo = deleteCommand.execute();
            ModelManager.manager.deletePhoto(photo.getIdAsString());
            Log.d("deleted photo", photo.getStatus());
        } catch (IOException e) {
            Log.e("DeletePhotoTask", e.getMessage());
        }
        return "Photo deleted successfully";
    }


}
