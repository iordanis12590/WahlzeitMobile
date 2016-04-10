package com.wahlzeit.mobile.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.components.textswitcher.EditorActionListener;
import com.wahlzeit.mobile.components.textswitcher.FocusChangeListener;
import com.wahlzeit.mobile.components.textswitcher.TextViewClickListener;

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
        setupTagsText();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupImageView() {
        Bitmap image = WahlzeitModel.model.getImageBitmapOfSize(photoToUpdate.getIdAsString(), 3);
        imageViewEdit.setImageBitmap(image);

    }

    private void getPhoto() {
        Intent intent = getIntent();
        String photoId = intent.getStringExtra("selected_photo_id");
        photoToUpdate = WahlzeitModel.model.getPhotoFromId(photoId);

    }

    private void setupTagsText() {
//        String tags = photoToUpdate.getTags().getTags().toArray().toString();
        View.OnClickListener onClickListener = new TextViewClickListener(this);
        View.OnFocusChangeListener onFocusChangeListener = new FocusChangeListener(this);
        TextView.OnEditorActionListener onEditorActionListener = new EditorActionListener(this);
        textViewTags.setOnClickListener(onClickListener);
        editTextTags.setOnFocusChangeListener(onFocusChangeListener);
        editTextTags.setOnEditorActionListener(onEditorActionListener);
    }

    private class UpdatePhotoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // update photo task call
        }
    }

}
