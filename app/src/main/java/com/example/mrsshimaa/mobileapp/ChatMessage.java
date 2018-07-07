package com.example.mrsshimaa.mobileapp;

import java.util.Date;

/**
 * Created by Mrs.shimaa on 5/8/2018.
 */

public class ChatMessage {
    private  String MessageText;
    private  String MessageUser;
    private  Long MessageTime;
    public ChatMessage(String messageText, String messageUser) {
        this.MessageText = messageText;
        this.MessageUser = messageUser;

        // Initialize to current time
        MessageTime = new Date().getTime();


    }


    public ChatMessage()
    {

    }
    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        this.MessageText = messageText;
    }

    public String getMessageUser() {
        return MessageUser;
    }

    public void setMessageUser(String messageUser) {
        this.MessageUser = messageUser;
    }

    public long getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(long messageTime) {
        this.MessageTime = messageTime;
    }
}
