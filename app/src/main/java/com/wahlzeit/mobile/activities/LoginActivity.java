package com.wahlzeit.mobile.activities;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.iordanis_mobilezeit.wahlzeitApi.WahlzeitApi;
import com.appspot.iordanis_mobilezeit.wahlzeitApi.model.About;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.wahlzeit.mobile.CommunicationManager;
import com.wahlzeit.mobile.Oauth2LoginTask;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.WahlzeitModel;

import java.io.IOException;

/**
 * A login screen that offers login via myEmail/password.
 */
public class LoginActivity extends AppCompatActivity {

    private GoogleAccountCredential credential;
    static final int REQUEST_ACCOUNT_PICKER = 2;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//            attemptLoginmptLogin();
                    return true;
                }
                return false;
            }
        });

        credential = GoogleAccountCredential.usingAudience(this, CommunicationManager.manager.WEB_CLIENT);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });



        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        // User is authorized.
                        WahlzeitModel.model.setCredential(credential);
                        WahlzeitModel.model.setAccountName(accountName);
                    }
                }
                break;
        }
    }

    public void onClickGetAboutButton(View view) {

        AsyncTask<Integer, Void, About> getAboutTask = new AsyncTask<Integer,Void, About>() {
            protected About doInBackground(Integer... integers) {

                WahlzeitApi wahlzeitServiceHandle = CommunicationManager.manager.getApiServiceHandler(WahlzeitModel.model.getCredential());
                try {
                    WahlzeitApi.GetAbout getAboutCommand = wahlzeitServiceHandle.getAbout();

                    About about = getAboutCommand.execute();
                    return about;
                } catch (IOException e) {
                    Log.e("MainActivity", "Exception during API call", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(About about) {
                if (about != null) {
                    Log.d("WahlzeitActivity", about.getDescription());
                    makeToast(about.getDescription());
                } else {
                    Log.e("WahlzeitModel", "No about was returned from the API");
                }
            }
        };
        getAboutTask.execute();
    }

    protected void makeToast(String about) {
        Toast.makeText(this, about, Toast.LENGTH_LONG).show();
    }


    private Oauth2LoginTask getOauth2LoginTask(LoginActivity loginActivity, String email, String scope) {
        return new Oauth2LoginTask(loginActivity, email, scope);
    }

    /**
     * Attempts to retrieve authentication token and profile data from server.
     */
    private void attemptLogin() {

        if(WahlzeitModel.model.getCredential() == null) {
            chooseAccount();
        }

        if(CommunicationManager.manager.isNetworkAvailable(this)) {
            String userAccount = WahlzeitModel.model.getAccountName();
            if(userAccount != null && userAccount.length() > 0) {
                showProgress(true);
                getOauth2LoginTask(LoginActivity.this, userAccount, CommunicationManager.manager.SCOPE_LOGIN).execute();
                showProgress(false);
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                this.finish();
            } else {
                makeToast("Please choose a valid google account and try again");
            }
        } else {
            makeToast("No network service");
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

