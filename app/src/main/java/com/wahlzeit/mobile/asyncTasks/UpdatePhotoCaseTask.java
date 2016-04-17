package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

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
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
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
