package com.wahlzeit.mobile.fragments.tell;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.fragments.WahlzeitFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TellFragment extends Fragment implements WahlzeitFragment {

    View rootView;
    @InjectView(R.id.imageview_image_tell) ImageView imageViewTell;
    @InjectView(R.id.edittext_address_tell) EditText editTextAddress;
    @InjectView(R.id.edittext_subject_tell) EditText editTextSubject;
    @InjectView(R.id.edittext_message_tell) EditText editTextMessage;
    @InjectView(R.id.button_tell_about_photo) Button buttonTell;
    Photo selectedPhoto;
    Bitmap selectedPhotoBitmap;

    public TellFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_tell, container, false);
        ButterKnife.inject(this, rootView);
        getArgsFromBundle();
        setSelectedImage();
        buttonTell.setOnClickListener(new TellClickListener());
        return rootView;
    }

    private void setSelectedImage() {
        if(selectedPhoto != null) {
            selectedPhotoBitmap = WahlzeitModel.model.getImageBitmapOfSize(selectedPhoto.getIdAsString(), 3);
            imageViewTell.setImageBitmap(selectedPhotoBitmap);
        }
    }

    private void getArgsFromBundle() {
        Bundle args = getArguments();
        if(args != null) {
            String photoId = args.getString("photoId");
            selectedPhoto = WahlzeitModel.model.getPhotoFromId(photoId);
            if (selectedPhoto == null) {
                selectedPhoto = WahlzeitModel.model.getClientsPhotoFromId(photoId);
            }
        }
    }

    private class TellClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String address = editTextAddress.getText().toString();
            String subject = editTextSubject.getText().toString();
            String message = editTextMessage.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            String pathOfBitmap = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), selectedPhotoBitmap,"title", null);
            Uri bmpUri = Uri.parse(pathOfBitmap);
            emailIntent.setType("message/rfc822/image/png");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
