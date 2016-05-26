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
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.asyncTasks.UpdatePhotoTask;
import com.wahlzeit.mobile.components.textswitcher.EditorActionListener;
import com.wahlzeit.mobile.components.textswitcher.FocusChangeListener;
import com.wahlzeit.mobile.components.textswitcher.TextViewClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
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

    private void setupImageView() {
        Bitmap image = WahlzeitModel.model.getImageBitmapOfSize(photoToUpdate.getIdAsString(), 3);
        imageViewEdit.setImageBitmap(image);

    }

    private void getPhoto() {
        Intent intent = getIntent();
        String photoId = intent.getStringExtra("selected_photo_id");
        photoToUpdate = WahlzeitModel.model.getClientsPhotoFromId(photoId);

    }

    private void setupTagsText() {
        String tags = WahlzeitModel.model.getPhotoTagsAsString(photoToUpdate);
        textViewTags.setText(tags);
        View.OnClickListener onClickListener = new TextViewClickListener(this);
        View.OnFocusChangeListener onFocusChangeListener = new FocusChangeListener(this);
        TextView.OnEditorActionListener onEditorActionListener = new EditorActionListener(this);
        textViewTags.setOnClickListener(onClickListener);
        editTextTags.setOnFocusChangeListener(onFocusChangeListener);
        editTextTags.setOnEditorActionListener(onEditorActionListener);
    }

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

    private class UpdatePhotoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // update photo task call
            photoToUpdate.setTags(getTagsFromTextView());
            new UpdatePhotoTask(getApplicationContext()).execute(photoToUpdate);
        }
    }

}
