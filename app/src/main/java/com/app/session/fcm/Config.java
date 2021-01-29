package com.app.session.fcm;


/**
 * Created by RatufaManish on 13-05-2017.
 */

public class Config {
    public static final String TOPIC_GLOBAL = "global";
//fL9gIm63U1M:APA91bHwyo1LImJXePY634p360YUgtSHIs5b0smyWHoQHZWg-9BrOZa-XQJostKb6rsyQV0gL59FBK8b5TyDwx6yFukXflC133QqJhdhRTs8uV_z0wuAnTnXtUegvYx0KghU-JBjISla
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
