package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 11-Apr-18.
 */

public interface DynamoDBControllerGenericHandler {
    void didSucceed();
    void didFail(Exception exception);
}
