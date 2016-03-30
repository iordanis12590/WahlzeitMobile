package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wahlzeit.mobile.R;

/**
 * Created by iordanis on 16/03/16.
 */
public class ModerateFragment extends Fragment {
    public ModerateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moderate, container, false);
    }

}
