package com.wahlzeit.mobile.fragments.show;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wahlzeit.mobile.activities.FlagActivity;
import com.wahlzeit.mobile.asyncTasks.SkipPhotoTask;
import com.wahlzeit.mobile.fragments.WahlzeitFragment;
import com.wenchao.cardstack.CardStack;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShowFragment extends Fragment implements WahlzeitFragment {

    View rootView;
    String displayedPhotoId;
    private CardsDataAdapter mCardAdapter;
    @InjectView(R.id.textview_done_show) TextView mTextViewDone;
    @InjectView(R.id.container) CardStack mCardStack;

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
        CommunicationManager.manager.getListAllPhotosTask(getActivity()).execute();
        return rootView;
    }

    private void registerEvents() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(discardPhotoReceiver, new IntentFilter("discard_photo"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(populatePhotoCardsReceiver, new IntentFilter("populate_photo_card_stack"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cardTappedReceiver, new IntentFilter("tapped_top_of_card_stack"));
    }

    private void unregisterEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discardPhotoReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(populatePhotoCardsReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(cardTappedReceiver);
    }

    private void setupCardStack() {
        mCardStack.setContentResource(R.layout.fragment_show_card_content);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext(), mCardStack);
    }

    private void setupDoneText() {
        mTextViewDone.setVisibility(View.GONE);
        String doneText = getResources().getString(R.string.done_text);
        mTextViewDone.setText(doneText);
    }

    private void skipCard(String photoId) {
        Photo photoToSkip = WahlzeitModel.model.getPhotoFromId(photoId);
        photoToSkip.setPraisingClientId(WahlzeitModel.model.getCurrentClient().getId());
        new SkipPhotoTask(getActivity().getApplicationContext()).execute(photoToSkip);
    }

    private void createPopupOnCard() {
        String options[] = getActivity().getResources().getStringArray(R.array.card_item_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.photo_item_options_title);
        builder.setItems(options, new CardItemOptionListener());
        builder.show();
    }

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
                case "mail owner":
                    break;
                case "tell":
                    break;
            }
        }
    }

    private void lauchFlagActivity() {
        Intent launchFlagActivityIntent = new Intent(getActivity(), FlagActivity.class);
        launchFlagActivityIntent.putExtra("diplayed_photo_id", displayedPhotoId);
        startActivity(launchFlagActivityIntent);
    }

    private BroadcastReceiver discardPhotoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // get discarded card and skip
            CardModel card = mCardAdapter.getItem(mCardStack.getCurrIndex() - 1);
            skipCard(card.getPhotoId());
            // :) A hack to determine the card stack is left without cards
            if (mCardAdapter.getCount() == mCardStack.getCurrIndex()) {
                mTextViewDone.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver cardTappedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            createPopupOnCard();
        }
    };

    private BroadcastReceiver populatePhotoCardsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            for(Photo photo: WahlzeitModel.model.getPhotoCache().getItems()) {
                String photoId = photo.getIdAsString();
                Bitmap decodedImage = WahlzeitModel.model.getImageBitmapOfSize(photoId, 3);
                mCardAdapter.add(new CardModel(photoId, decodedImage));
            }
            mCardStack.setAdapter(mCardAdapter);
            mCardStack.setListener(new CardStackEventListener(getActivity().getApplicationContext()));
        }
    };

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
