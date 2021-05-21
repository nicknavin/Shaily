package com.app.session.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.app.session.R;
import com.app.session.activity.ChattingActivity;
import com.app.session.activity.VideoChatViewActivity;
import com.app.session.activity.VoiceChatViewActivity;
import com.app.session.api.Urls;
import com.app.session.data.model.AudioVideoData;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.bumptech.glide.Glide;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyForgroundService extends Service implements SocketEventListener.Listener {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String CHANNEL_ID_AUDIO_CALL = "com.app.session.service.audiocall";
    public static final String CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL = "com.app.session.service.missedcall";
    public static final String CHANNEL_ID_MESSAGE = "com.app.session.service.message";
    public static final int NOTIFICATION_ID_AUDIO_VIDEO_CALL = 2;
    public static final int NOTIFICATION_ID_AUDIO_VIDEO_CALL_END = 3;
    public static final int NOTIFICATION_ID_MESSAGE = 4;
    public static final int NOTIFICATION_ID_MESSAGE_FILE = 5;
    public static final String KEY_BROADCAST_MESSAGE_AUDIO_VIDEO_CALLING = "session_message_audio_video_calling";
    private static final String TAG = "myTag";
    public static final String EXTRA_USER_NAME = "extra_user_name";
    public static final String EXTRA_EVENT_TYPE = "extra_event_type";
    public static final int EVENT_TYPE_JOIN = 1, EVENT_TYPE_MESSAGE = 2, EVENT_TYPE_TYPING = 3, EVENT_VIDEO_CALLING = 4, EVENT_CALL_DISCONNET = 5, EVENT_CALL_DISCONNET_CALLER = 6;
    private Socket mSocket;
    private String userId;
    private Boolean isConnected = true;
    private MediaPlayer mPlayer;
//96817941
    @Override
    public void onCreate() {
        super.onCreate();
        userId = DataPrefrence.getPref(this, Constant.USER_ID, "");
        connectWithSocket();


    }

    NotificationManager manager = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "onStartCommand ");
        createNotificationChannel();
        showNotification("Sessionway", "");
        if (intent.getAction() != null)
        {
            switch (intent.getAction()) {
                case Constant.CONNECT_CALL:
                    Log.d(TAG, "Constant.CONNECT_CALL ");
                    break;
                case Constant.DISCONNECT_CALL:
                    manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(NOTIFICATION_ID_AUDIO_VIDEO_CALL);
                    String data = intent.getStringExtra("DATA");
                    Log.d(TAG, " EVENT_CALL_DISCONNET " + data);
                    callDisconnectSocket(data);
                    Log.d(TAG, "Constant.DISCONNECT_CALL ");
                    break;
                case Constant.DISCONNECT_MISSED_CALL:
                    manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(NOTIFICATION_ID_AUDIO_VIDEO_CALL_END);
                    break;
                case Constant.CALL_BACK:

                    try {

                        int notificationId = intent.getIntExtra("notificationId", 0);
                        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(notificationId);
                        String request = intent.getStringExtra("DATA");
                        AudioVideoData audioVideoData=new AudioVideoData();
                        JSONObject jsonObject=new JSONObject(request);
                        audioVideoData.setReciverName(jsonObject.getString("callerName"));
                        audioVideoData.setCallType(jsonObject.getString("callingType"));
                        audioVideoData.setProfileUrl(jsonObject.getString("ProfileUrl"));
                        audioVideoData.setCallerName(jsonObject.getString("reciverName"));
                        audioVideoData.setAgoraTockenID(jsonObject.getString("agoraTockenID"));
                        audioVideoData.setAgorachannelName(jsonObject.getString("agorachannelName"));
                        audioVideoData.setUserId(jsonObject.getString("reciverId"));
                        audioVideoData.setReciverId(jsonObject.getString("callerID"));


                        try {
                            Gson gson = new Gson();
                            String callData = gson.toJson(audioVideoData);
                            Log.d(TAG, request);
                            JSONObject object = new JSONObject(callData);
                            mSocket.emit(Constant.CALL_NOTIFICATION, object);
                            Intent callingintent1=null;
                            if(object.getString("callType").equals("audio"))
                            {

                                callingintent1 = new Intent(this, VoiceChatViewActivity.class);
                            }
                            else {

                                callingintent1 = new Intent(this, VideoChatViewActivity.class);
                            }

                            callingintent1.putExtra("DATA", request);
                            callingintent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callingintent1);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
            }
        }
        if (intent != null) {
//            Message chat = intent.getParcelableExtra(EXTRA_DATA);
            int eventType = intent.getIntExtra(EXTRA_EVENT_TYPE, EVENT_TYPE_JOIN);
            Log.d(TAG, "" + eventType);
            switch (eventType) {
                case EVENT_TYPE_JOIN:

                    userId = intent.getStringExtra("userId");
                    if (!mSocket.connected()) {
                        mSocket.connect();
                        Log.i(TAG, "connecting socket...");
                    } else {
                        joinChat();
                    }
                    break;

                case EVENT_VIDEO_CALLING:

                    AudioVideoData audioVideoData = intent.getParcelableExtra("DATA");
                    String recieverName = intent.getStringExtra("RECEIVER_NAME");
                    if (isSocketConnected())
                    {
                     sendVideoCallNotification(audioVideoData);
                    }

                    break;

                case EVENT_TYPE_MESSAGE:
                    if (isSocketConnected()) {
//                        if (chat.getMessageType() == MessageType.PICTURE)
//                        {
////                            sendPictureImage(chat, eventType);
//                        }
//                        else
//                        {
////                            sendMessage(chat, eventType);
//                        }

                    } else {

                    }
                    break;
                case EVENT_TYPE_TYPING:
                    if (isSocketConnected()) {


                    }
                    break;

                case EVENT_CALL_DISCONNET:
                    if (isSocketConnected()) {
                        String data = intent.getStringExtra("DATA");
                        Log.d(TAG, " EVENT_CALL_DISCONNET " + data);
                        callDisconnectSocket(data);
                    }
                    break;
                case EVENT_CALL_DISCONNET_CALLER:
                    if (isSocketConnected()) {
                        String data = intent.getStringExtra("DATA");
                        Log.d(TAG, " EVENT_CALL_DISCONNET_CALLER " + data);
                        callDisconnectFromCaller(data);
                    }
                    break;
            }

        }


        //do heavy work on a background thread
        //stopSelf();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void connectWithSocket() {
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{Polling.NAME};
//            Socket mSocket = IO.socket("http://example.com/", opts);
            mSocket = IO.socket(Constant.CHAT_SERVER_URL, opts);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        //mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_CONNECT, new SocketEventListener(Socket.EVENT_CONNECT, this));
        mSocket.on(Socket.EVENT_DISCONNECT, new SocketEventListener(Socket.EVENT_DISCONNECT, this));
        mSocket.on(Socket.EVENT_CONNECT_ERROR, new SocketEventListener(Socket.EVENT_CONNECT_ERROR, this));
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new SocketEventListener(Socket.EVENT_CONNECT_TIMEOUT, this));
        mSocket.on(Constant.NEW_MESSAGE, new SocketEventListener(Constant.NEW_MESSAGE, this));
        mSocket.on(Constant.FILES_EVENT, new SocketEventListener(Constant.FILES_EVENT, this));
        mSocket.on(Constant.IS_ON, new SocketEventListener(Constant.IS_ON, this));
        mSocket.on(Constant.TYPING, new SocketEventListener(Constant.TYPING, this));
        mSocket.on(Constant.CALL_NOTIFY, new SocketEventListener(Constant.CALL_NOTIFY, this));
        mSocket.on(Constant.CALL_DISCONNECT_NOTIFY, new SocketEventListener(Constant.CALL_DISCONNECT_NOTIFY, this));
        mSocket.on(Constant.CALL_DISCONNECT_CALLED_END_NOTIFY, new SocketEventListener(Constant.CALL_DISCONNECT_CALLED_END_NOTIFY, this));
        mSocket.on(Constant.FILES_EVENT, new SocketEventListener(Constant.FILES_EVENT, this));
        mSocket.connect();
    }

    public void callDisconnectSocket(String req) {
        try {
            Log.d(TAG, " EVENT_CALL_DISCONNET EMIT CALL");
            JSONObject jsonObject = new JSONObject(req);
            JSONObject object = new JSONObject();
            object.put("callerID", jsonObject.getString("userId"));
            object.put("reciverId", jsonObject.getString("reciverId"));
            object.put("callingType", jsonObject.getString("callType"));
            object.put("reciverName", "");

            mSocket.emit(Constant.CALL_DISCONNECT, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callDisconnectFromCaller(String req) {
        try {
            Log.d(TAG, " EVENT_CALL_DISCONNET EMIT CALL");
            JSONObject jsonObject = new JSONObject(req);
            JSONObject object = new JSONObject();
            object.put("callerID", jsonObject.getString("userId"));
            object.put("reciverId", jsonObject.getString("reciverId"));
            object.put("callingType", jsonObject.getString("callType"));
            object.put("reciverName", jsonObject.getString("reciverName"));
            object.put("callerName", jsonObject.getString("callerName"));
            object.put("ProfileUrl", jsonObject.getString("ProfileUrl"));
            object.put("agoraTockenID", jsonObject.getString("agoraTockenID"));
            object.put("agorachannelName", jsonObject.getString("agorachannelName"));


            mSocket.emit(Constant.CALL_DISCONNECT_CALLER_END, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void joinChat() {
        if (TextUtils.isEmpty(userId)) {
            // user name is null can not join chat
            return;
        }


        try {
            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("deviceType", "mobile");
            mSocket.emit(Constant.EVENT_JOIN, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private boolean isSocketConnected() {
        if (null == mSocket) {
            return false;
        }
        if (!mSocket.connected()) {
            mSocket.connect();
            Log.i(TAG, "reconnecting socket...");
            return false;
        }

        return true;
    }


    private void sendVideoCallNotification(AudioVideoData audioVideoData) {
        try {
            Gson gson = new Gson();
            String request = gson.toJson(audioVideoData);
            Log.d(TAG, request);
            Intent intent = new Intent(KEY_BROADCAST_MESSAGE_AUDIO_VIDEO_CALLING);
            intent.putExtra("REQUEST", "caller");
            intent.putExtra("TYPE", "calling");
            intent.putExtra("DATA", request);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            JSONObject jsonObject = new JSONObject(request);
            mSocket.emit(Constant.CALL_NOTIFICATION, jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


//        Intent intent = new Intent(this, VideoChatViewActivity.class);
//        startActivity(intent);
    }


    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

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

        Intent declineIntent = new Intent(this, MyForgroundService.class);
        declineIntent.setAction(Constant.DISCONNECT_CALL);
        declineIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL);
        declineIntent.putExtra("DATA", request);


        PendingIntent declinePendingIntent = PendingIntent.getService(this,
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
//{"callerID":"5f437ef1c4b8ce176fc80a91","reciverId":"5f3cfba45a1d392b092e3fb8","callingType":"audio","reciverName":""}

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
        try {
             bitmap= Glide.with(this).asBitmap()
                     .load(profileUrl).submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//
//        /**Creates an explicit intent for an Activity in your app**/
//
//        connectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        connectIntent.putExtra("DATA", request);
//        connectIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL_END);
//        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
//                0 /* Request code */, connectIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);




        Intent declineIntent = new Intent(this, MyForgroundService.class);
        declineIntent.setAction(Constant.DISCONNECT_MISSED_CALL);
        declineIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL_END);
        declineIntent.putExtra("DATA", request);
        PendingIntent declinePendingIntent = PendingIntent.getService(this,
                0 /* Request code */, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent callBackIntent = new Intent(this, MyForgroundService.class);
        callBackIntent.setAction(Constant.CALL_BACK);
        callBackIntent.putExtra("notificationId", NOTIFICATION_ID_AUDIO_VIDEO_CALL_END);
        callBackIntent.putExtra("DATA", request);
        PendingIntent callBackPendingIntent = PendingIntent.getService(this,
                0 /* Request code */, callBackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long when = System.currentTimeMillis();
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL);
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
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL, "default", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            notificationChannel.setImportance(importance);
            notificationChannel.setShowBadge(true);



            mBuilder.setChannelId(CHANNEL_ID_AUDIO_VIDEO_MISSED_CALL);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(NOTIFICATION_ID_AUDIO_VIDEO_CALL_END, mBuilder.build());

    }

    private static final String KEY_NOTIFICATION_GROUP = "KEY_NOTIFICATION_GROUP";
    int bundleNotificationId = 100;
    int singleNotificationId = 100;
    public void createMessageNotification(String request)
    {
        Intent connectIntent = null;
        String title = "", message = "",profileUrl="",receiverID="",receiverName="",channelID="",groupID="",senderID="",groupName="";


        try {
            JSONObject object = new JSONObject(request);
            title = object.getString("senderName");
            message = object.getString("message");
            receiverID=object.getString("senderId");
            senderID=object.getString("reciverId");
            channelID=receiverID;
            groupID=receiverID+senderID;
            groupName=title;
            receiverName=object.getString("senderName");
            profileUrl=object.getString("senderProfileUrl");
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

    public void createMessageFileNotification(String request,Bitmap bitmapImage)
    {
        Intent connectIntent = null;
        String title = "", messageType = "",profileUrl="",receiverID="",receiverName="",channelID="",imageUrl="";
        String groupName="";


        try {
            JSONObject object = new JSONObject(request);
            title = object.getString("senderName");
            messageType = object.getString("messageType");
            receiverID=object.getString("senderId");
            groupName=receiverID;
            channelID=receiverID;
            receiverName=object.getString("senderName");
            profileUrl=object.getString("senderProfileUrl");
            imageUrl= Urls.BASE_IMAGES_URL +object.getString("file");
            connectIntent = new Intent(this, ChattingActivity.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=null,bitmapFile=null;
        try {
            if(!profileUrl.isEmpty()) {
                bitmap = Glide.with(this).asBitmap()
                        .load(profileUrl).submit().get();
            }
           // bitmapFile=Glide.with(this).asBitmap().load(imageUrl).submit().get();

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
        connectIntent.putExtra("notificationId", NOTIFICATION_ID_MESSAGE_FILE);

        PendingIntent connecPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, connectIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();




        mBuilder = new NotificationCompat.Builder(this, channelID);
        mBuilder.setSmallIcon(R.drawable.app_new_icon);
        mBuilder.setContentTitle(title)
                .setContentText(messageType)
                .setAutoCancel(false)
                .setWhen(when)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapImage))
                .setSound(ring)
                .setGroup(groupName)
                .setGroupSummary(true)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(connecPendingIntent);


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannelGroup notificationChannelGroup=new NotificationChannelGroup(bundle_notification_id, receiverName);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelID, "Messages", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(importance);
            notificationChannel.setShowBadge(true);
            notificationChannel.setGroup(groupName);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(channelID);
            mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        assert mNotificationManager != null;
        mNotificationManager.notify(NOTIFICATION_ID_MESSAGE_FILE, mBuilder.build());

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






    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mPlayer.release();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void play() {
        mPlayer.start();
    }

    public void pause() {
        mPlayer.pause();
    }

    private class getBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;
        String data;
        String imageUrl;
        public getBitmapFromUrl(Context context,String request, String imageUrl) {
            super();
            this.ctx = context;
            data=request;
            this.imageUrl=imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
//            message = params[0] + params[1];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);

            createMessageFileNotification(data,result);


        }
    }



    @Override
    public void onEventCall(String event, Object... objects)
    {
        switch (event) {
            case Socket.EVENT_CONNECT:
                joinChat();
                break;
            case Socket.EVENT_DISCONNECT:
                mSocket.connect();
                joinChat();
                break;
            case Socket.EVENT_CONNECT_ERROR:
                mSocket.connect();
                break;
            case Socket.EVENT_CONNECT_TIMEOUT:
                mSocket.connect();
                break;

            case Constant.NEW_MESSAGE:
                Log.d(TAG, "Constant.NEW_MESSAGE " + objects[0].toString());

                //{"message":"regards David David I am not sure if you have any questions or","senderId":"5f3cfba45a1d392b092e3fb8","senderName":"demo1","reciverName":"demo5","messageType":"text","createdAt":"2021-01-08T07:53:59.925Z","isRead":true}

                String msg=objects[0].toString();
                try {
                    JSONObject jsonObject=new JSONObject(msg);
                    if(!jsonObject.getString("senderId").equals(userId))
                    {
                        createMessageNotification(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case Constant.FILES_EVENT:
                Log.d(TAG, "FILES_EVENT " + objects[0].toString());
                String result=objects[0].toString();
                String url="";
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(!jsonObject.getString("senderId").equals(userId))
                    {

                        if(jsonObject.getString("messageType").equals("image"))
                        {
                            url=Urls.BASE_IMAGES_URL +jsonObject.getString("file");
                            new getBitmapFromUrl(this, result, url).execute();
                        }
                        else
                        {

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constant.CALL_NOTIFY:
                try {
                    Log.d(TAG, "run : my service " + objects[0].toString());
                    JSONObject obj = (JSONObject) objects[0];
                    String imageUrl=obj.getString("ProfileUrl");
                    String request = obj.toString();
                    createAudioVideoNotification(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case Constant.CALL_DISCONNECT_NOTIFY:
                Log.d(TAG, "run : CALL_DISCONNECT_NOTIFY " + objects[0].toString());
                try {

                    JSONObject jsonObject = new JSONObject(objects[0].toString());
                    if (jsonObject.getString("callingType").equals("audio")) {

                        Intent intent = new Intent(Constant.DISCONNECT_AUDIO);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    } else {

                        Intent intent = new Intent(Constant.DISCONNECT_VIDEO);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //{"callerID":"5f3cfba45a1d392b092e3fb8","reciverId":"5f413256c4b8ce176fc80a8f","callingType":"audio","reciverName":""}
                break;
            case Constant.CALL_DISCONNECT_CALLED_END_NOTIFY:
                Log.d(TAG, "run : CALL_DISCONNECT_CALLED_END_NOTIFY " + objects[0].toString());
                try {
                    JSONObject jsonObject = new JSONObject(objects[0].toString());

                    if (jsonObject.getString("callingType").equals("audio")) {
                        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(NOTIFICATION_ID_AUDIO_VIDEO_CALL);
                        Intent intent = new Intent(Constant.CUSTOME_BROADCAST);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    } else {
                        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(NOTIFICATION_ID_AUDIO_VIDEO_CALL);
                        Intent intent = new Intent(Constant.CUSTOME_BROADCAST);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }
                    createMissedAudioVideoNotification(jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //{"callerID":"5f3cfba45a1d392b092e3fb8","reciverId":"5f413256c4b8ce176fc80a8f","callingType":"audio","reciverName":""}
                break;

            case Constant.IS_ON:
                //{"reciverId":"5f437ef1c4b8ce176fc80a91","isOn":false}
                try {
                    JSONObject jsonObject = new JSONObject(objects[0].toString());

                    Log.d("tag",jsonObject.toString());



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;



        }
    }




}



