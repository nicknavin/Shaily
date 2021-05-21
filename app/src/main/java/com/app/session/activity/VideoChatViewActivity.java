package com.app.session.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.session.R;
import com.app.session.customview.CircleImageView;
import com.app.session.data.model.AudioVideoData;
import com.app.session.data.model.NotificationModel;
import com.app.session.utility.Constant;
import com.app.session.utility.Foreground;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class VideoChatViewActivity extends AppCompatActivity {
    private static final String TAG ="myTag";

    private static final int PERMISSION_REQ_ID = 22;

    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean mMuted;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private VideoCanvas mLocalVideo;
    private VideoCanvas mRemoteVideo;

    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;
    boolean dialRing=false;
    // Customized logger view
   // private LoggerRecyclerView mLogView;

    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        /**
         * Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         *
         * @param channel Channel name.
         * @param uid User ID.
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
         */
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mLogView.logI("Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                    showLog(" onJoinChannelSuccess ");
                    if(dialRing) {
                        startDialerRing();
                    }
                }
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            stopDialerRing();
        }

        /**
         * Occurs when the first remote video frame is received and decoded.
         * This callback is triggered in either of the following scenarios:
         *
         *     The remote user joins the channel and sends the video stream.
         *     The remote user stops sending the video stream and re-sends it after 15 seconds. Possible reasons include:
         *         The remote user leaves channel.
         *         The remote user drops offline.
         *         The remote user calls the muteLocalVideoStream method.
         *         The remote user calls the disableVideo method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param width Width (pixels) of the video stream.
         * @param height Height (pixels) of the video stream.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mLogView.logI("User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft(uid);
                    endCall();
                    mCallEnd = true;
                }
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = mLocalContainer;
        }

        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        if (mRemoteVideo != null) {
            return;
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            // Destroys remote view
            mRemoteVideo = null;
        }
    }

    String data="";
    String tokenId="",name="",reciverName="",profileUrl="";
    Context context;
    CircleImageView imgProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
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
//               profileUrl=jsonObject.getString("ProfileUrl");
                profileUrl=jsonObject.getString("reciever_profile_url");
                if(getIntent().getStringExtra("CALLING_TYPE")!=null)
                {
                    if (getIntent().getStringExtra("CALLING_TYPE").equals("receiver"))
                    {
                        dialRing=false;

                        profileUrl = jsonObject.getString("ProfileUrl");
                        reciverName = jsonObject.getString("user_name");
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
        initUI();

        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }

    private void initUI() {
        imgProfile=findViewById(R.id.imgProfile);
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);

        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);

        Glide.with(context)
                .load(profileUrl)
                .placeholder(R.mipmap.profile_image)
                .error(R.mipmap.profile_image)
                .into(imgProfile);

        //mLogView = findViewById(R.id.log_recycler_view);

        // Sample logs are optional.
        showSampleLogs();
    }

    private void showSampleLogs() {
//        mLogView.logI("Welcome to Agora 1v1 video call");
//        mLogView.logW("You will see custom logs here");
//        mLogView.logE("You can also use this to show errors");
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.private_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        String token = tokenId;
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }
        mRtcEngine.joinChannel(token, name, "Extra Optional Data", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDialerRing();
        if (!mCallEnd) {
            leaveChannel();
        }
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        // Stops/Resumes sending the local audio stream.
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onSwitchCameraClicked(View view) {
        // Switches between front and rear cameras.
        mRtcEngine.switchCamera();
    }

    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
          //  mCallBtn.setImageResource(R.drawable.btn_startcall);
        }

        showButtons(!mCallEnd);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public ComponentName getComponentName() {
        return super.getComponentName();
    }



    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }
    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        stopDialerRing();
        removeFromParent(mLocalVideo);
        mLocalVideo = null;
        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        leaveChannel();
//        Intent service = new Intent(context, MyForgroundService.class);
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



    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }





    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }


    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver != null) {

            IntentFilter intentFilter = new IntentFilter(Constant.DISCONNECT_VIDEO);

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
