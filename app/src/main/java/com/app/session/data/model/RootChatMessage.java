package com.app.session.data.model;

import com.app.session.room.ChatMessageBody;
import com.google.gson.annotations.SerializedName;

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
