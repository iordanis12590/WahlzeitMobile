package com.wahlzeit.mobile.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.PhotoCase;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.network.asyncTasks.CreatePhotoCaseTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An independent activity launched when the user chooses to flag a photo
 * Created by iordanis on 14/04/16.
 */
public class FlagActivity extends BaseActivity {
    PhotoCase photoCase;
    Photo photoToFlag;
    ArrayAdapter<CharSequence> flagReasonsStrings;
    String userEmailAddress, reason, explanation;
    @InjectView(R.id.imageview_image_flag) ImageView imageViewFlag;
    @InjectView(R.id.edittext_subject_flag) EditText editTextAddress;
    @InjectView(R.id.edittext_explanation_flag) EditText editTextExplanation;
    @InjectView(R.id.spinner_reason_flag) Spinner spinnerFlagReasons;
    @InjectView(R.id.button_flag_photo) Button buttonFlagPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag);
        ButterKnife.inject(this);
        getPhotoToFlag();
        setPhotoToFlag();
        setEmailAddress();
        buttonFlagPhoto.setOnClickListener(new FlagPhotoButtonListener());
        setupReasonSpinner();
    }

    /**
     * Set the user's email address
     */
    private void setEmailAddress() {
        userEmailAddress = ModelManager.manager.getCurrentClient().getEmailAddress().getValue();
        editTextAddress.setText(userEmailAddress);
    }

    /**
     * Set up the dropdown box which contains all flag reasons
     */
    private void setupReasonSpinner() {
        flagReasonsStrings = ArrayAdapter.createFromResource(this, R.array.flag_reason, R.layout.spinner_item);
        spinnerFlagReasons.setAdapter(flagReasonsStrings);
    }

    /**
     * Retrieves the photo to be flagged by extracting the selected photo is from the intent
     */
    private void getPhotoToFlag() {
        Intent intent = getIntent();
        String photoId = intent.getStringExtra("diplayed_photo_id");
        photoToFlag = ModelManager.manager.getPhotoFromId(photoId);
    }

    /**
     * Sets the image of the flagged photo
     */
    private void setPhotoToFlag() {
        if(photoToFlag != null) {
            Bitmap photoToFlagBitmap = ModelManager.manager.getImageBitmapOfSize(photoToFlag.getIdAsString(), 3);
            imageViewFlag.setImageBitmap(photoToFlagBitmap);
        }
    }

    /**
     * Creates a new photo case when the flag button is clicked
     */
    private class FlagPhotoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            getPhotoCaseDetails();
            setupPhotoCase();
        }
    }

    /**
     * Sets up a new photo case and uploads it to the server
     */
    private void setupPhotoCase() {
        if (photoToFlag != null) {
            photoCase = new PhotoCase();
            photoCase.setFlagger(userEmailAddress);
            photoCase.setPhoto(photoToFlag);
            photoCase.setReason(reason.toUpperCase());
            new CreatePhotoCaseTask(this).execute(photoCase);
        } else {
            CharSequence text = "Please select a photo to flag and explain the reason of flagging";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }

    }

    /**
     * Retrieves the details of the photo case from the UI
     */
    private void getPhotoCaseDetails() {
        userEmailAddress = editTextAddress.getText().toString();
        explanation = editTextExplanation.getText().toString();
        reason = spinnerFlagReasons.getSelectedItem().toString();
    }
}
