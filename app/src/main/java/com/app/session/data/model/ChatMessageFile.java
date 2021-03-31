package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessageFile implements Parcelable
{
    private String senderId;
    private String senderName;
    private String senderProfileUrl;
    private String reciverId;
    private String reciverName;
    private String reciverProfileUrl;
    private String messageType;

   public ChatMessageFile()
    {

    }
    protected ChatMessageFile(Parcel in) {
        senderId = in.readString();
        senderName = in.readString();
        senderProfileUrl = in.readString();
        reciverId = in.readString();
        reciverName = in.readString();
        reciverProfileUrl = in.readString();
        messageType = in.readString();
    }

    public static final Creator<ChatMessageFile> CREATOR = new Creator<ChatMessageFile>() {
        @Override
        public ChatMessageFile createFromParcel(Parcel in) {
            return new ChatMessageFile(in);
        }

        @Override
        public ChatMessageFile[] newArray(int size) {
            return new ChatMessageFile[size];
        }
    };

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfileUrl() {
        return senderProfileUrl;
    }

    public void setSenderProfileUrl(String senderProfileUrl) {
        this.senderProfileUrl = senderProfileUrl;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getReciverProfileUrl() {
        return reciverProfileUrl;
    }

    public void setReciverProfileUrl(String reciverProfileUrl) {
        this.reciverProfileUrl = reciverProfileUrl;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public static Creator<ChatMessageFile> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(senderId);
        parcel.writeString(senderName);
        parcel.writeString(senderProfileUrl);
        parcel.writeString(reciverId);
        parcel.writeString(reciverName);
        parcel.writeString(reciverProfileUrl);
        parcel.writeString(messageType);
    }
}
