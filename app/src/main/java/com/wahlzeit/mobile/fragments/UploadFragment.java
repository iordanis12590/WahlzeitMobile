package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Tags;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.asyncTasks.UploadPhotoTask;
import com.wahlzeit.mobile.components.textswitcher.EditorActionListener;
import com.wahlzeit.mobile.components.textswitcher.FocusChangeListener;
import com.wahlzeit.mobile.components.textswitcher.TextViewClickListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UploadFragment extends Fragment implements WahlzeitFragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final String TAG = "UploadActivity";

    Photo photoToUpload;
    Bitmap mySelectedImage;
    View rootView;
    @InjectView(R.id.button_upload_image) Button takePhotoButton;
    @InjectView(R.id.imageview_upload_image) ImageView uploadImageView;
    @InjectView(R.id.button_choose_photo) Button choosePhotoButton;
    @InjectView(R.id.textview_tags_upload) TextView textViewTags;
    @InjectView(R.id.edittext_tags_upload) EditText editTextTags;
    @InjectView(R.id.button_upload_photo) Button uploadPhotoButton;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload, container, false);
        ButterKnife.inject(this, rootView);
        setupButtons();
        setupTagsText();
        return rootView;
    }

    private void setupTagsText() {
        View.OnClickListener onClickListener = new TextViewClickListener(getActivity());
        View.OnFocusChangeListener onFocusChangeListener = new FocusChangeListener(getActivity());
        TextView.OnEditorActionListener onEditorActionListener = new EditorActionListener(getActivity());
        textViewTags.setOnClickListener(onClickListener);
        editTextTags.setOnFocusChangeListener(onFocusChangeListener);
        editTextTags.setOnEditorActionListener(onEditorActionListener);
    }


    private void setupButtons() {
        takePhotoButton.setOnClickListener(new MyTakePhotoButtonClickListener());
        choosePhotoButton.setOnClickListener(new MyChoosePhotoButtonClickListener());
        uploadPhotoButton.setOnClickListener(new MyUploadPhotoButtonClickListener());
    }


    private class MyChoosePhotoButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private class MyTakePhotoButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "Calling onActivityResult after taking picture");
        if (resultCode == -1)  {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                mySelectedImage = (Bitmap) data.getExtras().get("data");
                Log.v(TAG, "set bitmap photo into ImageView");
                uploadImageView.setImageBitmap(mySelectedImage);
                uploadImageView.setVisibility(View.VISIBLE);

            }
            if (requestCode == CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mySelectedImage = BitmapFactory.decodeStream(imageStream);
                uploadImageView.setImageBitmap(mySelectedImage);
            }
        }
    }

    @Override
    public void onStart() { super.onStart(); }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void uploadPhoto() {
        photoToUpload = new Photo();
        photoToUpload.setOwnerId(WahlzeitModel.model.getCurrentClient().getId());
        photoToUpload.setOwnerEmailAddress(WahlzeitModel.model.getCurrentClient().getEmailAddress());
        photoToUpload.setBlobImage(getCompressedSelectedImage());
        photoToUpload.setTags(getTagsFromTextView());
        new UploadPhotoTask(getActivity().getApplicationContext()).execute(photoToUpload);
    }

    private Tags getTagsFromTextView() {
        Tags result = new Tags();
        String tagsText = textViewTags.getText().toString();
        String tagsPlaceholder = getResources().getString(R.string.tags_placeholder);
        if(tagsText.equals(tagsPlaceholder)) {
            //do nothing
            Log.i(getActivity().getTitle().toString(), "do nothing");
        } else {
            String[] tagsArray = tagsText.split(",");
            List<String> tagsList = new ArrayList<String>(Arrays.asList(tagsArray));
            result.setTags(tagsList);
        }
        return result;
    }

    private String getCompressedSelectedImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mySelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String byteArrayString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return byteArrayString;
    }

    private class MyUploadPhotoButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mySelectedImage == null) {
                Toast.makeText(getActivity(), "Please choose a photo from your gallery or take a photo with your camera", Toast.LENGTH_LONG).show();
            } else {
                uploadPhoto();
            }
        }
    }
}
