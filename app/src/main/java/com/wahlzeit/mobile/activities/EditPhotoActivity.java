package com.wahlzeit.mobile.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Tags;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.network.asyncTasks.UpdatePhotoTask;
import com.wahlzeit.mobile.listeners.EditorActionListener;
import com.wahlzeit.mobile.listeners.FocusChangeListener;
import com.wahlzeit.mobile.listeners.TextViewClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A new activity launched when the user chooses to edit a photo of his
 * Created by iordanis on 09/04/16.
 */
public class EditPhotoActivity extends BaseActivity {

    @InjectView(R.id.imageview_image_edit) ImageView imageViewEdit;
    @InjectView(R.id.textview_tags_edit) TextView textViewTags;
    @InjectView(R.id.edittext_tags_edit) EditText editTextTags;
    @InjectView(R.id.switch_visibility_edit) Switch switchVisibility;
    @InjectView(R.id.button_update_photo) Button buttonUpdatePhoto;
    Photo photoToUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);
        getPhoto();
        setupImageView();
        setupSwitch();
        setupTagsText();
        buttonUpdatePhoto.setOnClickListener(new UpdatePhotoButtonListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Sets up the visibility switch
     */
    private void setupSwitch() {
        switchVisibility.setChecked(photoToUpdate.getVisible());
        switchVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                photoToUpdate.setVisible(!photoToUpdate.getVisible());
                photoToUpdate.setStatus(photoToUpdate.getVisible() ? "VISIBLE" : "INVISIBLE");
            }
        });
    }

    /**
     * Sets the image of the selected photo
     */
    private void setupImageView() {
        Bitmap image = ModelManager.manager.getImageBitmapOfSize(photoToUpdate.getIdAsString(), 3);
        imageViewEdit.setImageBitmap(image);

    }

    /**
     * Retreives the photo to be updated by extracting the selected photo is from the intent
     */
    private void getPhoto() {
        Intent intent = getIntent();
        String photoId = intent.getStringExtra("selected_photo_id");
        photoToUpdate = ModelManager.manager.getClientsPhotoFromId(photoId);

    }

    /**
     * Sets the tags of the photo in the text view and allocates the click listeners
     */
    private void setupTagsText() {
        String tags = ModelManager.manager.getPhotoTagsAsString(photoToUpdate);
        textViewTags.setText(tags);
        View.OnClickListener onClickListener = new TextViewClickListener(this);
        View.OnFocusChangeListener onFocusChangeListener = new FocusChangeListener(this);
        TextView.OnEditorActionListener onEditorActionListener = new EditorActionListener(this);
        textViewTags.setOnClickListener(onClickListener);
        editTextTags.setOnFocusChangeListener(onFocusChangeListener);
        editTextTags.setOnEditorActionListener(onEditorActionListener);
    }

    /**
     * Retrieves the tags diplayed
     * @return
     */
    private Tags getTagsFromTextView() {
        Tags result = new Tags();
        String tagsText = textViewTags.getText().toString();
        String tagsPlaceholder = getResources().getString(R.string.tags_placeholder);
        if(tagsText.equals(tagsPlaceholder)) {
            //do nothing
            Log.i(this.getTitle().toString(), "do nothing");
        } else {
            String[] tagsArray = tagsText.split(",");
            List<String> tagsList = new ArrayList<String>(Arrays.asList(tagsArray));
            result.setTags(tagsList);
        }
        return result;
    }

    /**
     * Updates the photo when the update button is clicked
     */
    private class UpdatePhotoButtonListener implements View.OnClickListener {
        /**
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // update photo task call
            photoToUpdate.setTags(getTagsFromTextView());
            new UpdatePhotoTask(getApplicationContext()).execute(photoToUpdate);
        }
    }

}
