package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 11-Apr-18.
 */

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;

public interface CognitoUserPoolControllerConfirmSignupHandler {
    void didSucceed(CognitoUser user, CognitoUserSession session);
    void didFail(Exception exception);
}
