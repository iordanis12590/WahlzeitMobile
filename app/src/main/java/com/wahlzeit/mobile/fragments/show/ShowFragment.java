package com.wahlzeit.mobile.fragments.show;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Image;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.Photo;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;
import com.wenchao.cardstack.CardStack;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShowFragment extends Fragment {

    View rootView;
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
    }

    private void unregisterEvents() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discardPhotoReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(populatePhotoCardsReceiver);
    }

    private void setupCardStack() {
        mCardStack.setContentResource(R.layout.card_content);
//        mCardStack.setStackMargin(20);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext());
    }

    private void setupDoneText() {
        mTextViewDone.setVisibility(View.GONE);
        String doneText = getResources().getString(R.string.done_text_en);
        mTextViewDone.setText(doneText);
    }

    private BroadcastReceiver discardPhotoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // :) A hack to determine the card stack is left without cards
            if (mCardAdapter.getCount() == mCardStack.getCurrIndex()) {
                mTextViewDone.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver populatePhotoCardsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            for(Photo photo: WahlzeitModel.model.getPhotoCache().getItems()) {
                String photoId = photo.getIdAsString();
                Image image = WahlzeitModel.model.getImages().get(photoId).getItems().get(3);
                byte[] imageAsBytes = Base64.decode(image.getImageData().getBytes(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
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
