package com.example.ruchirshetye.awschat;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.example.ruchirshetye.awschat.controllers.CognitoIdentityPoolController;
import com.example.ruchirshetye.awschat.controllers.CognitoUserPoolController;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.example.ruchirshetye.awschat.interfaces.CognitoUserPoolControllerGenericHandler;
import com.example.ruchirshetye.awschat.interfaces.CognitoIdentityPoolControllerGenericHandler;
import com.example.ruchirshetye.awschat.interfaces.CognitoUserPoolControllerUserDetailsHandler2;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private EditText mUsernameView;
    private EditText mPasswordView;

    private String mGoogleClientId = "183612140914-phjg1u7uqk1ifvcke5ti50m0vh3vvrk9.apps.googleusercontent.com";
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSignInButton;
    private int GOOGLE_SIGNIN_RESULT_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignupButton = (Button) findViewById(R.id.signup_button);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySignupActivity();
            }
        });
        Log.i("GoogleSign","Button");

        configureGoogleSignIn();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_SIGNIN_RESULT_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i("GoogleSign","if mei jaa rah hau");
            handleGoogleSignInResult(result);

        } else {
            Log.v("Facbook aaraha hai","Else mei jaa rah hau");
//            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void configureGoogleSignIn() {

        Log.i("GoogleSign","lolitstarted");

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(mGoogleClientId)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();
        Log.i("GoogleSign","API Client Done");

        mGoogleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setSize(SignInButton.SIZE_STANDARD);
//        mGoogleSignInButton.setScopes(mGoogleSignInOptions.getScopeArray());
        Log.i("GoogleSign","Find View by ID done");

        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GoogleSign","On CLick");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGNIN_RESULT_CODE);
            }
        });
    }



    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            mUsernameView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            return;
        }
        CognitoUserPoolController userPoolController = CognitoUserPoolController.getInstance(this);
        userPoolController.login(username, password, new CognitoUserPoolControllerGenericHandler() {
            @Override
            public void didSucceed() {displaySuccessMessage();}

            @Override
            public void didFail(Exception exception) {displayErrorMessage(exception);}
        });

    }

    private void displaySignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("A", "A");
        startActivity(intent);
    }
    private void displayHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    private void displaySuccessMessage() {

        final Context context = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Login succesful!");
                builder.setTitle("Success");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                displayHomeActivity();
                            }
                        });

                final AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void displayErrorMessage(final Exception exception) {

        final Context context = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(exception.getMessage());
                builder.setTitle("Error");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                final AlertDialog alert = builder.create();

                alert.show();
            }
        });
    }

    private void displayErrorMessage(final String title, final String message) {

        final Context context = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message);
                builder.setTitle(title);
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                final AlertDialog alert = builder.create();

                alert.show();
            }
        });
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess() == false) {
            displayErrorMessage("Error", "Google Sign-In Failed.");
            return;
        }

        GoogleSignInAccount acct = result.getSignInAccount();
        String authToken = acct.getIdToken();
        String username = acct.getDisplayName();
        String email = acct.getEmail();

        CognitoIdentityPoolController identityPoolController = CognitoIdentityPoolController.getInstance(this);
        identityPoolController.getFederatedIdentityForGoogle(authToken, username, email, new CognitoIdentityPoolControllerGenericHandler() {
            @Override
            public void didSucceed() {
                displaySuccessMessage();
            }

            @Override
            public void didFail(Exception exception) {
                displayErrorMessage(exception);
            }
        });
    }

    private void getFederatedIdentity(final CognitoUser cognitoUser, final CognitoUserSession userSession) {

        final Context context = this;
        final CognitoUserPoolController userPoolController = CognitoUserPoolController.getInstance(this);

        userPoolController.getUserDetails(cognitoUser, new CognitoUserPoolControllerUserDetailsHandler2() {

            @Override
            public void didSucceed(CognitoUserDetails userDetails) {

                CognitoUserAttributes userAttributes = userDetails.getAttributes();
                Map attributeMap    = userAttributes.getAttributes();

                String authToken = userSession.getIdToken().getJWTToken();
                String username = mUsernameView.getText().toString();
                String email = attributeMap.get("email").toString();

                CognitoIdentityPoolController identityPoolController = CognitoIdentityPoolController.getInstance(context);
                identityPoolController.getFederatedIdentityForAmazon(authToken,  username, email,
                        userPoolController.getUserPoolRegion(),
                        userPoolController.getUserPoolID(),
                        new CognitoIdentityPoolControllerGenericHandler() {
                            @Override
                            public void didSucceed() {
                                displaySuccessMessage();
                            }

                            @Override
                            public void didFail(Exception exception) {
                                displayErrorMessage(exception);
                            }
                        });

            }

            @Override
            public void didFail(Exception exception) {
                displayErrorMessage(exception);
            }
        });
    }



    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        displayErrorMessage("Error", connectionResult.getErrorMessage());
    }

}

