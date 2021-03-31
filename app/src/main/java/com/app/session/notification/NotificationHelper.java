package com.app.session.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import com.app.session.R;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * Helper class to manage notification channels, and create notifications.
 */
public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String CHANNEL_ID = "default";
    public static final String CHANNEL_NAME = "Sending Media";
    public static final int NOTIFICATION_ID = 143;
    public NotificationHelper(Context mContext) {
        super(mContext);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLightColor(Color.GREEN);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(mChannel);
        }
    }

    public NotificationCompat.Builder getNotification(String title, String body, int progress) {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        mBuilder.setSmallIcon(getSmallIcon());
        mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        mBuilder.setContentTitle(title)
                .setContentText(body)
                //.setContentIntent(resultPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setVibrate(new long[]{0L});
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setProgress(100, progress, false);
        if (progress == 100) {
            mBuilder.setProgress(0, 0, false);
            mBuilder.setContentText(body);
        }
        return mBuilder;
    }


    public NotificationCompat.Builder getNotification(String title, String body, PendingIntent resultPendingIntent) {

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        mBuilder.setSmallIcon(getSmallIcon());
        mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        mBuilder.setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(resultPendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(importance);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mBuilder.setChannelId(CHANNEL_ID);

        }
        mBuilder.setVibrate(new long[]{0L});
      //  mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        return mBuilder;
    }



    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public void createNotification(String title, String message,PendingIntent resultPendingIntent) {


        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true).setOngoing(true)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.DEFAULT_ALL)
        .setContentIntent(resultPendingIntent);


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(importance);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        mNotificationManager.notify(NOTIFICATION_ID, notification);


    }








    /**
     * Send a notification.
     *
     * @param id           The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private int getSmallIcon() {
        return R.drawable.app_new_icon;
    }

    /**
     * Get the notification manager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void cancelNotification(int notificationId)
    {

        getManager().cancel(notificationId);
    }
}
