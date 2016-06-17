package com.wahlzeit.mobile.listeners;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * A superclass with actions used to create an editable text view UI component
 * Created by iordanis on 08/04/16.
 */
public abstract class AbstractSwitcher  {

    private Context mContext;

    public AbstractSwitcher(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void hideKeyboardAndCursor(View v) {
        InputMethodManager imm =  (InputMethodManager) getmContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void showKeyboardAndCursor(EditText editText) {
        final InputMethodManager inputMethodManager = (InputMethodManager) getmContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

}
