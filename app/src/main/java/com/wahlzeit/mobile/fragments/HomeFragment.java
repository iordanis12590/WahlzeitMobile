package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HomeFragment extends Fragment {

    ImageView imageProfile;
    TextView textViewName, textViewEmail, textViewGender;
    String textName, textEmail, textGender, userImageUrl;

    View rootView;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        textViewName = (TextView) rootView.findViewById(R.id.textViewNameValue);
        textViewEmail = (TextView) rootView.findViewById(R.id.textViewEmailValue);
        textViewGender = (TextView) rootView.findViewById(R.id.textViewGenderValue);
        imageProfile = (ImageView) rootView.findViewById(R.id.imageView1);

        populateTextAndImage();
        return rootView;
    }

    private void populateTextAndImage() {
        try {
            JSONObject profileData = WahlzeitModel.model.getProfileData(); //new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);

            if(profileData.has("picture")) {
                userImageUrl = profileData.getString("picture");
                new GetImageFromUrl().execute(userImageUrl);
            }
            if(profileData.has("name")) {
                textName = profileData.getString("name");
                textViewName.setText(textName);
            }
            if(profileData.has("gender")) {
                textGender = profileData.getString("gender");
                textViewGender.setText(textGender);
            }

            if(profileData.has("id") ) {
                String id = profileData.getString("id");
                Log.d("User id: ", profileData.getString("id"));
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            Bitmap map = null;
            for(String url : urls) {
                map = downloadImage(url);
            }

            return map;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageProfile.setImageBitmap(bitmap);
        }

        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        private InputStream getHttpConnection(String urlString) throws IOException{
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpURLConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stream;
        }
    }


}
