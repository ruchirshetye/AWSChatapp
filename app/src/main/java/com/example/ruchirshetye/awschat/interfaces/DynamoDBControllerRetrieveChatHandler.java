package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 14-Apr-18.
 */

public interface DynamoDBControllerRetrieveChatHandler {
    void didSucceed();
    void didNotFindChat();
    void didFail(Exception exception);
}
