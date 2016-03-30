package com.wahlzeit.mobile.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

/**
 * Created by iordanis on 30/03/16.
 */
public class UpdateProfile extends AsyncTask<Void, Void, Void> {
    WahlzeitApi wahlzeitServiceHandle;
    Context mContext;

    public UpdateProfile(Context context) {
        this.mContext = context;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());

    }

    @Override
    protected Void doInBackground(Void... params) {
//        try {
//            WahlzeitApi.Clients.Update updateClient = wahlzeitServiceHandle.clients().update(WahlzeitModel.model.getCurrentClient());
//            Client result = updateClient.execute();
//        } catch (IOException ioe) {
//            Log.e(this.getClass().getName(), "Exception while updating profile", ioe);
//        }
        return null;
    }
}
