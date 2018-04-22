package com.example.ruchirshetye.awschat.interfaces;

public interface CognitoUserPoolControllerGenericHandler {
    void didSucceed();
    void didFail(Exception exception);
}
