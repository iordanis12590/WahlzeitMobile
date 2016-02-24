package com.wahlzeit.mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Created by iordanis on 23/02/16.
 */
public class CommunicationManager {
    private static final JsonFactory GSON_FACTORY = new GsonFactory();
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    public static final String LOCAL_APP_ENGINE_SERVER_URL_FOR_ANDROID = "http://10.0.2.2:9999/_ah/api/";
    public static final String WEB_CLIENT = "server:client_id:326325117092-mpckcvum5182l7udgu6sq5du1ivmeo6a.apps.googleusercontent.com";

    public static CommunicationManager manager = new CommunicationManager();

    public WahlzeitApi getApiServiceHandler(@Nullable GoogleAccountCredential credential) {
        WahlzeitApi.Builder wahlzeitApiBuilder = new WahlzeitApi.Builder(HTTP_TRANSPORT, GSON_FACTORY, credential);
        wahlzeitApiBuilder.setRootUrl(LOCAL_APP_ENGINE_SERVER_URL_FOR_ANDROID);

        return wahlzeitApiBuilder.build();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "Available");
            return true;
        }

        Log.e("Network Testing", "Not Available");
        return false;
    }

}
