package com.app.session.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.NotificationData;
import com.app.session.data.model.NotificationModel;
import com.app.session.service.MyForgroundService;
import com.app.session.utility.Constant;
import com.app.session.utility.Foreground;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class VoiceChatViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = VoiceChatViewActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;

    private RtcEngine mRtcEngine; // Tutorial Step 1
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a goodbye message. When this message is received, the SDK determines that the user/host leaves the channel.
         *     Drop offline: When no data packet of the user or host is received for a certain period of time (20 seconds for the communication profile, and more for the live broadcast profile), the SDK assumes that the user/host drops offline. A poor network connection may lead to false detections, so we recommend using the Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who
         * leaves
         * the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft(uid, reason);
                    finish();


                }
            });
        }

        /**
         * Occurs when a remote user stops/resumes sending the audio stream.
         * The SDK triggers this callback when the remote user stops or resumes sending the audio stream by calling the muteLocalAudioStream method.
         *
         * @param uid ID of the remote user.
         * @param muted Whether the remote user's audio stream is muted/unmuted:
         *
         *     true: Muted.
         *     false: Unmuted.
         */
        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed)
        {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            showLog(" onJoinChannelSuccess ");
            if(dialRing) {
                startDialerRing();
            }
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            showLog(" onUserJoined ");
            stopDialerRing();
        }
    };

    String data="";
    String tokenId="",name="",reciverName="",profileUrl="";
    private static final String TAG ="myTag";
    Context context;
    CustomTextView txtRecieverName;
    CircleImageView imgProfile;
    boolean dialRing=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_chat_view);
        context=this;
        if(getIntent().getStringExtra("DATA")!=null)
        {
            Log.d(TAG,"11111");
            data=getIntent().getStringExtra("DATA");
            Log.d(TAG,"data "+data);
            try {
                JSONObject jsonObject=new JSONObject(data);
                tokenId= jsonObject.getString("agoraTockenID");
                name=jsonObject.getString("agorachannelName");
                reciverName=jsonObject.getString("reciverName");
                profileUrl=jsonObject.getString("ProfileUrl");

                 if(!jsonObject.isNull("CALLING_TYPE"))
                 {

                     if (jsonObject.getString("CALLING_TYPE").equals("receiver")) {
                         dialRing=false;
                         Log.d(TAG, "data " + jsonObject.getString("CALLING_TYPE"));
                         profileUrl = jsonObject.getString("reciever_profile_url");
                         reciverName = jsonObject.getString("reciverName");
                     }
                 }
                 else
                 {
                    dialRing=true;
                 }





            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(getIntent().getIntExtra("notificationId",0)!=0)
        {
            int notificationId=getIntent().getIntExtra("notificationId",0);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            initAgoraEngineAndJoinChannel();
        }
        initView();
    }


    private void initView()
    {
        txtRecieverName=(CustomTextView)findViewById(R.id.txtRecieverName);
        txtRecieverName.setText(reciverName);
        imgProfile=(CircleImageView)findViewById(R.id.imgProfile);
        Glide.with(context)
                .load(profileUrl)
                .placeholder(R.mipmap.profile_image)
                .error(R.mipmap.profile_image)
                .into(imgProfile);

    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();     // Tutorial Step 1
        joinChannel();               // Tutorial Step 2
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();
                }
                break;
            }
        }
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
stopDialerRing();
        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    // Tutorial Step 7
    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            if(player!=null)
            {
                if(player.isPlaying())
                {
                    player.setVolume(5.0f, 5.0f);
                }

            }
            iv.setImageResource(R.drawable.btn_mute_pressed);
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {

            if(player!=null) {
                if (player.isPlaying()) {
                    player.setVolume(1.05f, 1.05f);
                }
            }
            iv.setImageResource(R.drawable.btn_mute_normal);
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        // Stops/Resumes sending the local audio stream.
        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    // Tutorial Step 5
    public void onSwitchSpeakerphoneClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        // Enables/Disables the audio playback route to the speakerphone.
        //
        // This method sets whether the audio is routed to the speakerphone or earpiece. After calling this method, the SDK returns the onAudioRouteChanged callback to indicate the changes.
        mRtcEngine.setEnableSpeakerphone(view.isSelected());
    }

    // Tutorial Step 3
    public void onEncCallClicked(View view)
    {
        stopDialerRing();

//        Intent service = new Intent(this, MyForgroundService.class);
//        service.putExtra(MyForgroundService.EXTRA_EVENT_TYPE, MyForgroundService.EVENT_CALL_DISCONNET_CALLER);
//        service.putExtra("DATA",data);
//        startService(service);
        callDisconnectFromCaller(data);

        finish();
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
            Foreground.get().getmSocket().emit(Constant.CALL_DISCONNECT_CALLER_END, object);
            sendPushNotification(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void sendPushNotification(JSONObject jsonObject)
    {
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        String body = jsonObject.toString();
        String msg="";

        try {
            if(jsonObject.getString("callingType").equals("audio"))
            {
                msg="Missed voice call";
            }
            else
            {
                msg="Missed video call";
            }
            // notificationModel.notification.body = msg;
            //  notificationModel.notification.title = jsonObject.getString("callerName");//audioVideoData.getCallerName();
            notificationModel.data.title = jsonObject.getString("callerName");
            notificationModel.data.message = msg;
            notificationModel.data.body = body;

//        notificationModel.to = "/topics/" +"rPg55d1dciOafYF3P8eFI2PHJng2";//navin
            notificationModel.to = "/topics/" +jsonObject.getString("reciverId");//audioVideoData.getReciverId();//iron
        } catch (JSONException e) {
            e.printStackTrace();
        }

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




    // Tutorial Step 1
    private void initializeAgoraEngine() {
        try {

            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.private_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    // Tutorial Step 2
    private void joinChannel() {
        showLog(" joinChannel ");
        String accessToken = tokenId;
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "#YOUR ACCESS TOKEN#")) {
            accessToken = null; // default, no token
        }
        
        // Sets the channel profile of the Agora RtcEngine.
        // CHANNEL_PROFILE_COMMUNICATION(0): (Default) The Communication profile. Use this profile in one-on-one calls or group calls, where all users can talk freely.
        // CHANNEL_PROFILE_LIVE_BROADCASTING(1): The Live-Broadcast profile. Users in a live-broadcast channel have a role as either broadcaster or audience. A broadcaster can both send and receive streams; an audience can only receive streams.
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);

        // Allows a user to join a channel.
        mRtcEngine.joinChannel(accessToken, name, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
        //stopDialerRing();
    }

    // Tutorial Step 3
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    // Tutorial Step 4
    private void onRemoteUserLeft(int uid, int reason) {
       // showLongToast(String.format(Locale.US, "user %d left %d", (uid & 0xFFFFFFFFL), reason));
        View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk); // optional UI
       // tipMsg.setVisibility(View.VISIBLE);

    }

    // Tutorial Step 6
    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        //showLongToast(String.format(Locale.US, "user %d muted or unmuted %b", (uid & 0xFFFFFFFFL), muted));
    }


    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver != null) {

            IntentFilter intentFilter = new IntentFilter(Constant.DISCONNECT_AUDIO);

            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver != null) {

            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        }
    }


    MediaPlayer player=null;
    private void startDialerRing()
    {

            if(player==null) {
                player = MediaPlayer.create(context, R.raw.tring);
                player.setLooping(true); // Set looping
                player.setVolume(1.05f, 1.05f);
                player.start();
            }

    }

    private void stopDialerRing()
    {
        try {
            if(player!=null) {
                if (player.isPlaying())
                {
                    player.stop();
                    player.release();
                    player = null;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }





    private void showLog(String msg)
    {
        Log.d(TAG,msg);
    }

}
