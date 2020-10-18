package com.app.session.utils;

public enum MIMEType {
    IMAGE("image/*"), VIDEO("video/*"),AUDIO("audio/*");
    public String value;

    MIMEType(String value) {
        this.value = value;
    }
}
