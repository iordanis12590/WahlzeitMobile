package com.wahlzeit.mobile.activities;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.wahlzeit.mobile.network.CommunicationManager;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.network.asyncTasks.GuestLoginTask;
import com.wahlzeit.mobile.network.asyncTasks.Oauth2LoginTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A login screen that offers login via myEmail/password.
 */
public class LoginActivity extends BaseActivity {

    private GoogleAccountCredential credential;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    @InjectView(R.id.button_signin_user) Button mUserSigninButton;
    @InjectView(R.id.button_signin_guest) Button mGuestSigningButton;
    @InjectView(R.id.login_progress) View mProgressView;
    @InjectView(R.id.login_form) View mLoginFormView;
    private SharedPreferences settings;
    private static String PREF_ACCOUNT_NAME = "ACCOUNT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        settings = getSharedPreferences(getResources().getString(R.string.app_name), 0);
        credential = GoogleAccountCredential.usingAudience(this, CommunicationManager.manager.WEB_CLIENT);
        getAccountName();
        setupLoginButtons();
    }

    /**
     * Handles the result of the popup dialog launched to allow the user to choose one of his accounts
     * @param requestCode
     * @param resultCode
     * @param data The intent which contains the user's choice
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        // User is authorized.
                        credential.setSelectedAccountName(accountName);
                        ModelManager.manager.setAccountName(accountName);
                        saveAccountName(accountName);
                        ModelManager.manager.setCredential(credential);
                    }
                }
                break;
        }
    }

    /**
     * Retrieves the account name from the shared preferences of launches a new account picker
     * if no account is found.
     */
    private void getAccountName() {
        String accountName = settings.getString(PREF_ACCOUNT_NAME, null);
        if(accountName == null || accountName.contentEquals("")) {
            chooseAccount();
        } else {
            ModelManager.manager.setAccountName(accountName);
            credential.setSelectedAccountName(accountName);
            ModelManager.manager.setCredential(credential);
        }
    }

    /**
     *
     * @param accountName
     */
    private void saveAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
    }

    /**
     * Sets up the click listener for the user and guest login buttons.
     */
    private void setupLoginButtons() {
        mUserSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptOauth2Login(true);
            }
        });

//        mAdminSigningButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptOauth2Login(false);
//            }
//        });

        mGuestSigningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptGuestLogin();
            }
        });
    }

    /**
     * Creates a popup dialog which allows the user to select one of his account in order to sign in
     */
    void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    protected void makeToast(String about) {
        Toast.makeText(this, about, Toast.LENGTH_LONG).show();
    }

    private Oauth2LoginTask getOauth2LoginTask(LoginActivity loginActivity, String email, String scope, Boolean performUserLogin) {
        return new Oauth2LoginTask(loginActivity, email, scope, performUserLogin);
    }

    private GuestLoginTask getGuestLoginTask(LoginActivity loginActivity) {
        return new GuestLoginTask(loginActivity);
    }
    /**
     * Attempts to retrieve authentication token and profile data from server.
     */
    private void attemptOauth2Login(Boolean performUserLogin) {

        if(CommunicationManager.manager.isNetworkAvailable(this)) {
            if(ModelManager.manager.getCredential().getSelectedAccountName() == null) {
                chooseAccount();
            }
            String userAccount = ModelManager.manager.getAccountName();
            if(userAccount != null && userAccount.length() > 0) {
                showProgress(true);
                getOauth2LoginTask(LoginActivity.this, userAccount, CommunicationManager.manager.SCOPE_LOGIN, performUserLogin).execute();
            } else {
                makeToast("Please choose a valid google account and try again");
            }
        } else {
            makeToast("No network service");
        }
    }

    private void attemptGuestLogin() {
        if(CommunicationManager.manager.isNetworkAvailable(this)) {
            showProgress(true);
            getGuestLoginTask(LoginActivity.this).execute();
        } else {
            makeToast("Please choose a valid google account and try again");
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
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



