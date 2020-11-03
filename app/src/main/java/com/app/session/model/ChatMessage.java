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

    private String uri;

    private boolean upload;

    @SerializedName("displayFileName")
    private String displayFileName;

@SerializedName("durationTime")
    private String durationTime;


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
        uri = in.readString();
        upload = in.readByte() != 0;
        displayFileName = in.readString();
        durationTime = in.readString();
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

    public String getDisplayFileName() {
        return displayFileName;
    }

    public void setDisplayFileName(String displayFileName)
    {
        this.displayFileName = displayFileName;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public ChatMessage() {
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
        parcel.writeString(uri);
        parcel.writeByte((byte) (upload ? 1 : 0));
        parcel.writeString(displayFileName);
        parcel.writeString(durationTime);
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

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

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    @Nullable
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(@Nullable String messageType) {
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

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
}