package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.asyncTasks.ListAllPhotosTask;

public class ShowFragment extends Fragment {

    ImageView randomImage;
    View rootView;

    public ShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_show, container, false);

        randomImage = (ImageView) rootView.findViewById(R.id.random_image);
        getListAllPhotosTask(randomImage).execute();

        return rootView;
    }

    public ListAllPhotosTask getListAllPhotosTask(ImageView randomImage) {
        return new ListAllPhotosTask(randomImage);
    }


}
