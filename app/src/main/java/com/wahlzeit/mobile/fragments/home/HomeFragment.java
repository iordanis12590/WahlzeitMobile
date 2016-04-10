package com.wahlzeit.mobile.fragments.home;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.activities.MainActivity;
import com.wahlzeit.mobile.asyncTasks.GetImageFromUrlTask;
import com.wahlzeit.mobile.activities.EditPhotoActivity;
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
    String selectedPhotoId;

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
        header = inflater.inflate(R.layout.fragment_home_list_header, container ,false);
        ButterKnife.inject(this, header);
        populateTextAndImage();
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
                int i = 3;
                Image image = WahlzeitModel.model.getImages().get(photo.getIdAsString()).getItems().get(i);
                Bitmap decodedImage = null;
                //get a smaller image in case there is no bigger
                while (image.isEmpty() && i >= 0) {
                    --i;
                    image = WahlzeitModel.model.getImages().get(photo.getIdAsString()).getItems().get(i);
                }
                if(!image.isEmpty()) {
                    byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
                    decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                }
                // create list object
                PhotoListItem photoItem = new PhotoListItem( photoId, photoPraise, photoStatus,
                                                    photoCreationTime, photoTags, photoName, decodedImage);
                photoListItems.add(photoItem);
            }
            photoListAdapter = new PhotoListAdapter(getActivity(), photoListItems);
            listViewPhotos.setAdapter(photoListAdapter);
            listViewPhotos.setOnItemClickListener(new ListItemClickListener());
            listViewPhotos.addHeaderView(header);
        }
    };

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0) {
                return; // header was clicked
            }
            PhotoListItem item = (PhotoListItem) photoListAdapter.getItem(position -1);
            selectedPhotoId = item.getId();
            createPopup();
        }
    }

    private class OptionListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            String selectedOption = ((AlertDialog)dialog).getListView().getAdapter().getItem(which).toString();
            switch (selectedOption.toLowerCase()) {
                case "edit":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    lauchEditActivity();
                case "tell":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    ((MainActivity)getActivity()).displayView(1);
                case "select":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                case "delete":
                    Log.d(getActivity().getTitle().toString(), selectedOption);


            }
        }
    }

    private void lauchEditActivity() {
//        EditPhotoActivity nextFragment = new EditPhotoActivity();
//        this.getFragmentManager().beginTransaction().replace(this.getId(), nextFragment).addToBackStack(null).commit();
        Intent intent = new Intent(getActivity(), EditPhotoActivity.class);
        intent.putExtra("selected_photo_id", selectedPhotoId);
        startActivity(intent);
    }

    private void createPopup() {
        String options[] = getActivity().getResources().getStringArray(R.array.photo_item_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.photo_item_options_title);
        builder.setItems(options, new OptionListener());
        builder.show();
    }

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
