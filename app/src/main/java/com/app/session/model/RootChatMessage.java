package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RootChatMessage
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private ChatMessageBody chatMessageBody;




    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatMessageBody getChatMessageBody() {
        return chatMessageBody;
    }

    public void setChatMessageBody(ChatMessageBody chatMessageBody) {
        this.chatMessageBody = chatMessageBody;
    }
}
