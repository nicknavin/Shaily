package com.app.session.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.session.notification.NotificationHelper;
import com.app.session.service.FileUploadService;


import java.util.Objects;

public class RetryJobReceiver extends BroadcastReceiver {

    public static final String ACTION_RETRY = "com.wave.ACTION_RETRY";
    public static final String ACTION_CLEAR = "com.wave.ACTION_CLEAR";
    NotificationHelper mNotificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * Handle notification user actions
         */
        mNotificationHelper = new NotificationHelper(context);
        int notificationId = intent.getIntExtra("notificationId", 0);
        String filePath = intent.getStringExtra("mFilePath");
        switch (Objects.requireNonNull(intent.getAction())) {
            case ACTION_RETRY:
                mNotificationHelper.cancelNotification(notificationId);
                Intent mIntent = new Intent(context, FileUploadService.class);
                mIntent.putExtra("mFilePath", filePath);
                FileUploadService.enqueueWork(context, mIntent);
                break;
            case ACTION_CLEAR:
                mNotificationHelper.cancelNotification(notificationId);
                break;
            default:
                break;
        }
    }
}
