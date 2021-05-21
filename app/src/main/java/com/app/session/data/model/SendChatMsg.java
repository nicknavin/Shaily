package com.app.session.data.model;

public class SendChatMsg {
    private String userId;
    private String receiverID;
    private String message;
    private String senderName;
    private String reciverName;
    private String reciverProfileUrl;
    private String senderProfileUrl;
    private String fileUrl;
    private String messageType;
    private boolean isFile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
