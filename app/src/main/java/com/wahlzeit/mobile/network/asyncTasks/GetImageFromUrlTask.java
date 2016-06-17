package com.wahlzeit.mobile.network.asyncTasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by iordanis on 28/02/16.
 */
public class GetImageFromUrlTask extends AsyncTask<String, Void, Bitmap> {

    protected Activity mActivity;
    protected ImageView mImageView;

    public GetImageFromUrlTask(Activity activity, ImageView imageView) {
        this.mActivity = activity;
        this.mImageView = imageView;
    }

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
        mImageView.setImageBitmap(bitmap);
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
