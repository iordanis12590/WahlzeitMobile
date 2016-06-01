package com.wahlzeit.mobile.fragments.upload;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.wahlzeit.mobile.ModelManager;
import com.wahlzeit.mobile.asyncTasks.UploadPhotoTask;
import com.wahlzeit.mobile.components.textswitcher.EditorActionListener;
import com.wahlzeit.mobile.components.textswitcher.FocusChangeListener;
import com.wahlzeit.mobile.components.textswitcher.TextViewClickListener;
import com.wahlzeit.mobile.fragments.WahlzeitFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UploadFragment extends Fragment implements WahlzeitFragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CHOOSE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final String TAG = "UploadFragment";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private String mCurrentPhotoPath;


    Photo photoToUpload;
    Bitmap mySelectedImage;
    View rootView;
    @InjectView(R.id.button_take_photo) Button takePhotoButton;
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
        if (rootView != null) {
            return rootView;
        }
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
            Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException e) {
                Log.e("Uplaod Fragment", "Error occurred while creating image file", e);
            }
            if(photoFile != null) {
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(captureImageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = new File (
                Environment.getExternalStorageDirectory()
                        + "/dcim/"
                        + R.string.album_name
        );
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "Calling onActivityResult after taking picture");
        if (resultCode == -1)  {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                setCapturedImage();
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

    private void setCapturedImage() {
        int targetW = uploadImageView.getWidth();
        int targetH = uploadImageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mySelectedImage = bitmap;
        uploadImageView.setImageBitmap(bitmap);
        uploadImageView.setVisibility(View.VISIBLE);
    }

    private void uploadPhoto() {
        photoToUpload = new Photo();
        photoToUpload.setOwnerId(ModelManager.manager.getCurrentClient().getId());
        photoToUpload.setOwnerEmailAddress(ModelManager.manager.getCurrentClient().getEmailAddress());
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
