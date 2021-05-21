package com.app.session.model;

/**
 * Created by sourabh on 15/9/17.
 */

public class Messages {
    private String SessionID;
    private String fromUser_id;
    private String ToUser_id;
    public String message;
    private String messageTime;
    private String message_type;
    private String file_path;
    private String status;
    private int MessageID;

    public Messages(String sessionID, String fromUser_id, String toUser_id, String message, String messageTime, String message_type, String file_path, String status, int messageID) {
        SessionID = sessionID;
        this.fromUser_id = fromUser_id;
        ToUser_id = toUser_id;
        this.message = message;
        this.messageTime = messageTime;
        this.message_type = message_type;
        this.file_path = file_path;
        this.status = status;
        MessageID = messageID;
    }

    public String getSessionID() {
        return SessionID;
    }

    public String getFromUser_id() {
        return fromUser_id;
    }

    public String getToUser_id() {
        return ToUser_id;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getMessage_type() {
        return message_type;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getStatus() {
        return status;
    }

    public int getMessageID() {
        return MessageID;
    }
}
