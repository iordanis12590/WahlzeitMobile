package com.wahlzeit.mobile.asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Guest;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.activities.LoginActivity;
import com.wahlzeit.mobile.activities.MainActivity;

import java.io.IOException;

/**
 * Created by iordanis on 27/02/16.
 */
public class GuestLoginTask extends AsyncTask<Void, Void, Void> {

    protected LoginActivity myLoginActivity;

    public GuestLoginTask(LoginActivity loginActivity) {
        this.myLoginActivity = loginActivity;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            postGuestUser();
        } catch (IOException ioe){
            Log.e(this.getClass().getName(), "Exception while trying to post guest", ioe);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intent = new Intent(myLoginActivity, MainActivity.class);
        myLoginActivity.startActivity(intent);
        myLoginActivity.showProgress(false);
        myLoginActivity.finish();
    }

    private void postGuestUser() throws IOException {
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        // have to send an initialized Guest instance to avoid EOFE Exception
        // Library bug!
        Guest guest = new Guest();
        WahlzeitApi.Clients.Guests postGuestCommand = wahlzeitServiceHandle.clients().guests(guest);
        Client responseGuest = postGuestCommand.execute();
        if(guest != null) {
            Log.d("guest: ", responseGuest.getId());
        }
    }
}
