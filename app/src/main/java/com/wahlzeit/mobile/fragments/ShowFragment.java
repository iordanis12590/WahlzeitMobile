package com.wahlzeit.mobile.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.activities.FlagActivity;
import com.wahlzeit.mobile.activities.MainActivity;
import com.wahlzeit.mobile.adapters.CardsDataAdapter;
import com.wahlzeit.mobile.listeners.CardStackEventListener;
import com.wahlzeit.mobile.model.CardModel;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.network.asyncTasks.GetFilteredPhotosTask;
import com.wahlzeit.mobile.network.asyncTasks.SkipPhotoTask;
import com.wenchao.cardstack.CardStack;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment class with a card stack that allows the user to view and rate photos one at a time
 */
public class ShowFragment extends Fragment implements WahlzeitFragment {

    View rootView;
    String displayedPhotoId;
    String filterTags;
    private CardsDataAdapter mCardAdapter;
    @InjectView(R.id.textview_done_show) TextView mTextViewDone;
    @InjectView(R.id.container) CardStack mCardStack;
    @InjectView(R.id.progress_bar_show) RelativeLayout progressBar;
    @InjectView(R.id.edittext_filter) EditText editTextFilter;

    public ShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show, container, false);
        ButterKnife.inject(this, rootView);
        registerEvents();
        setupDoneText();
        setupCardStack();
        setFilterText();
        new GetFilteredPhotosTask(getActivity()).execute();
        return rootView;
    }

    /**
     * Saves the tags of the editText to a class variable
     */
    private void setFilterText() {
        editTextFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(v);
                    filterTags = editTextFilter.getText().toString();
                    refreshCardStack();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     *
     * @param v
     */
    private void hideKeyboard(TextView v) {
        InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     *
     */
    private void registerEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(discardPhotoReceiver, new IntentFilter("discard_photo"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(populatePhotoCardsReceiver, new IntentFilter("populate_photo_card_stack"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cardTappedReceiver, new IntentFilter("tapped_top_of_card_stack"));
    }

    /**
     *
     */
    private void unregisterEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discardPhotoReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(populatePhotoCardsReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(cardTappedReceiver);
    }

    /**
     *
     */
    private void setupCardStack() {
        mCardStack.setContentResource(R.layout.fragment_show_card_content);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), mCardStack);
    }

    /**
     *
     */
    private void setupDoneText() {
        mTextViewDone.setVisibility(View.GONE);
        String doneText = getResources().getString(R.string.done_text);
        mTextViewDone.setText(doneText);
    }

    /**
     * Skipps the photo and adds the skipped photo to the model
     * @param photoId The photo to be skipped
     */
    private void skipCard(String photoId) {
        Photo photoToSkip = ModelManager.manager.getPhotoFromId(photoId);
        photoToSkip.setPraisingClientId(ModelManager.manager.getCurrentClient().getId());
        ModelManager.manager.setSkippedPhoto(photoToSkip.getIdAsString());
        new SkipPhotoTask(getActivity().getApplicationContext()).execute(photoToSkip);
    }

    /**
     * Creates a dialog when the user clicks on a card, and gives him the options flag & tell
     */
    private void createPopupOnCard() {
        String options[] = getActivity().getResources().getStringArray(R.array.card_item_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.photo_item_options_title);
        builder.setItems(options, new CardItemOptionListener());
        builder.show();
    }

    /**
     * A listener class to react to the option the user selected
     */
    private class CardItemOptionListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            String selectedOption = ((AlertDialog)dialog).getListView().getAdapter().getItem(which).toString();
            // get current photo id
            CardModel card = mCardAdapter.getItem(mCardStack.getCurrIndex());
            displayedPhotoId = card.getPhotoId();
            switch (selectedOption.toLowerCase()) {
                case "flag":
                    Log.d(getActivity().getTitle().toString(), selectedOption);
                    lauchFlagActivity();
                    break;
                case "tell":
                    launchTellFragment();
                    break;
            }
        }
    }

    /**
     * Launches the tell fragment and passes the relevant photo's id
     */
    private void launchTellFragment() {
        int tellFragmentPosition = 1;
        Fragment fragment = ((MainActivity)getActivity()).getFragmentView(tellFragmentPosition);
        Bundle args = new Bundle();
        args.putString("photoId", displayedPhotoId);
        ((MainActivity)getActivity()).displayFragmentView(fragment, tellFragmentPosition, args);
    }

    /**
     * Launches the flag activity and passes the relevant photo's id
     */
    private void lauchFlagActivity() {
        Intent launchFlagActivityIntent = new Intent(getActivity(), FlagActivity.class);
        launchFlagActivityIntent.putExtra("diplayed_photo_id", displayedPhotoId);
        startActivity(launchFlagActivityIntent);
    }

    /**
     * A receiver class to determine the actions to be performed when a card is discarded
     */
    private BroadcastReceiver discardPhotoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // get discarded card and skip
            CardModel card = mCardAdapter.getItem(mCardStack.getCurrIndex() - 1);
            skipCard(card.getPhotoId());
            // :) A hack to determine the card stack is left without cards
            if (mCardAdapter.getCount() == mCardStack.getCurrIndex()) {
                showDoneTextView(true);
            } else {
                showDoneTextView(false);
            }
        }
    };

    /**
     * A receiver class to react when the user tapps on a card
     */
    private BroadcastReceiver cardTappedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            createPopupOnCard();
        }
    };

    /**
     *
     */
    private BroadcastReceiver populatePhotoCardsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshCardStack();
        }
    };

    /**
     * Refreshes the card stack using all photos the model manager holds, except those the user has already rated.
     */
    private void refreshCardStack() {
        showDoneTextView(false);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), mCardStack);
        List<String> ratedPhotoIds = ModelManager.manager.getPraisedPhotoIds();
        List<String> skippedPhotoIds = ModelManager.manager.getSkippedPhotoIds();
        for(Photo photo: ModelManager.manager.getAllPhotos().values()) {
            String photoId = photo.getIdAsString();
            if(ratedPhotoIds.contains(photoId)) {
                continue;
            }
            if(!photoContainsTag(photo)) {
                continue;
            }
            if(!photo.getStatus().toLowerCase().equals("visible")) {
                continue;
            }
            Bitmap decodedImage = ModelManager.manager.getImageBitmapOfSize(photoId, 3);
            mCardAdapter.add(new CardModel(photoId, decodedImage));
        }
        if(mCardAdapter.getCount() == 0) {
            showDoneTextView(true);
        } else {

        }
        mCardStack.setAdapter(mCardAdapter);
        mCardStack.setListener(new CardStackEventListener(getActivity().getApplicationContext()));
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Uses the tags in the filter EditText, to determine whether a photo contains those tags or not
     * @param photo The photo to check whether it contains the tags or not
     * @return
     */
    private boolean photoContainsTag(Photo photo) {
        boolean result = false;
        if(filterTags == null || filterTags.toLowerCase().equals("filter")) {
            return true;
        }
        String photoTags = ModelManager.manager.getPhotoTagsAsString(photo);
        String[] filterTagsArray = ModelManager.manager.getTagsFromText(filterTags);
        for(String tag: filterTagsArray) {
            if(photoTags.contains(tag)) {
                return true;
            }
        }
        return result;
    }

    /**
     * Shows a placeholder informing the user that there are no photos left to rate
     * @param show Determines whether to show the text or not
     */
    private void showDoneTextView(boolean show) {
        if(show) {
            mTextViewDone.setVisibility(View.VISIBLE);
        } else {
            mTextViewDone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvents();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterEvents();
    }

}
