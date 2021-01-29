package com.app.session;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;


import com.app.session.activity.CallIncomingActivity;
import com.app.session.activity.SplashActivity;
import com.app.session.fcm.Config;
import com.app.session.fcm.NotificationUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationUtils notificationUtils;
    boolean flagNoti;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {


         {
            //Your App Code
            System.out.println("hello applozic");
            flagNoti = true;
            System.out.println("onMessageReceived before");
            Map<String, String> data = remoteMessage.getData();
            System.out.println("onMessageReceived after");
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            Log.e(TAG, "Key: " + remoteMessage.getCollapseKey());
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Log.e(TAG, "Size: " + remoteMessage.getData().size());

            if (remoteMessage.getData().size() > 0) {
                String type = remoteMessage.getData().get("type");
                String session_id = remoteMessage.getData().get("sesseion_id");
                String token = remoteMessage.getData().get("token");
                String case_id = remoteMessage.getData().get("case_id");
                String patient_id = remoteMessage.getData().get("patient_id");

                System.out.println("Type: " + type);
                Log.e(TAG, "Type: " + type);

                if (type.equals(Constant.CALL)) {

                    Intent intent = new Intent(getBaseContext(), CallIncomingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }


            if (remoteMessage == null)
                return;

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                Log.e(TAG, "Notification title: " + remoteMessage.getNotification().getTitle());
                String title = "";
                title = remoteMessage.getNotification().getTitle();//video calling
                String body = remoteMessage.getNotification().getBody();///{"_message":"False","from_user_cd":"25","user_name":"User","video_sessionId":"","video_tokenId":""}
                String message = "";
                DataPrefrence.setPref(getBaseContext(), Constant.CALLING_TYPE, title);
                try {
                    if (title.equals(Constant.AUDIO) || title.equals(Constant.VIDEO)) {
                        JSONObject jsonObject = new JSONObject(body);

                        if (jsonObject.has("_message")) {

                            if (jsonObject.getString("_message").equals("Incoming Video Calling")) {
                                title = jsonObject.getString("_message");
                            } else {
                                message = jsonObject.getString("_message");
                            }
                        }
                        if (jsonObject.has("from_user_cd")) {
                            if (jsonObject.getString("from_user_cd") != null) {
                                DataPrefrence.setPref(getBaseContext(), Constant.FROM_USER_CD, jsonObject.getString("from_user_cd"));
                            }
                        }
                        if (jsonObject.has("user_name")) {
                            if (jsonObject.getString("user_name") != null) {
                                DataPrefrence.setPref(getBaseContext(), Constant.FROM_USER_NAME, jsonObject.getString("user_name"));
                            }
                        }
                        if (jsonObject.has("video_sessionId")) {
                            if (jsonObject.getString("video_sessionId") != null) {
                                DataPrefrence.setPref(getBaseContext(), Constant.VIDEO_SESSIONID, jsonObject.getString("video_sessionId"));
                            }
                        }
                        if (jsonObject.has("video_tokenId")) {
                            if (jsonObject.getString("video_tokenId") != null) {
                                DataPrefrence.setPref(getBaseContext(), Constant.VIDEO_TOKENID, jsonObject.getString("video_tokenId"));
                            }
                        }
                        System.out.println("onMessageReceived 000 ");
                        Intent intent = new Intent(getBaseContext(), CallIncomingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        System.out.println("onMessageReceived 1111 ");
                    } else if (title.equals(Constant.DISCONNECT_VIDEO) || title.equals(Constant.DISCONNECT_AUDIO)) {
                        DataPrefrence.setPref(getBaseContext(), Constant.VIDEO_TOKENID, "");
                        DataPrefrence.setPref(getBaseContext(), Constant.VIDEO_SESSIONID, "");
                        Intent intent = new Intent(Constant.CUSTOME_BROADCAST);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    } else {
                        JSONObject jsonObject = new JSONObject(body);
                        message = jsonObject.getString("_message");
                        if (title.equals("video calling") || title.equals("audio calling")) {
                            sendMessage(title, body);
                        } else if (title.equals("accept video") || title.equals("accept audio")) {
                            sendMessage(title, body);
                        } else if (title.equals("accept video") || title.equals("accept audio")) {
                            sendMessage(title, body);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (title.equals("video calling") || title.equals("audio calling") || title.equals("accept video") || title.equals("accept audio")) {

                } else {
                    handleNotification(title, message);
                }
                //  String title = remoteMessage.getNotification().getTitle();

//          if(!DataPrefrence.getPref(getApplicationContext(), Constant.CLASS_TAG,false)) {
//              handleNotification(title, body);
//          }
//          else
//          {
//              boolean tag=DataPrefrence.getPref(getApplicationContext(), Constant.CLASS_TAG,false);
//              System.out.println("the tag is "+tag);
//          }

            }

        }
        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("custome msg")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round).setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        else
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // SharedPreferencesMethod.setInt(getApplicationContext(), SharedPreferencesMethod.NO_OF_NOTIFICATION, SharedPreferencesMethod.getInt(getApplicationContext(), SharedPreferencesMethod.NO_OF_NOTIFICATION) + 1);
        notificationManager.notify(0, notificationBuilder.build());


    }

    private void handleNotification(String title, String message) {

//        if (!title.equals("you have message!"))
        {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.showNotificationMessageComman(title, message, "", pushNotification, "");


        }
//        if (Utility.isBackground) {
//            // app is in foreground, broadcast the pu sh message
//            pushNotification.putExtra("message", message);
////            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
//        }else{
//            pushNotification.putExtra("message", message);
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.showNotificationMessageComman(title,message,"",pushNotification,"");
//            // If the app is in background, firebase itself handles the notification
//        }
    }


    public void show(Intent intent, String title, String msg) {

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            }
//            else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    @Override
    public void handleIntent(Intent intent) {
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }




    private void sendMessage(String title, String data) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(Constant.CUSTOME_BROADCAST);
        // You can also include some extra data.
        intent.putExtra("title", title);
        intent.putExtra("message", data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



}