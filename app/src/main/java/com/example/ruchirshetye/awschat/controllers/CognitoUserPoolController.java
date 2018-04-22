package com.example.ruchirshetye.awschat.controllers;

/**
 * Created by Ruchir Shetye on 19-Mar-18.
 */
import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;

import com.example.ruchirshetye.awschat.interfaces.CognitoUserPoolControllerGenericHandler;
import com.example.ruchirshetye.awschat.interfaces.CognitoUserPoolControllerSignupHandler;
import com.example.ruchirshetye.awschat.interfaces.CognitoUserPoolControllerUserDetailsHandler2;
import com.example.ruchirshetye.awschat.interfaces.CognitoUserPoolControllerConfirmSignupHandler;

public class CognitoUserPoolController {

    //TO DO: Insert your Cognito user pool settings here
    private String userPoolRegion = "us-east-1";
    private String userPoolID = "us-east-1_I6C2fm9GS";

    //TO DO: Insert the client id and client secret for the App you created
    // within the Cognito user pool.
    private String appClientID = "1p4hrllvk95brs9kf59svo6ulb";
    private String appClientSecret = "110a81vjllgs28j5e28rnr9rvfjdj3ngfj4pautuehghektti64i";

    private CognitoUserPool userPool;
    private Context mContext;
    private CognitoUserSession mUserSession;



    private static CognitoUserPoolController instance = null;
    private CognitoUserPoolController() {}

    public static CognitoUserPoolController getInstance(Context context) {
        if(instance == null) {
            instance = new CognitoUserPoolController();
        }

        instance.setupUserPool(context);
        return instance;
    }

    private void  setupUserPool(Context context) {
        if (userPool == null) {
            mContext = context;
            userPool = new CognitoUserPool(context, userPoolID, appClientID, appClientSecret);
            return;
        }

        if (mContext != context) {
            userPool = new CognitoUserPool(context, userPoolID, appClientID, appClientSecret);
        }
    }

    public void login(String username,
                      final String password,
                      final CognitoUserPoolControllerGenericHandler completion) {
        CognitoUser user = userPool.getUser(username);
        user.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession,CognitoDevice device) {
                completion.didSucceed();

            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String UserId) {
                // The API needs user sign-in credentials to continue
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(UserId, password, null);
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                // Multi-factor authentication is required; get the verification code from user
            }

            @Override
            public void onFailure(Exception exception) {

            }
            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }
        });

    }

    public void signup(String username,
                       final String password,
                       String emailAddress,
                       final CognitoUserPoolControllerSignupHandler completion) {

        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute("email", emailAddress);

        userPool.signUpInBackground(username, password, userAttributes, null, new SignUpHandler() {

            @Override
            public void onSuccess(final CognitoUser user, final boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {

                final boolean userMustConfirmEmailAddress = !signUpConfirmationState;
                if (userMustConfirmEmailAddress == true) {
                    completion.didSucceed(user, null, userMustConfirmEmailAddress);
                    return;
                }

                user.getSessionInBackground(new AuthenticationHandler() {

                    @Override
                    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                        mUserSession = userSession;
                        completion.didSucceed(user, userSession, userMustConfirmEmailAddress);
                    }

                    @Override
                    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String UserId) {
                        // The API needs user sign-in credentials to continue
                        AuthenticationDetails authenticationDetails = new AuthenticationDetails(UserId, password, null);
                        authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                        authenticationContinuation.continueTask();
                    }

                    @Override
                    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                        // Multi-factor authentication is required; get the verification code from user
                    }

                    @Override
                    public void authenticationChallenge(ChallengeContinuation continuation) {

                    }

                    @Override
                    public void onFailure(Exception exception) {
                        completion.didFail(exception);
                    }
                });



            }

            @Override
            public void onFailure(Exception exception) {
                completion.didFail(exception);
            }
        });
    }

    public void confirmSignup(final CognitoUser user, final String password, String confirmationCode, final CognitoUserPoolControllerConfirmSignupHandler completion) {

        user.confirmSignUpInBackground(confirmationCode, false, new GenericHandler() {
            @Override
            public void onSuccess() {

                user.getSessionInBackground(new AuthenticationHandler() {

                    @Override
                    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                        mUserSession = userSession;
                        completion.didSucceed(user, userSession);
                    }

                    @Override
                    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String UserId) {
                        // The API needs user sign-in credentials to continue
                        AuthenticationDetails authenticationDetails = new AuthenticationDetails(UserId, password, null);
                        authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                        authenticationContinuation.continueTask();
                    }

                    @Override
                    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                        // Multi-factor authentication is required; get the verification code from user
                    }

                    @Override
                    public void authenticationChallenge(ChallengeContinuation continuation) {

                    }

                    @Override
                    public void onFailure(Exception exception) {
                        completion.didFail(exception);
                    }
                });

            }

            @Override
            public void onFailure(Exception exception) {
                completion.didFail(exception);
            }
        });
    }

    public void resendConfirmationCode(CognitoUser user, final CognitoUserPoolControllerGenericHandler completion) {
        user.resendConfirmationCodeInBackground(new VerificationHandler() {
            @Override
            public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
                completion.didSucceed();
            }

            @Override
            public void onFailure(Exception exception) {
                completion.didFail(exception);
            }
        });
    }


    public CognitoUser getCurrentUser() {
        return userPool.getCurrentUser();
    }

    public void getUserDetails(CognitoUser user, final CognitoUserPoolControllerUserDetailsHandler2 completion) {
        user.getDetailsInBackground(new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                completion.didSucceed(cognitoUserDetails);
            }

            @Override
            public void onFailure(Exception exception) {
                completion.didFail(exception);
            }
        });
    }

    public String getUserPoolID() {
        return userPoolID;
    }

    public String getUserPoolRegion() {
        return userPoolRegion;
    }

    public CognitoUserSession getUserSession() {
        return mUserSession;
    }

}
