package com.wahlzeit.mobile;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Client;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by iordanis on 24/02/16.
 */
public class WahlzeitModel {

    public static WahlzeitModel model = new WahlzeitModel();

    GoogleAccountCredential credential;
    String accountName;
    JSONObject profileData;
    Client currentClient;

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }


    public JSONObject getProfileData() {
        return profileData;
    }

    public void setProfileData(JSONObject profileData) {
        this.profileData = profileData;
    }

    public String getGoogleUserValue(String key) throws JSONException {
        if (profileData != null) {
            if(profileData.has(key)) {
                return profileData.getString(key);
            }
        }
        return "";
    }

}
