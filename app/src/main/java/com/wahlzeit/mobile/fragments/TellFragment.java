package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TellFragment extends Fragment implements WahlzeitFragment {

    View rootView;
    @InjectView(R.id.edittext_address_tell) EditText editTextAddress;
    @InjectView(R.id.edittext_subject_tell) EditText editTextSubject;
    @InjectView(R.id.edittext_message_tell) EditText editTextMessage;
    @InjectView(R.id.button_tell_about_photo) Button buttonTell;
    Photo selectedPhoto;

    public TellFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tell, container, false);
        ButterKnife.inject(this, rootView);
        buttonTell.setOnClickListener(new TellClickListener());
        return rootView;
    }

    public Photo getSelectedPhoto() {
        return selectedPhoto;
    }

    public void setSelectedPhoto(Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    private class TellClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String address = editTextAddress.getText().toString();
            String subject = editTextSubject.getText().toString();
            String message = editTextMessage.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
