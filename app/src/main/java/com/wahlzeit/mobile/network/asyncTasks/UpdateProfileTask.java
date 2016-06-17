package com.wahlzeit.mobile.network.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.network.CommunicationManager;

import java.io.IOException;

/**
 * Created by iordanis on 30/03/16.
 */
public class UpdateProfileTask extends AsyncTask<Void, Void, Void> {
    WahlzeitApi wahlzeitServiceHandle;
    Context mContext;

    public UpdateProfileTask(Context context) {
        this.mContext = context;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            WahlzeitApi.Clients.Update updateClientCommand = wahlzeitServiceHandle.clients().update(ModelManager.manager.getCurrentClient());
            Client result = updateClientCommand.execute();
            boolean notify = result.getNotifyAboutPraise();
            String name = result.getNickName();

//            WahlzeitApi.Clients.Update updateClient = wahlzeitServiceHandle.clients().update(ModelManager.manager.getCurrentClient());
//            Client result = updateClient.execute();
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while updating profile", ioe);
        }
        return null;
    }
}
