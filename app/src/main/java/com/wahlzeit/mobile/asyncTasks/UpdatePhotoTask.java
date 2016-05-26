package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;

/**
 * Created by iordanis on 13/04/16.
 */
public class UpdatePhotoTask extends AsyncTask<Photo, Void, String> {

    Context context;

    public UpdatePhotoTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Photo... params) {
        Photo photoToUpdate = params[0];
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        try {
            WahlzeitApi.Photos.Update updateCommand = wahlzeitServiceHandle.photos().update(photoToUpdate);
            Photo updatedPhoto = updateCommand.execute();
            WahlzeitModel.model.updatePhoto(updatedPhoto);
        } catch (IOException e) {
            Log.e("UpdatePhotoTask", e.getMessage());
        }
        return "Photo updated successfully";
    }

    protected void onPostExecute(String result) {
        CharSequence text = result;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        // TODO: Update card stack and home list
    }
}
