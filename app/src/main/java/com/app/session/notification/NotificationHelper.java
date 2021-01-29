package com.app.session.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

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
                .setOngoing(true)
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
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        mBuilder.setVibrate(new long[]{0L});
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        return mBuilder;
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
