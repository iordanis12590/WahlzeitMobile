package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCaseCollection;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;

/**
 * Created by iordanis on 16/04/16.
 */
public class ListAllPhotoCasesTask extends AsyncTask<Void, Void, Void> {

    Context mContext;

    public ListAllPhotoCasesTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            PhotoCaseCollection photoCaseCache = downloaPhotoCases();
            WahlzeitModel.model.setPhotoCaseCacheAsMap(photoCaseCache);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "Exception while fetching photo cases", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent populatePhotoCaseList = new Intent("receive_photo_cases");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(populatePhotoCaseList);
    }

    private PhotoCaseCollection downloaPhotoCases() throws IOException{
        PhotoCaseCollection result = null;
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        WahlzeitApi.Flags.List getPhotoCasesCommand = wahlzeitServiceHandle.flags().list();
        result = getPhotoCasesCommand.execute();
        return result;
    }
}
