package com.app.session.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.app.session.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "RestaurantDriver";
    public static final int NOTIFICATION_ID_ORDER = 1;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // createNotificationChannel();
        Gson gson = new Gson();


        Map<String, String> params = remoteMessage.getData();
        String message = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getNotification().getBody();

        Log.e("body..", body);
//        Order order = gson.fromJson(body, Order.class);
//        Log.e("result..", order.getCustomerName());
        //createAudioVideoNotification(title,message);
    }


    private void showNotification(String title, String input) {
//        Intent notificationIntent = new Intent(this, ChattingActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .build();

        startForeground(1, notification);
        //.setContentIntent(pendingIntent)
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Normal",
                    NotificationManager.IMPORTANCE_MIN
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            manager.notify();
        }
    }


//    private NotificationManager mNotificationManager;
//    private NotificationCompat.Builder mBuilder;
//
//    public void createAudioVideoNotification(String title, String message) {
//        Intent connectIntent = null;
//        //  String title = "", message = "",profileUrl="";
//        connectIntent = new Intent(this, HomeActivity.class);
//        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        connectIntent.putExtra("DATA", request);
//        connectIntent.putExtra("CALLING_TYPE", "receiver");
//        connectIntent.putExtra("notificationId", NOTIFICATION_ID_ORDER);
//
//        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
//                0 /* Request code */, connectIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        long when = System.currentTimeMillis();
//        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        mBuilder.setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setWhen(when)
//                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
//                .setSound(ring)
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                .setPriority(Notification.DEFAULT_ALL).setContentIntent(connecPendingIntent);
//
//
//        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            AudioAttributes att = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                    .build();
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Order Notification", importance);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setImportance(importance);
//            notificationChannel.setSound(ring, att);
//            notificationChannel.setShowBadge(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            assert mNotificationManager != null;
//            mBuilder.setChannelId(CHANNEL_ID);
//            mNotificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        mNotificationManager.notify(NOTIFICATION_ID_ORDER, mBuilder.build());
//
//    }

}