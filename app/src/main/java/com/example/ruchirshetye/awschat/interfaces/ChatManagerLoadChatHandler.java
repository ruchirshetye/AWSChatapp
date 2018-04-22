package com.example.ruchirshetye.awschat.interfaces;

import com.example.ruchirshetye.awschat.models.Chat;

/**
 * Created by Ruchir Shetye on 14-Apr-18.
 */

public interface ChatManagerLoadChatHandler {
    void didSucceed(Chat chat);
    void didFail(Exception exception);
}

