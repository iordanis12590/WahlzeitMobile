package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.asyncTasks.GetImageFromUrlTask;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment {

    @InjectView(R.id.textViewNameValue) TextView textViewName;
    @InjectView(R.id.textViewEmailValue) TextView textViewEmail;
    @InjectView(R.id.textViewGenderValue) TextView textViewGender;
    @InjectView(R.id.imageView1) ImageView imageViewProfilePicture;
    String textName, textEmail, textGender, userImageUrl;

    View rootView;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, rootView);
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


            String id = WahlzeitModel.model.getGoogleUserValue("id");


        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

}
