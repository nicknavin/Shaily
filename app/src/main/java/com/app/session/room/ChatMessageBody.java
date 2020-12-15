package com.app.session.room;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

import androidx.annotation.Nullable;

public class ChatMessageBody
{
    @SerializedName("Total_Page")
    private int Total_Page;

    @Nullable
    @SerializedName("private_messages")
    LinkedList<ChatMessage> chatMessages;


    public int getTotal_Page() {
        return Total_Page;
    }

    public void setTotal_Page(int total_Page) {
        Total_Page = total_Page;
    }

    public LinkedList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(LinkedList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
