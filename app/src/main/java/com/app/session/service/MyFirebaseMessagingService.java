package com.app.session.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;


import com.app.session.R;
import com.app.session.activity.ChattingActivity;
import com.app.session.activity.NotifyActivityHandler;
import com.app.session.activity.VideoChatViewActivity;
import com.app.session.activity.VoiceChatViewActivity;
import com.app.session.api.Urls;
import com.app.session.data.model.SendChatMsg;
import com.app.session.utility.Constant;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    public static final String CHANNEL_ID = "Sessionway";
    public static final int NOTIFICATION_ID_ORDER = 1;
    public static final int NOTIFICATION_ID_MESSAGE_FILE = 5;
    public static final int NOTIFICATION_ID_AUDIO_VIDEO_CALL = 2;
    public static final int NOTIFICATION_ID_AUDIO_VIDEO_CALL_END = 3;

    public static final String CHANNEL_ID_AUDIO_CALL = "com.app.session.service.audiocall";
    public static final String CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL = "com.app.session.service.missedcall";
    private static final String TAG = "tag";

    int bundleNotificationId = 100;
    int singleNotificationId = 100;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
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
        String body = remoteMessage.getData().get("body");
//        String body = remoteMessage.getNotification().getBody();

        Log.e("body..", body);
//        Order order = gson.fromJson(body, Order.class);
     //   Log.e("result..", order.getCustomerName());
        if(message.contains("Incoming voice call")||message.contains("Incoming video call"))
        {
            createAudioVideoNotification(body);
        }
        if(body.contains("callingType"))
        {
            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(NOTIFICATION_ID_AUDIO_VIDEO_CALL);
            if(message.equals("Missed voice call")||message.equals("Missed video call"))
            {
                createMissedAudioVideoNotification(body);
            }

        }
        if (body.contains("isFile")){
            try {
                JSONObject jsonObject=new JSONObject(body);

                if(jsonObject.getBoolean("isFile"))
                {
                   String url=Urls.BASE_IMAGES_URL +jsonObject.getString("fileUrl");
                   String urlProfile=jsonObject.getString("senderProfileUrl");

                    new getBitmapFromUrl(this, body, url,urlProfile).execute();
                }
                else
                {
                    createMessageNotification(body);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(message.contains("finish"))
        {
            try {

                JSONObject jsonObject = new JSONObject(body);
                if (jsonObject.getString("callType").contains("audio")) {
                    Intent intent = new Intent(Constant.DISCONNECT_AUDIO);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                } else
                {
                    Intent intent = new Intent(Constant.DISCONNECT_VIDEO);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


    public void createMessageNotification(String request)
    {
        Intent connectIntent = null;
        String title = "", message = "",profileUrl="",receiverID="",receiverName="",channelID="",groupID="",senderID="",groupName="";

        SendChatMsg sendChatMsg=null;
        try {
            Gson gson = new Gson();

            JSONObject object = new JSONObject(request);
             sendChatMsg= gson.fromJson(object.toString(), SendChatMsg.class);
           String msg= sendChatMsg.getMessage();
            title = sendChatMsg.getSenderName();
            message = sendChatMsg.getMessage();
            receiverID=sendChatMsg.getUserId();//object.getString("senderId");
            senderID=sendChatMsg.getReceiverID();
            receiverName=sendChatMsg.getSenderName();//object.getString("senderName");
            profileUrl=sendChatMsg.getSenderProfileUrl();//object.getString("senderProfileUrl");
            channelID=receiverID;
            groupID=receiverID+senderID;
            groupName=title;

            connectIntent = new Intent(this, ChattingActivity.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=null;
        try {
            bitmap= Glide.with(this).asBitmap()
                    .load(profileUrl).submit().get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**Creates an explicit intent for an Activity in your app**/
        bundleNotificationId += 100;
        singleNotificationId = bundleNotificationId;
        String bundle_notification_id = "bundle_notification_" + bundleNotificationId;

        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        connectIntent.putExtra("DATA", request);
        connectIntent.putExtra("NAME",receiverName);
        connectIntent.putExtra("ID",receiverID);
        connectIntent.putExtra("CALLING_TYPE","receiver");
        connectIntent.putExtra("notificationId", bundleNotificationId);

        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, connectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();




        mBuilder = new NotificationCompat.Builder(this, channelID);
        mBuilder.setSmallIcon(R.drawable.app_new_icon);
        mBuilder.setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(false)
                .setWhen(when)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setLargeIcon(bitmap)
                .setSound(ring)
                .setGroup(groupID)
                .setGroupSummary(true)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(connecPendingIntent);




        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {

            NotificationChannelGroup notificationChannelGroup=new NotificationChannelGroup(groupID, groupName);
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



    public void createMessageFileNotification(String request,List<Bitmap>bitmapList)
    {
        Intent connectIntent = null;
        String title = "", message = "",profileUrl="",messageType="",receiverID="",receiverName="",channelID="",groupID="",senderID="",groupName="";
        Bitmap bitmapImage=null, bitmapProfile=null;
        SendChatMsg sendChatMsg=null;
        try {
            Gson gson = new Gson();

            JSONObject object = new JSONObject(request);
            sendChatMsg= gson.fromJson(object.toString(), SendChatMsg.class);
            String msg= sendChatMsg.getMessage();
            title = sendChatMsg.getSenderName();
            message = sendChatMsg.getMessage();
            messageType=sendChatMsg.getMessageType();
            receiverID=sendChatMsg.getUserId();//object.getString("senderId");
            senderID=sendChatMsg.getReceiverID();
            receiverName=sendChatMsg.getSenderName();//object.getString("senderName");
            profileUrl=sendChatMsg.getSenderProfileUrl();//object.getString("senderProfileUrl");
            channelID=receiverID;
            groupID=receiverID+senderID;
            groupName=title;

            connectIntent = new Intent(this, ChattingActivity.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(bitmapList.size()>0)
        {
            bitmapImage=bitmapList.get(0);

        }

        if(bitmapList.size()>1)
        {
            bitmapProfile=bitmapList.get(1);
        }
//        try {
//            bitmap= Glide.with(this).asBitmap()
//                    .load(profileUrl).submit().get();
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        /**Creates an explicit intent for an Activity in your app**/
        bundleNotificationId += 100;
        singleNotificationId = bundleNotificationId;
        String bundle_notification_id = "bundle_notification_" + bundleNotificationId;

        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        connectIntent.putExtra("DATA", request);
        connectIntent.putExtra("NAME",receiverName);
        connectIntent.putExtra("ID",receiverID);
        connectIntent.putExtra("CALLING_TYPE","receiver");
        connectIntent.putExtra("notificationId", bundleNotificationId);

        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, connectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();




        mBuilder = new NotificationCompat.Builder(this, channelID);
        mBuilder.setSmallIcon(R.drawable.app_new_icon);
        mBuilder.setContentTitle(title)
                 .setContentText(messageType)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(false)
                .setWhen(when)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setLargeIcon(bitmapProfile)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapImage))
                .setSound(ring)
                .setGroup(groupID)
                .setGroupSummary(true)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(connecPendingIntent);




        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {

            NotificationChannelGroup notificationChannelGroup=new NotificationChannelGroup(groupID, groupName);
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



    public void createAudioVideoNotification(String request)
    {
        Intent connectIntent = null;
        String title = "", message = "",profileUrl="";

        try {
            JSONObject object = new JSONObject(request);
            title = object.getString("callerName");
            profileUrl = object.getString("ProfileUrl");

            if (object.getString("callType").equals("audio"))
            {
                message = "Incoming voice call";
                connectIntent = new Intent(this, VoiceChatViewActivity.class);
            } else
            {
                message = "Incoming video call";
                connectIntent = new Intent(this, VideoChatViewActivity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=null;
        try {
            bitmap= Glide.with(this).asBitmap()
                    .load(profileUrl).submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**Creates an explicit intent for an Activity in your app**/

        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        connectIntent.putExtra("DATA", request);
        connectIntent.putExtra("CALLING_TYPE","receiver");
        connectIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL);

        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, connectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent declineIntent = new Intent(this, NotifyActivityHandler.class);
        declineIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        declineIntent.putExtra("KEY", Constant.DISCONNECT_CALL);
        declineIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL);
        declineIntent.putExtra("DATA", request);


        PendingIntent declinePendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_AUDIO_CALL);
        mBuilder.setSmallIcon(R.drawable.app_new_icon);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setWhen(when)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setOngoing(true)
                .setLargeIcon(bitmap)
                .setSound(ring)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(0, "Decline", declinePendingIntent)
                .addAction(0, "Answer", connecPendingIntent);


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_AUDIO_CALL, "Call notifications", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(importance);
            notificationChannel.setSound(ring, att);
            notificationChannel.setShowBadge(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(CHANNEL_ID_AUDIO_CALL);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(NOTIFICATION_ID_AUDIO_VIDEO_CALL, mBuilder.build());

    }


    public void createMissedAudioVideoNotification(String request) {


        Log.d(TAG, "MISSED " + request);
        Intent connectIntent = null;
        String title = "", message = "",profileUrl="";

        try {
            JSONObject object = new JSONObject(request);
            title = object.getString("callerName");
            profileUrl=object.getString("ProfileUrl");

            if (object.getString("callingType").equals("audio")) {
                message = "Missed voice call";
//                connectIntent = new Intent(this, VoiceChatViewActivity.class);
            } else {
                message = "Missed video call";
//                connectIntent = new Intent(this, VideoChatViewActivity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=null;
//        try {
//            bitmap= Glide.with(this).asBitmap()
//                    .load(profileUrl).submit().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//
//        /**Creates an explicit intent for an Activity in your app**/
//
//        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        connectIntent.putExtra("DATA", request);
//        connectIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL_END);
//        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
//                0 /* Request code */, connectIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);







        Intent callBackIntent = new Intent(this, NotifyActivityHandler.class);
//        callBackIntent.putExtra("KEY",Constant.CALL_BACK);
        callBackIntent.setAction(Constant.CALL_BACK);
        callBackIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL_END);
        callBackIntent.putExtra("DATA", request);
        PendingIntent callBackPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, callBackIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent declineIntent = new Intent(this, NotifyActivityHandler.class);
//        declineIntent.putExtra("KEY",Constant.DISCONNECT_MISSED_CALL);
        declineIntent.setAction(Constant.DISCONNECT_MISSED_CALL);
        PendingIntent declinePendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        long when = System.currentTimeMillis();
        mBuilder = new NotificationCompat.Builder(this, Constant.CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL);
        mBuilder.setSmallIcon(R.drawable.app_new_icon);
        mBuilder.setLargeIcon(bitmap);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setWhen(when)
                .setAutoCancel(true)
                .setOngoing(false)
                .setPriority(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(0, "Decline", declinePendingIntent)
                .addAction(0, "Call back", callBackPendingIntent);


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(Constant.CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL, "default", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            notificationChannel.setImportance(importance);
            notificationChannel.setShowBadge(true);



            mBuilder.setChannelId(Constant.CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(NOTIFICATION_ID_AUDIO_VIDEO_CALL_END, mBuilder.build());

    }


    private void showNotification(String title, String input) {
//        Intent notificationIntent = new Intent(this, ChattingActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(input)
                .setSmallIcon(R.drawable.app_new_icon)
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
        }
    }



    private class getBitmapFromUrl extends AsyncTask<String, Void, List<Bitmap>> {

        Context ctx;
        String message;
        String data;
        String imageUrl,profileUrl;

        public getBitmapFromUrl(Context context,String request, String imageUrl,String profileUrl)
        {
            super();
            this.ctx = context;
            data=request;
            this.imageUrl=imageUrl;
            this.profileUrl=profileUrl;
        }

        @Override
        protected List<Bitmap> doInBackground(String... params) {

            InputStream in;
//            message = params[0] + params[1];
            try {
                URL url = new URL(imageUrl);
//                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                in = connection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                List<Bitmap> bitmapList=new ArrayList<>();
                Bitmap  bitmapProfile=null,bitmapImage;
                try {
                    bitmapImage = Glide.with(ctx).asBitmap()
                            .load(imageUrl).submit().get();
                    bitmapList.add(bitmapImage);
                    bitmapProfile = Glide.with(ctx).asBitmap()
                            .load(profileUrl).submit().get();
                    bitmapList.add(bitmapProfile);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return bitmapList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bitmap> result) {

            super.onPostExecute(result);

            createMessageFileNotification(data,result);



        }
    }


}
/*{"isFile":true,"message":"","receiverID":"607fc59f9082c632bde7c001","reciverName":"navin.nimade","reciverProfileUrl":"http://sessionway.com/userFiles/userProfileImage/profilePic-1619020265592-82146.png","senderName":"baniya.nimade","senderProfileUrl":"http://sessionway.com/userFiles/userProfileImage/profilePic-1618987534312-80145.png","userId":"607fc9079082c632bde7c005"}*/