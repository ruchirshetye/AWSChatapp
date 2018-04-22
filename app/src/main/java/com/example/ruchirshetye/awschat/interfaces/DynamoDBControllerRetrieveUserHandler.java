package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 11-Apr-18.
 */

import com.example.ruchirshetye.awschat.models.User;


public interface DynamoDBControllerRetrieveUserHandler {
    void didSucceed(User user);
    void didFail(Exception exception);
}
