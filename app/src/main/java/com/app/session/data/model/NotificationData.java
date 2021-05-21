package com.app.session.data.model;

public class NotificationData {
    public String to;
    public String priority;


    public NotificationModel.Data data = new NotificationModel.Data();



    public static class Data {
        public String title;
        public String body;
        public String message;
    }
}
