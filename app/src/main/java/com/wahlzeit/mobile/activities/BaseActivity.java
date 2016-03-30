package com.wahlzeit.mobile.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wahlzeit.mobile.R;

import java.util.Locale;

/**
 * Created by iordanis on 30/03/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings_language:
                lauchLanguageSelectionDialog();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void lauchLanguageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_language);
        String[] types = getResources().getStringArray(R.array.language);
        builder.setSingleChoiceItems(types, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Configuration configuration = new Configuration();
                Locale locale = new Locale("en");
                switch (which) {
                    case 1:
                        locale = new Locale("de");
                        break;
                }
                configuration.locale = locale;
                getResources().updateConfiguration(configuration, null);
            }
        });
        builder.show();
    }

}
