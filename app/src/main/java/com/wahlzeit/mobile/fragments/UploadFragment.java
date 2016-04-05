package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload, container, false);
        ButterKnife.inject(this, rootView);
        uploadButton.setOnClickListener(new MyUploadButtonClickListener());
        choosePhotoButton.setOnClickListener(new MyChoosePhotoButtonClickListener());
        return rootView;
    }

    private class MyChoosePhotoButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            choosePhoto();
        }
    }


    private class MyUploadButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            uploadPhoto();
            takePhoto();
        }

    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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

//        uploadPhotoTask.cancel(true);
//        AsyncTask.Status status = uploadPhotoTask.getStatus();
//        if(uploadPhotoTask.getStatus() == AsyncTask.Status.FINISHED ){
//            uploadPhotoTask.cancel(true);
//
//            return;
//        }

        new UploadPhotoTask().execute();
    }


    AsyncTask<Void,Void,String> uploadPhotoTask = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(null);
            try {

                WahlzeitApi.Photos.Upload uploadCommand = wahlzeitServiceHandle.photos().upload(photoToUpload);
                Photo photo = uploadCommand.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Upload Complete";
        }

        protected void onPostExecute(String result) {
//            Log.v(TAG, "Inside doInBackground - onPostExecute()");
//            Context context = getActivity().getApplicationContext();
//            CharSequence text = result;
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
        }
    };

    private class UploadPhotoTask extends AsyncTask<Photo, Void, String> {

        @Override
        protected String doInBackground(Photo... photo) {
            WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(null);
            try {

                WahlzeitApi.Photos.Upload uploadCommand = wahlzeitServiceHandle.photos().upload(photoToUpload);
                Photo myPhoto = uploadCommand.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Upload Complete";
        }

        protected void onPostExecute(String result) {
            Log.v(TAG, "Inside doInBackground - onPostExecute()");
            Context context = getActivity().getApplicationContext();
            CharSequence text = result;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
