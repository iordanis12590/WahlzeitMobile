package com.wahlzeit.mobile.fragments.home;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.asyncTasks.GetImageFromUrlTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment {

    View rootView;
    @InjectView(R.id.textview_name_value_home) TextView textViewName;
    @InjectView(R.id.textview_email_value_home) TextView textViewEmail;
    @InjectView(R.id.textview_gender_value_home) TextView textViewGender;
    @InjectView(R.id.imageview_profile_pic_home) ImageView imageViewProfilePicture;
    String textName, textEmail, textGender, userImageUrl;

    @InjectView(R.id.list_photos_home) ListView listViewPhotos;
    List<PhotoListItem> photoListItems;
    PhotoListAdapter photoListAdapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, rootView);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(populateUserPhotosReceiver, new IntentFilter("populate_user_photos"));
        populateTextAndImage();
        setupPhotosList();
        return rootView;
    }

    private void populateTextAndImage() {
        try {
            userImageUrl = WahlzeitModel.model.getGoogleUserValue("picture");
            new GetImageFromUrlTask(this.getActivity(), imageViewProfilePicture).execute(userImageUrl);

            textName = WahlzeitModel.model.getGoogleUserValue("name");
            textViewName.setText(textName);


            textEmail = WahlzeitModel.model.getGoogleUserValue("email");
            textViewEmail.setText(textEmail);

            textGender = WahlzeitModel.model.getGoogleUserValue("gender");
            textViewGender.setText(textGender);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupPhotosList() {
//        photoListItems = new ArrayList<PhotoListItem>();
//        photoListAdapter = new PhotoListAdapter(getActivity(), photoListItems);
//        mapPhotosToPhotoListItems();
//        if(photoListItems != null) {
//            photoListAdapter.addAll(photoListItems);
//        }
//        listViewPhotos.setAdapter(photoListAdapter);
    }

    private BroadcastReceiver populateUserPhotosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            photoListItems = new ArrayList<PhotoListItem>();
            for(Photo photo: WahlzeitModel.model.getPhotoCache().getItems()) {
                // get photo values
                String photoId = photo.getId().getStringValue();
                String photoPraise = photo.getPraise().toString();
                String photoStatus = photo.getStatus();
                String photoCreationTime = photo.getCreationTime().toString();
                String photoTags = photo.getTags().toString();
                String photoLink = photo.getTags().toString();
                Image image = WahlzeitModel.model.getImages().get(photo.getIdAsString()).getItems().get(3);
                byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                // create list object
                PhotoListItem photoItem = new PhotoListItem( photoId, photoPraise, photoStatus,
                                                    photoCreationTime, photoTags, photoLink, decodedImage);
                photoListItems.add(photoItem);
            }
            photoListAdapter = new PhotoListAdapter(getActivity(), photoListItems);
            listViewPhotos.setAdapter(photoListAdapter);
        }
    };

//    private void mapPhotosToPhotoListItems() {
//        if (photos != null) {
//            photoListItems = new ArrayList<PhotoListItem>();
//            for(Photo photo: photos.getItems()) {
//                PhotoListItem photoItem = new PhotoListItem(
//                                                    photo.getId().getStringValue(),
//                                                    photo.getPraise().toString(),
//                                                    photo.getStatus(),
//                                                    photo.getCreationTime().toString(),
//                                                    photo.getTags().toString(),
//                                                    photo.getIdAsString(),
//                                                    null);
//                Image image = WahlzeitModel.model.getImages().get(photo.getIdAsString()).getItems().get(3);
//                byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
//                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//                photoItem.setImage(decodedImage);
//                photoListItems.add(photoItem);
//            }
//        }
//    }

}
