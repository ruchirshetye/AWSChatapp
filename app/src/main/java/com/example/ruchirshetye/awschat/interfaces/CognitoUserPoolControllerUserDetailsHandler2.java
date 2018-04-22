package com.example.ruchirshetye.awschat.interfaces;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;

/**
 * Created by Ruchir Shetye on 20-Mar-18.
 */

public interface CognitoUserPoolControllerUserDetailsHandler2 {
    void didSucceed(CognitoUserDetails userDetails);
    void didFail(Exception exception);
}
