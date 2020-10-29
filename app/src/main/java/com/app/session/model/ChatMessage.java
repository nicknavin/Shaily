package com.app.session.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.app.session.api.Urls;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.Nullable;


public class ChatMessage implements Parcelable {

    @SerializedName("reciverId")
    private String reciverId;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("senderName")
    private String senderName;

    @SerializedName("reciverName")
    private String reciverName;

    @SerializedName("__v")
    private int V;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("_id")
    private String id;

    @SerializedName("message")
    private String message;

    @SerializedName("reciverProfileUrl")
    private String reciverProfileUrl;

    @SerializedName("senderProfileUrl")
    private String senderProfileUrl;
    @Nullable
    @SerializedName("messageType")
    private String messageType;

    @SerializedName("file")
    private String file;

    private String path;

    private boolean upload;


    public ChatMessage() {
    }

    protected ChatMessage(Parcel in) {
        reciverId = in.readString();
        createdAt = in.readString();
        senderId = in.readString();
        senderName = in.readString();
        reciverName = in.readString();
        V = in.readInt();
        isRead = in.readByte() != 0;
        id = in.readString();
        message = in.readString();
        reciverProfileUrl = in.readString();
        senderProfileUrl = in.readString();
        messageType = in.readString();
        file = in.readString();
        path = in.readString();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setV(int V) {
        this.V = V;
    }

    public int getV() {
        return V;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getReciverProfileUrl() {
        return reciverProfileUrl;
    }

    public void setReciverProfileUrl(String reciverProfileUrl) {
        this.reciverProfileUrl = reciverProfileUrl;
    }

    public String getSenderProfileUrl() {
        return senderProfileUrl;
    }

    public void setSenderProfileUrl(String senderProfileUrl) {
        this.senderProfileUrl = senderProfileUrl;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFile() {
        return Urls.BASE_IMAGES_URL + file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public static Creator<ChatMessage> getCREATOR() {
        return CREATOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessage)) return false;
        ChatMessage that = (ChatMessage) o;
        return getV() == that.getV() &&
                isRead() == that.isRead() &&
                getReciverId().equals(that.getReciverId()) &&
                getCreatedAt().equals(that.getCreatedAt()) &&
                getSenderId().equals(that.getSenderId()) &&
                getSenderName().equals(that.getSenderName()) &&
                getReciverName().equals(that.getReciverName()) &&
                getId().equals(that.getId()) &&
                getMessage().equals(that.getMessage()) &&
                getReciverProfileUrl().equals(that.getReciverProfileUrl()) &&
                getSenderProfileUrl().equals(that.getSenderProfileUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReciverId(), getCreatedAt(), getSenderId(), getSenderName(), getReciverName(), getV(), isRead(), getId(), getMessage(), getReciverProfileUrl(), getSenderProfileUrl());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reciverId);
        parcel.writeString(createdAt);
        parcel.writeString(senderId);
        parcel.writeString(senderName);
        parcel.writeString(reciverName);
        parcel.writeInt(V);
        parcel.writeByte((byte) (isRead ? 1 : 0));
        parcel.writeString(id);
        parcel.writeString(message);
        parcel.writeString(reciverProfileUrl);
        parcel.writeString(senderProfileUrl);
        parcel.writeString(messageType);
        parcel.writeString(file);
        parcel.writeString(path);
    }
}