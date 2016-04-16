package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;

/**
 * Created by iordanis on 14/04/16.
 */
public class CreatePhotoCaseTask extends AsyncTask<PhotoCase, Void, String> {

    Context mContent;

    public CreatePhotoCaseTask(Context mContext) {
        this.mContent = mContext;
    }

    @Override
    protected String doInBackground(PhotoCase... params) {
        PhotoCase  photoCase = params[0];
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        try {
            WahlzeitApi.Flags.Create createPhotoCaseCommand = wahlzeitServiceHandle.flags().create(photoCase);
            PhotoCase createdPhotoCase = createPhotoCaseCommand.execute();
            Log.d("photo case", createdPhotoCase.getIdAsString());
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return "We informed a moderator";
    }

    protected void onPostExecute(String result) {
        CharSequence text = result;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(mContent, text, duration);
        toast.show();
    }
}
