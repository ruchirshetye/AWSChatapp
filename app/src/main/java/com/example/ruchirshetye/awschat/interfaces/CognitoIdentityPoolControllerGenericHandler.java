package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 25-Mar-18.
 */

public interface CognitoIdentityPoolControllerGenericHandler {
    void didSucceed();
    void didFail(Exception exception);
}
