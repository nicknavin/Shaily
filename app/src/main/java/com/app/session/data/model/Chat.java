package com.app.session.data.model;

/**
 * Created by sourabh on 8/9/17.
 */

public class Chat {
    public String message;
    public String timestamp;
    private String fromuser_id;
    private String touser_id;

    public Chat(String message, String timestamp, String fromuser_id, String touser_id) {
        this.message = message;
        this.timestamp = timestamp;
        this.fromuser_id = fromuser_id;
        this.touser_id = touser_id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFromuser_id() {
        return fromuser_id;
    }

    public String getTouser_id() {
        return touser_id;
    }
}
