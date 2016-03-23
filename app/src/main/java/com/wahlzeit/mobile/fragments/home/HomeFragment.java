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
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.asyncTasks.GetImageFromUrlTask;
import com.wahlzeit.mobile.fragments.WahlzeitFragment;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment implements WahlzeitFragment {

    View rootView;
    @InjectView(R.id.textview_name_value_home) TextView textViewName;
    @InjectView(R.id.textview_email_value_home) TextView textViewEmail;
    @InjectView(R.id.textview_gender_value_home) TextView textViewGender;
    @InjectView(R.id.imageview_profile_pic_home) ImageView imageViewProfilePicture;
    String textName, textEmail, textGender, userImageUrl;

//    @InjectView(R.id.list_photos_home)
    ListView listViewPhotos;
    List<PhotoListItem> photoListItems;
    PhotoListAdapter photoListAdapter;
    View header;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        ButterKnife.inject(this, rootView);
        listViewPhotos = (ListView) rootView.findViewById(R.id.list_photos_home);
        registerEvents();
        CommunicationManager.manager.getListAllPhotosTask(getActivity()).execute();

        header = inflater.inflate(R.layout.list_header, container ,false);
        ButterKnife.inject(this, header);
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
                String photoStatus = photo.getStatus().toLowerCase();

                // date
                long unixCreationTime = photo.getCreationTime().longValue();
//                java.util.Date dateTime= new java.util.Date((long)unixCreationTime*1000);
                Date date = new Date(unixCreationTime*1000L);
                // Fix it
                SimpleDateFormat sdf = new SimpleDateFormat("MM dd, yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                String photoCreationTime = sdf.format(date);

                String photoTags = photo.getTags().getSize() != 0 ? photo.getTags().toString() : "-";
                String photoName = photo.getIdAsString();
                Image image = WahlzeitModel.model.getImages().get(photo.getIdAsString()).getItems().get(3);
                byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                // create list object
                PhotoListItem photoItem = new PhotoListItem( photoId, photoPraise, photoStatus,
                                                    photoCreationTime, photoTags, photoName, decodedImage);
                photoListItems.add(photoItem);
            }
            photoListAdapter = new PhotoListAdapter(getActivity(), photoListItems);
            listViewPhotos.setAdapter(photoListAdapter);
            listViewPhotos.addHeaderView(header);
        }
    };


    private void registerEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(populateUserPhotosReceiver, new IntentFilter("populate_user_photos"));
    }

    private void unregisterEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(populateUserPhotosReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvents();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterEvents();
    }
}
