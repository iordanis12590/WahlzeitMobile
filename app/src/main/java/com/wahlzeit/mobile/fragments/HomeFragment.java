package com.wahlzeit.mobile.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.activities.EditPhotoActivity;
import com.wahlzeit.mobile.activities.MainActivity;
import com.wahlzeit.mobile.adapters.PhotoListAdapter;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.model.PhotoListItem;
import com.wahlzeit.mobile.network.asyncTasks.DeletePhotoTask;
import com.wahlzeit.mobile.network.asyncTasks.GetClientsPhotoTask;
import com.wahlzeit.mobile.network.asyncTasks.GetImageFromUrlTask;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A Fragment displaying a list with all the user's uploaded photos, and information about the user.
 */
public class HomeFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener, WahlzeitFragment {

    View rootView;
    @InjectView(R.id.textview_name_value_home) TextView textViewName;
    @InjectView(R.id.textview_email_value_home) TextView textViewEmail;
    @InjectView(R.id.textview_gender_value_home) TextView textViewGender;
    @InjectView(R.id.imageview_profile_pic_home) ImageView imageViewProfilePicture;
    String textName, textEmail, textGender, userImageUrl;
    String selectedPhotoId;
    ListView listViewPhotos;
    PhotoListAdapter photoListAdapter;
    View header;
    private SwipyRefreshLayout swipyRefreshLayout;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        listViewPhotos = (ListView) rootView.findViewById(R.id.list_photos_home);
        photoListAdapter = new PhotoListAdapter(getActivity(), new ArrayList<PhotoListItem>());
        listViewPhotos.setAdapter(photoListAdapter);
        listViewPhotos.setOnItemClickListener(new ListItemClickListener());
        swipyRefreshLayout = (SwipyRefreshLayout) rootView.findViewById(R.id.swipy_refresh_layout_home);
        swipyRefreshLayout.setOnRefreshListener(this);
        registerEvents();
        new GetClientsPhotoTask(getActivity()).execute();
        header = inflater.inflate(R.layout.fragment_home_list_header, null ,false);
        ButterKnife.inject(this, header);
        listViewPhotos.addHeaderView(header);
        populateHeader();
        return rootView;
    }

    /**
     * Sets the values of the list header: name, gender, email, profile pic
     */
    private void populateHeader() {
        try {
            userImageUrl = ModelManager.manager.getGoogleUserValue("picture");
            new GetImageFromUrlTask(this.getActivity(), imageViewProfilePicture).execute(userImageUrl);
            textName = ModelManager.manager.getCurrentClient().getNickName(); //ModelManager.manager.getGoogleUserValue("name");
            textViewName.setText(textName);
            textEmail = ModelManager.manager.getCurrentClient().getEmailAddress().getValue(); //ModelManager.manager.getGoogleUserValue("email");
            textViewEmail.setText(textEmail);
            textGender = ModelManager.manager.getCurrentClient().getGender().toLowerCase(); //ModelManager.manager.getGoogleUserValue("gender");
            textViewGender.setText(textGender);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A receiver class triggered when and images are fetched from the server
     */
    private BroadcastReceiver retrievedPhotos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            populatePhotoList();
        }
    };

    /**
     * Creates the list containing the user's uploaded photos
     */
    private void populatePhotoList() {
        DateFormat dateFormater = new SimpleDateFormat("MMM d, yyyy");
        for(Photo photo: ModelManager.manager.getClientsPhotos().values()) {
            // get photo values
            String photoId = photo.getId().getStringValue();
            if(!photoListAdapter.containsPhotoListItem(photoId)) {
                String photoPraise = photo.getPraise().toString();
                String photoStatus = photo.getStatus().toLowerCase();
                // Date
                String photoCreationTime = dateFormater.format(photo.getCreationTime().longValue());
                // tags
                String photoTags = ModelManager.manager.getPhotoTagsAsString(photo);
                Bitmap decodedImage = ModelManager.manager.getImageBitmapOfSize(photo.getIdAsString(), 3);
                // create list object
                PhotoListItem photoItem = new PhotoListItem(photoId, photoPraise, photoStatus,
                        photoCreationTime, photoTags, decodedImage);
                photoListAdapter.addPhotoListItem(photoItem);
            }
        }
        photoListAdapter.notifyDataSetChanged();
        swipyRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("MainActivity", "Refresh triggered at "
                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
        new GetClientsPhotoTask(getActivity()).execute();
    }

    /**
     * A class to react on tapping list items
     */
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

    /**
     * A class to handle the action to be performed after a user selected an option from
     * the popup dialog.
     */
    private class PhotoItemOptionListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            String selectedOption = ((AlertDialog)dialog).getListView().getAdapter().getItem(which).toString();
            switch (selectedOption.toLowerCase()) {
                case "edit":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    lauchEditActivity();
                    break;
                case "tell":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    int tellFragmentPosition = 1;
                    Fragment fragment = ((MainActivity)getActivity()).getFragmentView(tellFragmentPosition);
                    Bundle args = new Bundle();
                    args.putString("photoId", selectedPhotoId);
                    ((MainActivity)getActivity()).displayFragmentView(fragment, tellFragmentPosition, args);
                    break;
                case "select":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    break;
                case "delete":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    Photo photoToDelete = ModelManager.manager.getClientsPhotoFromId(selectedPhotoId);
                    new DeletePhotoTask(getActivity().getApplicationContext()).execute(photoToDelete);
                    break;
            }
        }
    }

    /**
     * Launches edit photo activity and passes the selected photo's id
     */
    private void lauchEditActivity() {
        Intent intent = new Intent(getActivity(), EditPhotoActivity.class);
        intent.putExtra("selected_photo_id", selectedPhotoId);
        startActivity(intent);
    }

    /**
     * Creates a popup dialog, with the option that can be performed for each photo
     */
    private void createPopup() {
        String options[] = getActivity().getResources().getStringArray(R.array.photo_item_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.photo_item_options_title);
        builder.setItems(options, new PhotoItemOptionListener());
        builder.show();
    }

    /**
     * Registers the fragment for the relevant events
     */
    private void registerEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(retrievedPhotos, new IntentFilter("populate_user_photos"));
    }

    /**
     * Unregisters the fragment from
     */
    private void unregisterEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(retrievedPhotos);
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
