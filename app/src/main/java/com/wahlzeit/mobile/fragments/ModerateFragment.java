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
import android.widget.ListView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.wahlzeit.mobile.adapters.PhotoCaseListAdapter;
import com.wahlzeit.mobile.model.PhotoCaseListItem;
import com.wahlzeit.mobile.network.CommunicationManager;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.network.asyncTasks.UpdatePhotoCaseTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment that displays a list of all photo cases
 * Created by iordanis on 16/03/16.
 */
public class ModerateFragment extends Fragment {
    View rootView;
    List<PhotoCaseListItem> photoCaseListItemList;
    PhotoCaseListAdapter photoCaseListAdapter;
    String selectedPhotoCaseId;
    PhotoCase selectedPhotoCase;
    @InjectView(R.id.list_photos_cases_moderate)ListView listViewPhotoCases;

    public ModerateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_moderate, container, false);
        ButterKnife.inject(this, rootView);
        registerEvents();
        CommunicationManager.manager.getListAllPhotoCasesTask(getActivity()).execute();
        return rootView;
    }

    /**
     * A receiver class for performing actions after receiving photo cases from the server
     */
    private BroadcastReceiver retrievedPhotoCases = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            populatePhotoCasesList();
        }
    };

    /**
     * Populates the list with the received photo cases
     */
    private void populatePhotoCasesList() {
        photoCaseListItemList = getPhotoCaseListItems();
        photoCaseListAdapter = new PhotoCaseListAdapter(getActivity(), photoCaseListItemList);
        listViewPhotoCases.setAdapter(photoCaseListAdapter);
        listViewPhotoCases.setOnItemClickListener(new PhotoCaseClickListener());
    }

    /**
     * Iterates through the received photo cases and returns a collection of photo case list items
     * @return
     */
    private List<PhotoCaseListItem> getPhotoCaseListItems() {
        List<PhotoCaseListItem> result = new ArrayList<PhotoCaseListItem>();
        for (PhotoCase photoCase: ModelManager.manager.getPhotoCaseCache().getItems()) {
            String photoCaseId = photoCase.getIdAsString();
            String photoCaseDescription = "Photo by " + photoCase.getPhotoOwnerName();
            String photoCaseReason = photoCase.getReason();
            String photoCaseTags = ModelManager.manager.getPhotoTagsAsString(photoCase.getPhoto());
            String photoCaseFlagger = photoCase.getFlagger();
            String photoCaseExplanation = photoCase.getExplanation();
            Bitmap photoCaseImage = ModelManager.manager.getImageBitmapOfSize(photoCase.getPhoto().getIdAsString(), 2);
            PhotoCaseListItem item = new PhotoCaseListItem(photoCaseId, photoCaseDescription, photoCaseTags, photoCaseFlagger, photoCaseReason, photoCaseExplanation, photoCaseImage);
            result.add(item);
        }
        return result;
    }

    /**
     * Registers for the relevant events
     */
    private void registerEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(retrievedPhotoCases, new IntentFilter("receive_photo_cases"));
    }

    /**
     *
     */
    private void unregisterEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(retrievedPhotoCases);
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

    /**
     * Listener to react on list item clicks
     */
    private class PhotoCaseClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PhotoCaseListItem item = (PhotoCaseListItem) photoCaseListAdapter.getItem(position);
            selectedPhotoCaseId = item.getId();
            selectedPhotoCase = ModelManager.manager.getPhotoCaseFromId(selectedPhotoCaseId);
            createPopup();
        }
    }

    /**
     * Creates a popup dialog to allow user choose action on a photo case: moderate, unflag
     */
    private void createPopup() {
        String options[] = getActivity().getResources().getStringArray(R.array.photo_case_item_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.photo_item_options_title);
        builder.setItems(options, new PhotoItemOptionListener());
        builder.show();
    }

    /**
     * Determines what actions are performed after the user chooses an option from the popup dialog
     */
    private class PhotoItemOptionListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            String selectedOption = ((AlertDialog)dialog).getListView().getAdapter().getItem(which).toString();
            switch (selectedOption.toLowerCase()) {
                case "moderate":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    selectedPhotoCase.getPhoto().setStatus("MODERATED");
                    break;
                case "unflag":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    selectedPhotoCase.getPhoto().setStatus("FLAGGED2");
                    break;
            }
            new UpdatePhotoCaseTask(getActivity().getApplicationContext()).execute(selectedPhotoCase);
        }
    }
}
