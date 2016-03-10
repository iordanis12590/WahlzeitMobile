package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.asyncTasks.ListAllPhotosTask;

public class ShowFragment extends Fragment {

    View rootView;

    public ShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_show, container, false);

//        randomImage = (ImageView) rootView.findViewById(R.id.random_image);
//        getListAllPhotosTask(randomImage).execute();



//        adapter.add(new CardModel("Title1", "Description", resources.getDrawable(R.drawable.picture1)));
//
//        mCardContainer.setAdapter(adapter);

        return rootView;
    }

    private ListAllPhotosTask getListAllPhotosTask(BaseAdapter adapter) {
//        return new ListAllPhotosTask(adapter, container);
        return null;
    }

}
