package com.wahlzeit.mobile.listeners;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * A class to determines he actions to be performed when a user has finished writing on an editable text view
 * Created by iordanis on 08/04/16.
 */
public class EditorActionListener extends AbstractSwitcher implements TextView.OnEditorActionListener {

    public EditorActionListener(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.i("Main", "Oh there was some action");
        hideKeyboardAndCursor(v);
        switchBackToTextView(v);
        return false;
    }

    private void switchBackToTextView(View v) {
        ViewSwitcher parentSwitcher = (ViewSwitcher) v.getParent();
        EditText editText = (EditText) v;
        TextView siblingTextView = (TextView) parentSwitcher.getChildAt(0);

        String editedText = editText.getText().toString();
        siblingTextView.setText(editedText);
        parentSwitcher.showPrevious();
    }
}