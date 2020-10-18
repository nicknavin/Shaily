package com.app.session.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.app.session.R;

import com.app.session.activity.ConsultantStoryActivity;
import com.app.session.activity.ConsultantUserActivity;
import com.app.session.activity.MyProfileActivityNew;
import com.app.session.notification.NotificationHelper;

import java.util.Objects;

import androidx.core.app.NotificationCompat;

public class FileProgressReceiver extends BroadcastReceiver {
    private static final String TAG = "FileProgressReceiver";
    public static final String ACTION_CLEAR_NOTIFICATION = "com.wave.ACTION_CLEAR_NOTIFICATION";
    public static final String ACTION_PROGRESS_NOTIFICATION = "com.wave.ACTION_PROGRESS_NOTIFICATION";
    public static final String ACTION_UPLOADED = "com.wave.ACTION_UPLOADED";

    NotificationHelper mNotificationHelper;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder notification;
    public static final int STATUS = 123456;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        mNotificationHelper = new NotificationHelper(mContext);

        // Get notification id
        int notificationId = intent.getIntExtra("notificationId", 1);
        // Receive progress
        int progress = intent.getIntExtra("progress", 0);

        switch (Objects.requireNonNull(intent.getAction())) {
            case ACTION_PROGRESS_NOTIFICATION:
                notification = mNotificationHelper.getNotification(mContext.getString(R.string.uploading), mContext.getString(R.string.in_progress), progress);
                mNotificationHelper.notify(NOTIFICATION_ID, notification);
                break;
            case ACTION_CLEAR_NOTIFICATION:
                mNotificationHelper.cancelNotification(notificationId);
                break;
            case ACTION_UPLOADED:



                Intent resultIntent = new Intent(mContext, ConsultantUserActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                        0 /* Request code */, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                notification = mNotificationHelper.getNotification(mContext.getString(R.string.message_upload_success), mContext.getString(R.string.file_upload_successful), resultPendingIntent);
                mNotificationHelper.notify(NOTIFICATION_ID, notification);
                if (intent.getParcelableExtra("RECEVIER") != null)
                {
                    ResultReceiver resultReceiver = intent.getParcelableExtra("RECEVIER");
                    String data = intent.getStringExtra("DATA");
                    Bundle bundle = new Bundle();
                    bundle.putString("STATUS", "Done");
                    bundle.putString("DATA", data);
                    resultReceiver.send(STATUS, bundle);
                }

                break;
            default:
                break;
        }

    }
}