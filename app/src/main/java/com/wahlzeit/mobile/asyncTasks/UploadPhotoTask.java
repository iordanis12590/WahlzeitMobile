package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;

import java.io.IOException;

/**
 * Created by iordanis on 08/04/16.
 */
public class UploadPhotoTask extends AsyncTask<Photo, Void, String> {

    Context mContent;

    public UploadPhotoTask(Context mContent) {
        this.mContent = mContent;
    }

    @Override
    protected String doInBackground(Photo... params) {
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(null);
        try {
            WahlzeitApi.Photos.Upload uploadCommand = wahlzeitServiceHandle.photos().upload(params[0]);
            Photo myPhoto = uploadCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Upload Complete";
    }

    protected void onPostExecute(String result) {
        CharSequence text = result;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(mContent, text, duration);
        toast.show();

        // TODO: Update card stack and home list
    }
}