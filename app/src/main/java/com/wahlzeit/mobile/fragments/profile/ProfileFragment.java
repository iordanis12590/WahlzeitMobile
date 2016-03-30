package com.wahlzeit.mobile.fragments.profile;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.activities.BaseActivity;
import com.wahlzeit.mobile.asyncTasks.GetImageFromUrlTask;
import com.wahlzeit.mobile.fragments.WahlzeitFragment;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    boolean notify;
    ViewSwitcher viewSwitcher;
    ArrayAdapter<CharSequence> genderAdapter, languageAdapter;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(this, rootView);
        setupSpinners();
        setupSwitchers();
        setupUpdateButton();
        populateTextAndImage();
        return rootView;
    }

    private void populateTextAndImage() {
        try {
            if (WahlzeitModel.model.getCurrentClient() == null) throw new AssertionError("Client is null");

            userImageUrl = WahlzeitModel.model.getGoogleUserValue("picture");
            new GetImageFromUrlTask(this.getActivity(), imageViewProfilePicture).execute(userImageUrl);

            textName = WahlzeitModel.model.getGoogleUserValue("name");
            textViewName.setText(textName);

            textEmail = WahlzeitModel.model.getGoogleUserValue("email");
            textViewEmail.setText(textEmail);

            textGender = WahlzeitModel.model.getGoogleUserValue("gender");
            if (textGender == null || textGender.isEmpty()) throw new AssertionError("Gender fetched from google profile is nil");
            int myGenderPosition = getStringPositionInAdapter(genderAdapter, textGender);
            spinnerGender.setSelection(myGenderPosition);

            textLanguage = WahlzeitModel.model.getCurrentClient().getLanguage().toLowerCase();
            int myLanguagePositinon = getStringPositionInAdapter(languageAdapter, textLanguage);
            spinnerLanguage.setSelection(myLanguagePositinon);

            // TODO: get from current client
            switchNotify.setChecked(true);

        } catch(JSONException e) {
            e.printStackTrace();
        } catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    private void setupUpdateButton() {
        MyUpdateProfileClickListener myUpdateProfileClickListener = new MyUpdateProfileClickListener();
        updateButton.setOnClickListener(myUpdateProfileClickListener);
    }

    private void setupSwitchers() {
        View.OnClickListener onClickListener = new MyTextViewOnClickListener();
        View.OnFocusChangeListener onFocusChangeListener = new MyFocusChangeListener();
        TextView.OnEditorActionListener onEditorActionListener = new MyOnEditorActionListener();
        textViewName.setOnClickListener(onClickListener);
        editTextName.setOnFocusChangeListener(onFocusChangeListener);
        editTextName.setOnEditorActionListener(onEditorActionListener);
    }

    private void setupSpinners() {
        genderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender, R.layout.spinner_item);
        languageAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.language, R.layout.spinner_item);
        spinnerGender.setAdapter(genderAdapter);
        spinnerLanguage.setAdapter(languageAdapter);
    }

    private int getStringPositionInAdapter(ArrayAdapter<CharSequence> adapter, String genderValue) {
        for(int i=0; i<adapter.getCount(); i++){
            CharSequence item = adapter.getItem(i).toString().toLowerCase();
            if (item.equals(genderValue.toLowerCase())) {
                return i;
            }
        }
        return 0;
    }

    private class MyTextViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switchToEditText(v);
        }
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {
        public void onFocusChange(View v, boolean hasFocus){
            if(v instanceof EditText && !hasFocus) {
                hideKeyboardAndCursor(v);
                switchBackToTextView((EditText) v);
            }
        }
    }

    private class MyOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.i("Main", "Oh there was some action");
            hideKeyboardAndCursor(v);
            switchBackToTextView(v);
            return false;
        }

    }

    private class MyUpdateProfileClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String name = textViewName.getText().toString();
            String gender = spinnerGender.getSelectedItem().toString();
            String language = spinnerLanguage.getSelectedItem().toString();
            WahlzeitModel.model.getCurrentClient().setNickName(name);
            WahlzeitModel.model.getCurrentClient().setLanguage(language);
            ((BaseActivity)getActivity()).setLocale(language);

        }
    };

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

    private void switchBackToTextView(View v) {
        ViewSwitcher parentSwitcher = (ViewSwitcher) v.getParent();
        EditText editText = (EditText) v;
        TextView siblingTextView = (TextView) parentSwitcher.getChildAt(0);

        String editedText = editText.getText().toString();
        siblingTextView.setText(editedText);
        parentSwitcher.showPrevious();
    }

    private void hideKeyboardAndCursor(View v) {
        InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void showKeyboardAndCursor(EditText editText) {
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }



}
