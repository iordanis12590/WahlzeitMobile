package com.wahlzeit.mobile.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.ImageCollection;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCollection;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iordanis on 28/02/16.
 */
public class ListAllPhotosTask extends AsyncTask<Void,Void, Void> {
    ImageView mRandomImage;
    WahlzeitApi wahlzeitServiceHandle;
    public ListAllPhotosTask(ImageView randomImage) {
        this.mRandomImage = randomImage;
        wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            PhotoCollection photoCache = downloadPhotos();
            Map<String, ImageCollection> images = new HashMap<String, ImageCollection>();
            for(Photo photo: photoCache.getItems()) {
                String photoId = photo.getIdAsString();
                ImageCollection tempImages = downloadImages(photoId);
                images.put(photoId, tempImages);
            }
            WahlzeitModel.model.setPhotoCache(photoCache);
            WahlzeitModel.model.setImages(images);
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), "Exception while fetching fotos/images", ioe);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        String randomPhotoId = WahlzeitModel.model.getPhotoCache().getItems().get(0).getIdAsString();
        Image randomImage = WahlzeitModel.model.getImages().get(randomPhotoId).getItems().get(5);
        byte[] imageAsBytes = Base64.decode(randomImage.getImageData().getBytes(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0 , imageAsBytes.length);
        mRandomImage.setImageBitmap(decodedImage);
    }



    private ImageCollection downloadImages(String photoId) throws IOException{
        ImageCollection result = null;
        WahlzeitApi.Images getImagesCommand = wahlzeitServiceHandle.images(photoId);
        result = getImagesCommand.execute();
        return result;
    }

    private PhotoCollection downloadPhotos() throws IOException {
        PhotoCollection result = null;
        WahlzeitApi.Photos.List getPhotosCommand = wahlzeitServiceHandle.photos().list();
        result = getPhotosCommand.execute();
//        PhotoCollection result = list.getItems();
        return result;
    }
}
