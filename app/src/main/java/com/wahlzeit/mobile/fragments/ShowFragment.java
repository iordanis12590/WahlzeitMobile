package com.wahlzeit.mobile.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.asyncTasks.ListAllPhotosTask;
import com.wenchao.cardstack.CardStack;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShowFragment extends Fragment {

    View rootView;
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    private int counter;
    @InjectView(R.id.textview_done_show) TextView mTextViewDone;

    public ShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show, container, false);
        ButterKnife.inject(this, rootView);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(discardPhotoReceiver, new IntentFilter("discard_photo"));
        setupDoneText();
        setupCardStack();
        getListAllPhotosTask(getActivity(), mCardAdapter, mCardStack).execute();
        counter = mCardAdapter.getCount();
        return rootView;
    }

    private ListAllPhotosTask getListAllPhotosTask(Context context, BaseAdapter adapter, RelativeLayout container) {
        return new ListAllPhotosTask(getActivity(), adapter, container);
    }

    private void setupCardStack() {
        mCardStack = (CardStack) rootView.findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.card_content);
        mCardStack.setStackMargin(20);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discardPhotoReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(discardPhotoReceiver);
    }

}
