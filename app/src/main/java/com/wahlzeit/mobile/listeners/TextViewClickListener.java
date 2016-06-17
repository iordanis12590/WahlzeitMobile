package com.wahlzeit.mobile.listeners;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * A class to react when en editable text view is clicked
 * Created by iordanis on 08/04/16.
 */
public class TextViewClickListener extends AbstractSwitcher implements View.OnClickListener {

    public TextViewClickListener(Context mContext) {
        super(mContext);
    }

    @Override
    public void onClick(View v) {
        switchToEditText(v);
    }

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

}

