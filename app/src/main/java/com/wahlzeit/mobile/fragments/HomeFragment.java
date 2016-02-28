package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahlzeit.mobile.asyncTasks.GetImageFromUrlTask;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;

import org.json.JSONException;

public class HomeFragment extends Fragment {

    ImageView imageViewProfilePicture;
    TextView textViewName, textViewEmail, textViewGender;
    String textName, textEmail, textGender, userImageUrl;

    View rootView;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        textViewName = (TextView) rootView.findViewById(R.id.textViewNameValue);
        textViewEmail = (TextView) rootView.findViewById(R.id.textViewEmailValue);
        textViewGender = (TextView) rootView.findViewById(R.id.textViewGenderValue);
        imageViewProfilePicture = (ImageView) rootView.findViewById(R.id.imageView1);

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
