package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.activities.BaseActivity;
import com.wahlzeit.mobile.listeners.EditorActionListener;
import com.wahlzeit.mobile.listeners.FocusChangeListener;
import com.wahlzeit.mobile.listeners.TextViewClickListener;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.network.asyncTasks.GetImageFromUrlTask;
import com.wahlzeit.mobile.network.asyncTasks.UpdateProfileTask;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A framgment class displaying a user's profile details, and allowing him to edit certain fields
 */
public class ProfileFragment extends Fragment implements WahlzeitFragment {

    View rootView;
    @InjectView(R.id.textview_name_value_profile) TextView textViewName;
    @InjectView(R.id.edittext_name_value_profile) EditText editTextName;
    @InjectView(R.id.textview_email_value_profile) TextView textViewEmail;
    @InjectView(R.id.imageview_profile_pic_profile) ImageView imageViewProfilePicture;
    @InjectView(R.id.switch_notify_value_profile) Switch switchNotify;
    @InjectView(R.id.spinner_gender) Spinner spinnerGender;
    @InjectView(R.id.spinner_language) Spinner spinnerLanguage;
    @InjectView(R.id.button_update_profile) Button updateButton;
    String textName, textEmail, textGender, textLanguage, userImageUrl;
    boolean notifyAboutPraise;
    ArrayAdapter<CharSequence> genderAdapter, languageAdapter;

    /**
     * default constructor
     */
    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(this, rootView);
        setupSpinners();
        setupSwitchers();
        setupUpdateButton();
        populateTextAndImage();
        return rootView;
    }

    /**
     * Populates profile fields
     */
    private void populateTextAndImage() {
        try {
            if (ModelManager.manager.getCurrentClient() == null) throw new AssertionError("Client is null");

            userImageUrl = ModelManager.manager.getGoogleUserValue("picture");
            new GetImageFromUrlTask(this.getActivity(), imageViewProfilePicture).execute(userImageUrl);

            textName = ModelManager.manager.getGoogleUserValue("name");
            textViewName.setText(textName);

            textEmail = ModelManager.manager.getGoogleUserValue("email");
            textViewEmail.setText(textEmail);

            textGender = ModelManager.manager.getGoogleUserValue("gender");
            if (textGender == null || textGender.isEmpty()) throw new AssertionError("Gender fetched from google profile is nil");
            int myGenderPosition = genderAdapter.getPosition(textGender); //getStringPositionInAdapter(genderAdapter, textGender);
            spinnerGender.setSelection(myGenderPosition);

            textLanguage = ModelManager.manager.getCurrentClient().getLanguage().toLowerCase();
            int myLanguagePositinon = languageAdapter.getPosition(textLanguage);//getStringPositionInAdapter(languageAdapter, textLanguage);
            spinnerLanguage.setSelection(myLanguagePositinon);

            // TODO: get from current client
            notifyAboutPraise = ModelManager.manager.getCurrentClient().getNotifyAboutPraise();
            switchNotify.setChecked(true);

        } catch(JSONException e) {
            e.printStackTrace();
        } catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    /**
     *
     */
    private void setupUpdateButton() {
        MyUpdateProfileClickListener myUpdateProfileClickListener = new MyUpdateProfileClickListener();
        updateButton.setOnClickListener(myUpdateProfileClickListener);
    }

    /**
     *
     */
    private void setupSwitchers() {
        View.OnClickListener onClickListener = new TextViewClickListener(getActivity());
        View.OnFocusChangeListener onFocusChangeListener = new FocusChangeListener(getActivity());
        TextView.OnEditorActionListener onEditorActionListener = new EditorActionListener(getActivity());
        textViewName.setOnClickListener(onClickListener);
        editTextName.setOnFocusChangeListener(onFocusChangeListener);
        editTextName.setOnEditorActionListener(onEditorActionListener);
    }

    /**
     *
     */
    private void setupSpinners() {
        genderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender, R.layout.spinner_item);
        languageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.language, R.layout.spinner_item);
        spinnerGender.setAdapter(genderAdapter);
        spinnerLanguage.setAdapter(languageAdapter);
    }

    /**
     *
     *
     */
    private class MyUpdateProfileClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String name = textViewName.getText().toString();
            String gender = spinnerGender.getSelectedItem().toString().toUpperCase();
            String language = spinnerLanguage.getSelectedItem().toString().toUpperCase();
            boolean notify = switchNotify.isChecked();
            ModelManager.manager.getCurrentClient().setNickName(name);
            ModelManager.manager.getCurrentClient().setLanguage(language);
            ModelManager.manager.getCurrentClient().setGender(gender);
            ModelManager.manager.getCurrentClient().setNotifyAboutPraise(notify);
            ((BaseActivity) getActivity()).setLocale(language);
            new UpdateProfileTask(getActivity()).execute();
        }
    };

}
