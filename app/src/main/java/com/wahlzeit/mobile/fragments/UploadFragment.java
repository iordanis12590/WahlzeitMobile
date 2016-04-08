package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.asyncTasks.UploadPhotoTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UploadFragment extends Fragment implements WahlzeitFragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final String TAG = "UploadActivity";

    Photo photoToUpload;
    View rootView;
    @InjectView(R.id.button_upload_image) Button uploadButton;
    @InjectView(R.id.imageview_upload_image) ImageView uploadImageView;
    @InjectView(R.id.button_choose_photo) Button choosePhotoButton;
    @InjectView(R.id.textview_photo_tags) TextView textViewTags;
    @InjectView(R.id.edittext_tags) EditText editTextTags;

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
        View.OnClickListener onClickListener = new MyTextViewOnClickListener();
    }


    private void setupButtons() {
        uploadButton.setOnClickListener(new MyUploadButtonClickListener());
        choosePhotoButton.setOnClickListener(new MyChoosePhotoButtonClickListener());
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

    private class MyUploadButtonClickListener implements View.OnClickListener {

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
            Bitmap mySelectedImage = null;
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
            uploadPhoto(mySelectedImage);
        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }

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

    private void uploadPhoto(Bitmap image) {
        photoToUpload = new Photo();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String byteArrayString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        photoToUpload.setOwnerId(WahlzeitModel.model.getCurrentClient().getId());
        photoToUpload.setOwnerEmailAddress(WahlzeitModel.model.getCurrentClient().getEmailAddress());
        photoToUpload.setBlobImage(byteArrayString);

        new UploadPhotoTask(getActivity().getApplicationContext()).execute(photoToUpload);
    }

    private class MyTextViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switchToEditText(v);
        }
    }

    private void switchToEditText(View v) {
        ViewSwitcher switcher = (ViewSwitcher) v.getParent();
        EditText editText = (EditText) switcher.getNextView();;
        TextView textView = (TextView) v;
        switcher.showNext();
        String textViewText = textView.getText().toString();
        editText.setText(textViewText);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        showKeyboardAndCursor(editText);
    }

    private void showKeyboardAndCursor(EditText editText) {
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
}
