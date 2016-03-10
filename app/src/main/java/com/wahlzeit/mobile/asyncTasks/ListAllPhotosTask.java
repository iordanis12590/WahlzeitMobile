package com.wahlzeit.mobile.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

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
//    SimpleCardStackAdapter mAdapter;
//    CardContainer mContainer;
    WahlzeitApi wahlzeitServiceHandle;

    public ListAllPhotosTask(BaseAdapter adapter, AdapterView<ListAdapter> container) {
//        mAdapter = (SimpleCardStackAdapter) adapter;
//        mContainer = (CardContainer) container;
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

        for(Photo photo: WahlzeitModel.model.getPhotoCache().getItems()) {
            String photoId = photo.getIdAsString();
            Image image = WahlzeitModel.model.getImages().get(photoId).getItems().get(3);
            byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//            mAdapter.add(new CardModel("Photo by "+ "Owner", "Description", decodedImage));
        }

//        mContainer.setAdapter(mAdapter);
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
