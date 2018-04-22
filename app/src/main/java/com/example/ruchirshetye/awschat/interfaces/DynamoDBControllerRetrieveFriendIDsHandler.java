package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 11-Apr-18.
 */

import java.util.ArrayList;

public interface DynamoDBControllerRetrieveFriendIDsHandler {
    void didSucceed(ArrayList<String> results);
    void didFail(Exception exception);
}
