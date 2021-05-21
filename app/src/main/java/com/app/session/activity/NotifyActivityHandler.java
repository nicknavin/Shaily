package com.app.session.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.app.session.data.model.AudioVideoData;
import com.app.session.data.model.NotificationModel;
import com.app.session.utility.Constant;
import com.app.session.utility.Foreground;
import com.app.session.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.app.session.utility.Foreground.TAG;

public class NotifyActivityHandler extends Activity {
    public static final String PERFORM_NOTIFICATION_BUTTON = "perform_notification_button";
    public static final int NOTIFICATION_ID_AUDIO_VIDEO_CALL = 2;
    public static final int NOTIFICATION_ID_AUDIO_VIDEO_CALL_END = 3;
    public static final int NOTIFICATION_ID_MESSAGE = 4;
    public static final int NOTIFICATION_ID_MESSAGE_FILE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
getIntentData();

        finish();
    }

    private void getIntentData()
    {
        NotificationManager manager=null;
        Intent intent=getIntent();

        if (getIntent().getStringExtra("KEY") != null)
        {
            String key =getIntent().getStringExtra("KEY");
            switch (key)
            {

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
                            if (Foreground.get().getmSocket() != null) {
                                Foreground.get().getmSocket().emit(Constant.CALL_NOTIFICATION, object);
                            }
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

        if(intent.getAction()!=null)
        {
            switch (intent.getAction())
            {
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
                            if (Foreground.get().getmSocket() != null) {
                                Foreground.get().getmSocket().emit(Constant.CALL_NOTIFICATION, object);
                            }
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

                            sendPushNotification(audioVideoData);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
            }
        }

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
            if (Foreground.get().getmSocket() != null) {
                Foreground.get().getmSocket().emit(Constant.CALL_DISCONNECT, object);
            }
            sendPushNotification(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






public void sendPushNotification(AudioVideoData audioVideoData)
    {
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        String body = gson.toJson(audioVideoData);
        String msg="";
        if(audioVideoData.getCallType().equals("audio"))
        {
            msg="Incoming voice call";
        }
        else
        {
            msg="Incoming video call";
        }
        // notificationModel.notification.body = msg;
        //notificationModel.notification.title = audioVideoData.getCallerName();
        notificationModel.data.title = audioVideoData.getCallerName();
        notificationModel.data.message = msg;
        notificationModel.data.body = body;

//        notificationModel.to = "/topics/" +"rPg55d1dciOafYF3P8eFI2PHJng2";//navin
        notificationModel.to = "/topics/" +audioVideoData.getReciverId();//iron
        okhttp3.RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),
                gson.toJson(notificationModel));
        okhttp3.Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + Constant.KEY)//key = your Database Key
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        okhttp3.OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String data = responseBody.string();
                String result = requestBody.toString();

                Utility.log(result);
                Utility.log(data);

            }
        });

    }

    public void sendPushNotification(JSONObject  jsonObject)
    {
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();

        String msg="";
        String id="";
        try {
            id =jsonObject.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // notificationModel.notification.body = msg;
        //notificationModel.notification.title = audioVideoData.getCallerName();
        notificationModel.data.title = "finish";
        notificationModel.data.message = "finish";
        notificationModel.data.body = jsonObject.toString();

//        notificationModel.to = "/topics/" +"rPg55d1dciOafYF3P8eFI2PHJng2";//navin
        notificationModel.to = "/topics/" +id;//iron
        okhttp3.RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),
                gson.toJson(notificationModel));
        okhttp3.Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + Constant.KEY)//key = your Database Key
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        okhttp3.OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String data = responseBody.string();
                String result = requestBody.toString();

                Utility.log(result);
                Utility.log(data);

            }
        });

    }




}
