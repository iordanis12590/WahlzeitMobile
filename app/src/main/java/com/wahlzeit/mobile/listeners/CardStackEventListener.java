package com.wahlzeit.mobile.listeners;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.wenchao.cardstack.CardStack;

/**
 * Created by iordanis on 11/03/16.
 */
public class CardStackEventListener implements CardStack.CardEventListener {

    private static final String TAG = "Card stack listener";
    Context mContext;

    public CardStackEventListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean swipeEnd(int direction, float distance) {
        Log.i(TAG,
                "CardStack swipeEnd triggered with direction: " + direction + ", distance: " + distance);
        return distance > 300;
    }

    @Override
    public boolean swipeStart(int direction, float distance) {
        Log.i(TAG, "CardStack swipeStart triggered with direction: "
                + direction
                + ", distance: "
                + distance);
        return true;
    }

    @Override
    public boolean swipeContinue(int direction, float distanceX, float distanceY) {
        Log.i(TAG, "CardStack swipeContinue triggered with direction: "
                + direction
                + ", distanceX: "
                + distanceX
                + ", distanceY: "
                + distanceY);
        return true;
    }

    @Override
    public void discarded(int id, int direction) {
        Log.i(TAG, "CardStack discarded triggered with id: " + id + ", direction: " + direction);
        Intent intent = new Intent("discard_photo");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void topCardTapped() {
        Log.i(TAG, "CardStack topCardTapped triggered");
        Intent intent = new Intent("tapped_top_of_card_stack");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
