package com.wahlzeit.mobile.fragments.moderate;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by iordanis on 16/03/16.
 */
public class ModerateFragment extends Fragment {

    View rootView;
    List<PhotoCaseListItem> photoCaseListItemList;
    PhotoCaseListAdapter photoCaseListAdapter;
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

    private BroadcastReceiver retrievedPhotoCases = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            populatePhotoCasesList();
        }
    };

    private void populatePhotoCasesList() {
        photoCaseListItemList = getPhotoCaseListItems();
        photoCaseListAdapter = new PhotoCaseListAdapter(getActivity(), photoCaseListItemList);
        listViewPhotoCases.setAdapter(photoCaseListAdapter);
//        listViewPhotoCases.setOnClickListener();
    }

    private List<PhotoCaseListItem> getPhotoCaseListItems() {
        List<PhotoCaseListItem> result = new ArrayList<PhotoCaseListItem>();
        for (PhotoCase photoCase: WahlzeitModel.model.getPhotoCaseCache().getItems()) {
            String photoCaseId = photoCase.getIdAsString();
            String photoCaseDescription = "Photo by " + photoCase.getPhotoOwnerName();
            String photoCaseReason = photoCase.getReason();
            String photoCaseTags = WahlzeitModel.model.getPhotoTagsAsString(photoCase.getPhoto());
            String photoCaseFlagger = photoCase.getFlagger();
            String photoCaseExplanation = photoCase.getExplanation();
            Bitmap photoCaseImage = WahlzeitModel.model.getImageBitmapOfSize(photoCase.getPhoto().getIdAsString(), 2);
            PhotoCaseListItem item = new PhotoCaseListItem(photoCaseId, photoCaseDescription, photoCaseTags, photoCaseFlagger, photoCaseReason, photoCaseExplanation, photoCaseImage);
            result.add(item);
        }
        return result;
    }


    private void registerEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(retrievedPhotoCases, new IntentFilter("receive_photo_cases"));
    }

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

}
