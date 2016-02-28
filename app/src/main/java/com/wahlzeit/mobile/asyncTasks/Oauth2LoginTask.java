package com.wahlzeit.mobile.asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Administrator;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.User;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.activities.LoginActivity;
import com.wahlzeit.mobile.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
            WahlzeitModel.model.setProfileData(profileData);
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

    private void createWahlzeitUser() throws IOException, JSONException {
        WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
        Client responseClient;
        if(performUserLogin) {
            User user = new User();
            user.setId(WahlzeitModel.model.getGoogleUserValue("id"));
            user.setNickName(WahlzeitModel.model.getGoogleUserValue("name"));
            WahlzeitApi.Clients.Users postUserCommand = wahlzeitServiceHandle.clients().users(user);
            responseClient = postUserCommand.execute();
            Log.d("User: ", responseClient.getNickName());
        } else {
            // perform admin login
            Administrator admin = new Administrator();
            admin.setId(WahlzeitModel.model.getGoogleUserValue("id"));
            admin.setNickName(WahlzeitModel.model.getGoogleUserValue("name"));
            WahlzeitApi.Clients.Administrators postAdminCommand = wahlzeitServiceHandle.clients().administrators(admin);
            responseClient = postAdminCommand.execute();
            Log.d("Admin: ", responseClient.getNickName());
        }
        WahlzeitModel.model.setCurrentClient(responseClient);

    }

    protected void onError(String msg, Exception e) {
        if(e != null) {
            Log.e("Oauth2LoginTask", "Exception: ", e);
        }
    }
}