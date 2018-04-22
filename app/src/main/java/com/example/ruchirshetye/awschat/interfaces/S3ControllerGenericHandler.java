package com.example.ruchirshetye.awschat.interfaces;

/**
 * Created by Ruchir Shetye on 14-Apr-18.
 */

public interface S3ControllerGenericHandler {
    void didSucceed();
    void didFail(Exception exception);
}
