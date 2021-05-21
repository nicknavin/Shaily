package com.app.session.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.activity.ChattingActivity;
import com.app.session.activity.HomeUserChatProfileActivity;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

@SuppressLint("NewApi")
public class MyJobSchedulerService extends JobService {
    private static final String TAG = "tag";
    private boolean jobCancelled = false;
    private Messenger mActivityMessenger;

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        mActivityMessenger = intent.getParcelableExtra(Constant.MESSENGER_INTENT_KEY);
//        return START_NOT_STICKY;
//    }

    

    @Override
    public boolean onStartJob(JobParameters params)
    {
        Log.d(TAG, "onStartJob is called");
        Log.d(TAG, "onStartJob Thread name " + Thread.currentThread().getName());
        //doBackgroundWork(params);

        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        //sendMessage(Constant.JOB_STARTED, params.getJobId());
//        jobFinished(params, false);
//        long duration =1000;
//
//        // Uses a handler to delay the execution of jobFinished().
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sendMessage(Constant.JOB_STOPPED, params.getJobId());
//                jobFinished(params, false);
//
//            }
//        }, duration);
        return true;//if set return value is false that mean this scheduler not using background thread ,
        // but we are using background thread so set return is true.
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobCancelled = true;
        return true;
        // I want it to reschedule so returned true, if we would have returned false, then job would have ended here.
        // It would not fire onStartJob() when constraints are re satisfied.
    }


    private void sendMessage(int messageID, @Nullable Object params) {
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }
        Message m = Message.obtain();
        m.what = messageID;
        m.obj = params;
        try {
            createMessageNotification("notification is called");
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Utility.log("JobService task running");
            createMessageNotification("JobService task running");
            sendEvent();
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }

    });


    class loadData implements Runnable {

        @Override
        public void run() {
            mJobHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
    }


    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.d(TAG, "run: " + i);
                    if (jobCancelled) {
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Job finished");
                jobFinished(params, true);
            }
        }).start();
    }


    int bundleNotificationId = 100;
    int singleNotificationId = 100;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public void createMessageNotification(String request) {
        Intent connectIntent = null;
        String title = "", message = "", profileUrl = "", receiverID = "121212", receiverName = "", channelID = "121212", groupID = "", senderID = "", groupName = "";


        connectIntent = new Intent(this, HomeUserChatProfileActivity.class);


        /**Creates an explicit intent for an Activity in your app**/
        bundleNotificationId += 100;
        singleNotificationId = bundleNotificationId;
        String bundle_notification_id = "bundle_notification_" + bundleNotificationId;

        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        connectIntent.putExtra("notificationId", bundleNotificationId);

        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, connectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();
        title = request + " " + when;

        mBuilder = new NotificationCompat.Builder(this, channelID);
        mBuilder.setSmallIcon(R.drawable.app_new_icon);
        mBuilder.setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(false)
                .setWhen(when)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setSound(ring)
                .setGroup(groupID)
                .setGroupSummary(true)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(connecPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(groupID, groupName);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelID, "Messages", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(importance);
            notificationChannel.setShowBadge(true);
            notificationChannel.setGroup(groupID);

            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(channelID);
            mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(bundleNotificationId, mBuilder.build());

    }

private void sendEvent()
{
    Intent intent = new Intent(Constant.MESSENGER_INTENT_KEY);

    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
}
}