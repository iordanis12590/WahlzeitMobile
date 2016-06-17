package com.wahlzeit.mobile.network.asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.wahlzeit.mobile.network.CommunicationManager;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.activities.LoginActivity;
import com.wahlzeit.mobile.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by iordanis on 24/02/16.
 */
public class Oauth2LoginTask extends AsyncTask<Void,Void,Void> {

    protected LoginActivity myLoginActivity;
    protected String myEmail;
    protected String myScope;
    protected int myRequest;
    protected Boolean performUserLogin;

    public static String GOOGLE_USER_DATA = "";

    public Oauth2LoginTask(LoginActivity loginActivity, String email, String scope, Boolean performUserLogin) {
        this.myLoginActivity = loginActivity;
        this.myEmail = email;
        this.myScope = scope;
        this.performUserLogin = performUserLogin;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            fetchNameFromProfileServer();
            createWahlzeitUser();
        } catch (IOException ex) {
            onError("Following Error occured, please try again. "
                    + ex.getMessage(), ex);
        } catch (JSONException e) {
            onError("Bad response: "
                    + e.getMessage(), e);
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

    private void fetchNameFromProfileServer() throws IOException, JSONException {
        String token = fetchToken();

        URL url = new URL("https://www.googleapis.com/oauth2" +
                "/v1/userinfo?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        if(sc == 200) {
            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();
            JSONObject profileData = new JSONObject(GOOGLE_USER_DATA);
            ModelManager.manager.setProfileData(profileData);
            return;
        } else if(sc == 401) {
            GoogleAuthUtil.invalidateToken(myLoginActivity, token);
            onError("Server auth error: ", null);
        } else {
            onError("Returned by server: "
                    + sc, null);
            return;
        }
    }

    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(myLoginActivity, myEmail, myScope);
        } catch (UserRecoverableAuthException urae) {
            myLoginActivity.startActivityForResult(urae.getIntent(), myRequest);
        } catch (GoogleAuthException fatalException) {
            fatalException.printStackTrace();
        }

        return null;
    }

    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }

        return new String(bos.toByteArray(), "UTF-8");
    }

    private void createWahlzeitUser() throws IOException, JSONException, SocketTimeoutException {
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(ModelManager.manager.getCredential());

        Client client = new Client();
        client.setId(ModelManager.manager.getGoogleUserValue("id"));
        client.setNickName(ModelManager.manager.getGoogleUserValue("name"));
        if(performUserLogin) {
            client.setAccessRights("USER");
        } else {
            client.setAccessRights("ADMINISTRATOR");
        }
        WahlzeitApi.Clients.Create postClientCommand = wahlzeitServiceHandle.clients().create(client);
        Client responseClient = postClientCommand.execute();
        Log.d("User logged in: ", responseClient.getNickName() + " access rights: " + responseClient.getAccessRights());
        ModelManager.manager.setCurrentClient(responseClient);

    }

    protected void onError(String msg, Exception e) {
        if(e != null) {
            Log.e("Oauth2LoginTask", "Exception: ", e);
        }
    }
}
