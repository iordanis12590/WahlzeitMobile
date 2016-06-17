package com.wahlzeit.mobile.network.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.wahlzeit.mobile.network.CommunicationManager;
import com.wahlzeit.mobile.model.ModelManager;

import java.io.IOException;

/**
 * Created by iordanis on 16/04/16.
 */
public class UpdatePhotoCaseTask extends AsyncTask<PhotoCase, Void, String> {

    Context mContext;

    public UpdatePhotoCaseTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(PhotoCase... params) {
        PhotoCase photoCase = params[0];
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());
        try {
            WahlzeitApi.Flags.Update moderateCommand = wahlzeitServiceHandle.flags().update(photoCase);
            PhotoCase moderatedPhotoCase = moderateCommand.execute();
            Log.d("Praising", moderatedPhotoCase.getPhotoStatus());
        } catch (IOException ioe ) {
            Log.e(this.getClass().getName(), ioe.getMessage());
        }
        return null;
    }
}
