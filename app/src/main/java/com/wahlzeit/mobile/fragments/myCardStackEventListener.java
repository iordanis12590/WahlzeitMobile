package com.wahlzeit.mobile.fragments;

import com.wenchao.cardstack.CardStack;

/**
 * Created by iordanis on 11/03/16.
 */
public class myCardStackEventListener implements CardStack.CardEventListener {


    @Override
    public boolean swipeEnd(int i, float v) {

        return false;
    }

    @Override
    public boolean swipeStart(int i, float v) {
        return false;
    }

    @Override
    public boolean swipeContinue(int i, float v, float v1) {
        return false;
    }

    @Override
    public void discarded(int i, int i1) {

    }

    @Override
    public void topCardTapped() {

    }
}
