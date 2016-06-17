package com.wahlzeit.mobile.listeners;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * A class to determine the actions to be performed when an editable text view looses focus
 * Created by iordanis on 08/04/16.
 */
public class FocusChangeListener extends AbstractSwitcher implements View.OnFocusChangeListener  {

    public FocusChangeListener(Context mContext) {
        super(mContext);
    }

    public void onFocusChange(View v, boolean hasFocus){
        if(v instanceof EditText && !hasFocus) {
            hideKeyboardAndCursor(v);
            switchBackToTextView((EditText) v);
        }
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
