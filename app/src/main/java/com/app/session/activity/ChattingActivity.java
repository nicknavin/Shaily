package com.app.session.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.app.session.BuildConfig;
import com.app.session.R;
import com.app.session.adapter.ChatAdapter;
import com.app.session.api.Urls;
import com.app.session.audio_record_view.AttachmentOption;
import com.app.session.audio_record_view.AttachmentOptionsListener;
import com.app.session.audio_record_view.AudioRecordView;
import com.app.session.audioplayer.AudioStatus;
import com.app.session.audioplayer.MediaPlayerUtils;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ChatCallback;
import com.app.session.interfaces.ServiceResultReceiver;
import com.app.session.data.model.AgoraBody;
import com.app.session.data.model.AgoraRoot;
import com.app.session.data.model.AudioVideoData;
import com.app.session.data.model.UserInformation;
import com.app.session.room.ChatMessage;
import com.app.session.room.ChatMessageBody;
import com.app.session.data.model.ClearChat;
import com.app.session.data.model.Conversation;
import com.app.session.data.model.LoadMessage;
import com.app.session.data.model.MessageChat;
import com.app.session.data.model.ReqMessageRead;
import com.app.session.data.model.Root;
import com.app.session.data.model.RootChatMessage;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiInterface;
import com.app.session.room.MyDatabase;
import com.app.session.service.FileUploadService;
import com.app.session.service.MyForgroundService;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.session.service.FileUploadService.FAIL;
import static com.app.session.service.FileUploadService.SHOW_RESULT;
import static com.app.session.service.FileUploadService.STATUS;
import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;

public class ChattingActivity extends BaseActivity implements AudioRecordView.RecordingListener, View.OnClickListener, AttachmentOptionsListener, ServiceResultReceiver.Receiver, MediaPlayerUtils.Listener {

    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    public static final String RECEIVER = "receiver";
    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    int load = 0;
    int total_pages = 0, pageno = 1;
    CircleImageView imgProfilepic;
    CustomTextView txtUserName, txtTyping;
    ChatAdapter chatAdapter;
    Conversation conversation;

    LinkedList<ChatMessage> chatMessageLinkedList = new LinkedList<>();
    private static final String TAG = "myTag";
    String receiverID = "", receiverName = "", receiverUrl = "", senderProfileUrl = "";
    private long time;
    private Socket mSocket;
    private Boolean isConnected = true;
    LinearLayoutManager linearLayoutManager;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    SwipeRefreshLayout swipeRefreshLayout;

    public boolean isForCamera = false;
    private Uri mCameraImageUri, mImageCaptureUri, mDocumentUri;
    byte[] ByteArray;
    String story_type = "", selectedDocumentPath = "", docName = "", selectedVideoPath = "";
    File imageFile;
    Bitmap videoThumbBitmap;
    File audioFile = null;
    private Handler mTypingHandler = new Handler();
    private boolean mTyping = false;
    private static final int TYPING_TIMER_LENGTH = 600;
    String roomOne = "";
    boolean flagMobile;
    public LinkedList<AudioStatus> audioStatusList = new LinkedList<>();
    private Parcelable state;
    private int countUnReadMsg = 0;
    ImageView imgVideoCall,imgAudioCall;

    MyDatabase myDatabase;
    boolean userIsOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        if(getIntent().getIntExtra("notificationId",0)!=0)
        {
            int notificationId=getIntent().getIntExtra("notificationId",0);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        setUpDB();
        initView();
        connectWithSocket();
        readMessage();
        allLoadChatting();
//        if (countUnReadMsg > 0)//unread msg conditon
//        {
//         //   readMessage();//this to nofity read msg for sender
//            //getAllChatDB();
//            allLoadChatting();
//
//        }
//        else
//        {
//
//            if (getAllChatDB().size() ==0)
//            {
//                allLoadChatting();
//            }
//
//        }


    }
    public void startService() {
        Intent serviceIntent = new Intent(this, MyForgroundService.class);
        serviceIntent.putExtra("Sessionway", "Notification");
        serviceIntent.putExtra("userId",userId);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    private void setUpDB() {
        myDatabase = Room.databaseBuilder(context, MyDatabase.class, "SessionChatDB").allowMainThreadQueries().build();
    }

    private void insertInDB(ChatMessage chat) {
        audioStatusList.addLast(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
        myDatabase.taskDAO().chatInsertion(chat);
    }

    private void updatedChatDB(boolean upload, String id) {
        myDatabase.taskDAO().updateChat(upload, id);
//       if(flag)
//       {
//           showToast("update is done");
//       }
    }

    private List<ChatMessage> getAllChatDB() {
        List<ChatMessage> list = myDatabase.taskDAO().getChatMessages();
        if (list.size()>0) {
            for (ChatMessage chatMessage : list)
            {
            chatMessageLinkedList.addLast(chatMessage);
            audioStatusList.addLast(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
            }
            chatAdapter.notifyDataSetChanged();
            scrollToBottom();
        }

        return list;
    }


    private void initView()
    {
        if (getIntent().getStringExtra("ID") != null) {
            receiverID = getIntent().getStringExtra("ID");
            System.out.println("receiverID : " + receiverID);
            roomOne = userId + "." + receiverID;
        }
        if (getIntent().getStringExtra("NAME") != null) {
            receiverName = getIntent().getStringExtra("NAME");
            System.out.println("receiverName : " + receiverName);
        }
        if (getIntent().getStringExtra("URL") != null) {
            receiverUrl = getIntent().getStringExtra("URL");
        }
        if (getIntent().getStringExtra("NOTIFICATION_COUNT") != null) {

            String count = getIntent().getStringExtra("NOTIFICATION_COUNT");
            if (!count.isEmpty())
            {
                countUnReadMsg = Integer.parseInt(count);
            }
        }
        conversation = new Conversation();

        audioRecordView = new AudioRecordView();
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        audioRecordView.initView((FrameLayout) findViewById(R.id.layoutMain));
        // this is to provide the container layout to the audio record view..
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);

        audioRecordView.setRecordingListener(this);
        setListener();
        audioRecordView.getMessageView().requestFocus();

        containerView.findViewById(R.id.layBack).setOnClickListener(this);
        containerView.findViewById(R.id.imgSetting).setOnClickListener(this);
        imgProfilepic = containerView.findViewById(R.id.imgProfilepic);
        txtTyping = containerView.findViewById(R.id.txtTyping);
        txtUserName = containerView.findViewById(R.id.txtUserName);
        txtUserName.setText(receiverName);
        Glide.with(context)
                .load(receiverUrl)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilepic);

        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);

        audioRecordView.removeAttachmentOptionAnimation(false);

        //setUpRecyclerListener();
        swipeRefreshLayout = containerView.findViewById(R.id.swipeRefreshLayout);
        setUpRecyclerView(containerView);
        //  setUpRecyclerListener();
//        getAllChatDB();
//        allLoadChatting();
        setSwipeLayout();

    }


    private void setUpRecyclerView(View containerView) {

        imgVideoCall=containerView.findViewById(R.id.imgVideoCall);
        imgVideoCall.setOnClickListener(this);
        imgAudioCall=containerView.findViewById(R.id.imgAudioCall);
        imgAudioCall.setOnClickListener(this);
        recyclerViewMessages = containerView.findViewById(R.id.recyclerViewMessages);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);

        recyclerViewMessages.setHasFixedSize(false);
        chatAdapter = new ChatAdapter(context, chatMessageLinkedList, userId, new ChatCallback() {
            @Override
            public void result(ChatMessage chatMessage, int position, View view) {
                if (chatMessage.getMessageType().equals("docs")) {
                    if (chatMessage.getUri() != null && !chatMessage.getUri().isEmpty()) {
                        Uri uri = Uri.parse(chatMessage.getUri());
                        Utility.displayDocument(context, uri);
                    } else {
                        new DownloadFile(chatMessage.getDisplayFileName()).execute(chatMessage.getFile());
                    }
                } else if (chatMessage.getMessageType().equals("image")) {
                    if (chatMessage.getPath() != null && !chatMessage.getPath().isEmpty()) {
                        showImage(0, chatMessage.getPath());

                    } else {
                        showImage(1, chatMessage.getFile());
                    }

                }
            }
        });
        recyclerViewMessages.setAdapter(chatAdapter);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());


    }


    public void setSwipeLayout() {


        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (pageno < total_pages) {
                    load = total_pages - pageno;
                    swipeRefreshLayout.setRefreshing(true);
                    loadChattingMore();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setListener() {

        audioRecordView.getEmojiView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
//                showToast("Emoji Icon Clicked");
            }
        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
//                showToast("Camera Icon Clicked");
                setCameraPermission();
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString().trim();
                audioRecordView.getMessageView().setText("");
                //messageAdapter.add(new Message(msg));
                //                msg="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore";

                SendMessage(msg);
            }
        });


        audioRecordView.getMessageView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (count > 0)
                {

                    {
                        messageTyping();
                    }


                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onRecordingStarted() {
        showToast("started");
        debug("started");

        time = System.currentTimeMillis() / (1000);
        setAudioPermission();
    }

    @Override
    public void onRecordingLocked() {
//        showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
//        showToast("completed");
        debug("completed");

        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 1) {
            stopRecording();

            System.out.println("audio file path " + outputFile);
            sendFile("audio", outputFile, "", "00:00", "");

            //messageAdapter.add(new Message(recordTime));
        }
    }

    @Override
    public void onRecordingCanceled() {
        showToast("canceled");
        debug("canceled");
        callRecordingCancel();
    }

    public void callRecordingCancel() {

        if (isRecordring) {
            this.cancelRecording();
        }

        if (!outputFile.isEmpty()) {
            File file = new File(outputFile);
            if (file != null) {
                // Utils.printLog(context, "AudioFRG:", "File deleted...");
                file.delete();
            }
        }

    }

    public void cancelRecording() {

        if (audioRecorder != null) {
            try {
                showToast(context.getResources().getString(R.string.msg_audio_stop));
                audioRecorder.stop();
            } catch (RuntimeException stopException) {
//                Utils.printLog(context, "AudioMsgFrag:", "Runtime exception.This is thrown intentionally if stop is called just after start");
            } finally {
                audioRecorder.release();
                audioRecorder = null;
                isRecordring = false;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_normal);
//                audioRecordingText.setText(getResources().getText(com.applozic.mobicomkit.uiwidgets.R.string.start_text));
                countDownTimer.cancel();
            }

        }
        cnt = 0;
        sec = 0;
        audioTime = 0;
    }


    public void setAudioPermission() {

        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage")) {
                return;
            }
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.RECORD_AUDIO, "Record Audio")) {
                return;
            }
        }
        initAudio();
        callRecord();

    }


    private void debug(String log) {
        Log.d("", log);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.layBack:
                finish();
                break;
            case R.id.imgSetting:
                showMenu(view);
                break;
            case R.id.imgVideoCall:

                getCallingToken("video");

                break;
                case R.id.imgAudioCall:

                getCallingToken("audio");

                break;
        }

    }


    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {

            case AttachmentOption.DOCUMENT_ID:
                showToast("Document Clicked");
                openFile(mDocumentUri);
                break;
            case AttachmentOption.CAMERA_ID:
                showToast("Camera Clicked");
                setCameraPermission();
                break;
            case AttachmentOption.GALLERY_ID:
                showToast("Gallery Clicked");
                setGalleryPermission();
                break;
            case AttachmentOption.AUDIO_ID:
                showToast("Audio Clicked");
                pickAudioFile();
                break;
            case AttachmentOption.LOCATION_ID:
                showToast("Location Clicked");
                break;
            case AttachmentOption.CONTACT_ID:
                showToast("Contact Clicked");
                break;
        }
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
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Constant.NEW_MESSAGE, onNewMessage);
        mSocket.on(Constant.FILES_EVENT, onFiles);
        mSocket.on(Constant.IS_ON, isOnline);
        mSocket.on(Constant.TYPING, onTyping);
        mSocket.on(Constant.IS_CONTENTREAD, isContentRead);

//        mSocket.on(Constant.CALL_NOTIFY,onCallNotify);
        mSocket.connect();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    {
                        if (null != userId)
                            try {
                                JSONObject data = new JSONObject();
                                data.put("userId", userId);
                                data.put("deviceType", "mobile");
                                mSocket.emit(Constant.EVENT_JOIN, data);
                                connectedBoth();

                               sendOnlineStatus();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        showToast("connect");
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    System.out.println("DATA" + args[0]);
                    isConnected = false;
                   // showToast("diconnected");
                    mSocket.connect();

//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    // showToast("Error connecting" + args[0]);
                    System.out.println("DATA" + args[0]);
                    mSocket.connect();
                    //Toast.makeText(getActivity().getApplicationContext(),R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*{"message":"gg","userId":"5f413256c4b8ce176fc80a8f","senderName":"Fadel Bohamad ","reciverId":"5f3cfba45a1d392b092e3fb8","reciverName":"navin nimade jii"}*/
                    JSONObject data = (JSONObject) args[0];
                    System.out.println();
                    mlog("msg : " + data.toString());
                    MessageChat messageChat = new MessageChat();
                    try {

                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSenderId(data.getString("senderId"));
                        if (!data.isNull("reciverId")) {
                            chatMessage.setReciverId(data.getString("reciverId"));
                        }
                        chatMessage.setCreatedAt(data.getString("createdAt"));
                        chatMessage.setMessage(data.getString("message"));
                        chatMessage.setRead(data.getBoolean("isRead"));
                        chatMessage.setReciverName(receiverName);
                        chatMessage.setSenderName(user_name);
                        chatMessage.setMessageType(data.getString("messageType"));
                        chatMessage.setSenderProfileUrl(data.getString("senderProfileUrl"));
                        chatMessage.setReciverProfileUrl(data.getString("reciverProfileUrl"));
                        if (receiverID.equals(chatMessage.getSenderId()))//freind
                        {
                            chatMessageLinkedList.addLast(chatMessage);
                            chatAdapter.notifyDataSetChanged();
                            scrollToBottom();
                        }
                        if (userId.equals(chatMessage.getSenderId()))//user
                        {
                            chatMessageLinkedList.addLast(chatMessage);

                            chatAdapter.notifyDataSetChanged();
                            scrollToBottom();
                        }
                        flagMobile = false;
                        insertInDB(chatMessage);

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }


                }
            });
        }
    };

    private Emitter.Listener onFiles = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*{"message":"gg","userId":"5f413256c4b8ce176fc80a8f","senderName":"Fadel Bohamad ","reciverId":"5f3cfba45a1d392b092e3fb8","reciverName":"navin nimade jii"}*/
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("msg : " + data.toString());

                    try {
                        //{"reciverProfileUrl":"","senderProfileUrl":"","senderId":"5f3cfba45a1d392b092e3fb8","reciverId":"5f413256c4b8ce176fc80a8f","file":"userFiles\/chatFiles\/chatFiles-1603873123488-725-935185_732378726786492_806333436_n.jpg","messageType":"image","msg_notification_reciver":0,"createdAt":"2020-10-28T08:18:43.668Z"}
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSenderId(data.getString("senderId"));
                        if (!data.isNull("reciverId")) {
                            chatMessage.setReciverId(data.getString("reciverId"));
                        }
                        chatMessage.setCreatedAt(data.getString("createdAt"));
                        chatMessage.setRead(true);
                        chatMessage.setReciverName(receiverName);
                        chatMessage.setSenderName(user_name);
                        chatMessage.setFile(data.getString("file"));
                        chatMessage.setMessageType(data.getString("messageType"));
                        if (receiverID.equals(chatMessage.getSenderId()))//freind
                        {
                            chatMessageLinkedList.addLast(chatMessage);
                            chatAdapter.notifyDataSetChanged();
                            scrollToBottom();
                            insertInDB(chatMessage);
                        }
//                        if (userId.equals(chatMessage.getSenderId()))//user
//                        {
//                            chatMessageLinkedList.addLast(chatMessage);
//                            chatAdapter.notifyDataSetChanged();
//                            scrollToBottom();
//                        }
                        flagMobile = false;


                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }


                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //{"type":true,"typing":"typing.."}
                    if (args.length > 0) {
                        if (txtTyping != null) {
                            try {
                                JSONObject data = (JSONObject) args[0];

                                boolean type = data.getBoolean("type");

                                System.out.println("data " + data.toString());
                                //{"type":true,"typing":"typing..","typerUserId":"5f413256c4b8ce176fc80a8f","userName":"bluebird"}
                                if (type) {
                                    if (receiverID.equals(data.getString("typerUserId"))) {
                                        txtTyping.setVisibility(View.VISIBLE);
                                        txtTyping.setText("typing...");
                                    }
                                }
                                mTypingHandler.removeCallbacks(onTypingTimeout);
                                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        }
    };

    private Emitter.Listener isOnline = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0) {
                        if (txtTyping != null)
                        {
                                //{"reciverId":"5f437ef1c4b8ce176fc80a91","isOn":false}

                            JSONObject data = (JSONObject) args[0];
                            try {
                                userIsOnline= data.getBoolean("isOn");
                                mlog("user status"+data.toString());
                                mlog("userIsOnline "+userIsOnline);
                                txtTyping.setVisibility(View.VISIBLE);
                                if(userIsOnline) {
                                    txtTyping.setText("online");
                                }
                                else
                                {
                                    txtTyping.setText("offline");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener isContentRead = new Emitter.Listener() {
        @Override
        public void call(Object... args)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0)
                        {
                            mlog(" isContentRead ");
                       // chatAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };



    private void sendVideoCallNotification() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomName", receiverID);
            jsonObject.put("callerName",user_name);
            jsonObject.put("ProfileUrl",senderProfileUrl);
            jsonObject.put("callType","video");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit(Constant.CALL_NOTIFICATION, jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                if (args.length > 0)
                {
                    mlog("callNotification..");

                }
            }
        });
        Intent intent = new Intent(context,VideoChatViewActivity.class);
        startActivity(intent); }


    private void sendLeaveRoom() {
        mlog("sendLeaveRoom start ..");
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jsonRoom = new JSONObject();
            jsonRoom.put("roomOne", roomOne);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit(Constant.LEAVE_ROOM, jsonObject, new Ack() {
            @Override
            public void call(Object... args)
            {
                mlog("sendLeaveRoom.. args  ");
                if (args.length > 0)
                {
                    JSONObject data = (JSONObject) args[0];
                    mlog("sendLeaveRoom.."+data.toString());

                }
            }
        });
      }

    private void sendOnlineStatus() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reciverId", receiverID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit(Constant.IS_ONLINE, jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                if (args.length > 0) {
                    log("sendOnlineStatus ");
                }
            }
        });
    }

    private void connectedBoth() {
        try {
            JSONObject jsonRoom = new JSONObject();
            jsonRoom.put("roomOne", roomOne);
            mSocket.emit(Constant.CONNECTED_BOTH, jsonRoom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void messageTyping() {
        if (!mSocket.connected()) return;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reciverId", receiverID);
            jsonObject.put("userId", userId);
            jsonObject.put("userName", login_user_id);
            jsonObject.put("typing", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit(Constant.IS_TYPING, jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                if (args.length > 0) {
                    System.out.println("typing......" + args[0]);
                    mTyping=false;
                }
            }
        });
    }

    private void SendMessage(String message)
    {
        mTyping=true;
        flagMobile = true;
        if (!mSocket.connected()) return;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reciverId", receiverID);
            jsonObject.put("userId", userId);
            jsonObject.put("message", message);
            jsonObject.put("senderName", login_user_id);
            jsonObject.put("reciverName", receiverName);
            jsonObject.put("reciverProfileUrl", receiverUrl);
            jsonObject.put("senderProfileUrl", profileUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }


//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setSenderId(userId);
//        chatMessage.setReciverId(receiverID);
//        chatMessage.setMessage(message);
//        chatMessage.setIsRead(true);
//        chatMessage.setReciverName(receiverName);
//        chatMessage.setSenderName(user_name);
//        chatMessage.setCreatedAt(Utility.getLocalTime());
//        chatMessageLinkedList.addLast(chatMessage);
//        chatAdapter.notifyDataSetChanged();
//        scrollToBottom();
        log(" send msg : "+jsonObject.toString());
        mSocket.emit("privateMessage", jsonObject, new Ack()
        {
            @Override
            public void call(Object... args) {

                if (args.length > 0) {
                    JSONObject data = (JSONObject) args[0];
                    Log.d(TAG,"privateMessage : "+data.toString());
                }
            }
        });
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



    public void allLoadChatting() {
        if (Utility.isConnectingToInternet(context)) {
            LoadMessage loadMessage = new LoadMessage();
            loadMessage.setSenderId(userId);
            loadMessage.setReciverId(receiverID);
            loadMessage.setLoad(load);
            ApiInterface apiInterface = ApiClient.getClient(Urls.PRIVATEMESSAGE_URL).create(ApiInterface.class);
            Call<RootChatMessage> call = apiInterface.reqLoadPreviousMsg(accessToken, loadMessage);

            call.enqueue(new Callback<RootChatMessage>() {
                @Override
                public void onResponse(Call<RootChatMessage> call, Response<RootChatMessage> response) {
                    if (response.body() != null) {
                        RootChatMessage rootChatMessage = response.body();
                        if (rootChatMessage.getStatus() == 200) {
                            ChatMessageBody chatMessageBody = rootChatMessage.getChatMessageBody();
                            total_pages = chatMessageBody.getTotal_Page();
                            loading = false;
                            LinkedList<ChatMessage> list = new LinkedList<>();
                            list = chatMessageBody.getChatMessages();
                            for (ChatMessage chatMessage : list) {
                                chatMessageLinkedList.addLast(chatMessage);
                                audioStatusList.addLast(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
                                insertInDB(chatMessage);
                            }
                            chatAdapter.notifyDataSetChanged();
                            scrollToBottom();
                        }
                    }

                }

                @Override
                public void onFailure(Call<RootChatMessage> call, Throwable t) {

                }
            });


        } else {
            showInternetConnectionToast();
        }
    }

    public void loadChattingMore() {
        if (Utility.isConnectingToInternet(context)) {
            LoadMessage loadMessage = new LoadMessage();
            loadMessage.setSenderId(userId);
            loadMessage.setReciverId(receiverID);
            loadMessage.setLoad(load);

            ApiInterface apiInterface = ApiClient.getClient(Urls.PRIVATEMESSAGE_URL).create(ApiInterface.class);
            Call<RootChatMessage> call = apiInterface.reqLoadPreviousMsg(accessToken, loadMessage);

            call.enqueue(new Callback<RootChatMessage>() {
                @Override
                public void onResponse(Call<RootChatMessage> call, Response<RootChatMessage> response) {
                    RootChatMessage rootChatMessage = response.body();
                    if (rootChatMessage.getStatus() == 200) {
                        ChatMessageBody chatMessageBody = rootChatMessage.getChatMessageBody();
                        total_pages = chatMessageBody.getTotal_Page();
                        if (pageno <= total_pages) {
                            loading = true;
                            pageno++;
                        }
                        loading = true;
                        LinkedList<ChatMessage> list = new LinkedList<>();
                        list = chatMessageBody.getChatMessages();
                        for (int i = list.size() - 1; i > 0; i--) {
                            ChatMessage chatMessage = list.get(i);
                            chatMessageLinkedList.addFirst(chatMessage);
                        }

                        for (int i = 0; i < chatMessageLinkedList.size(); i++) {
                            audioStatusList.addFirst(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
                        }

//                        chatAdapter = new ChatAdapter(context, chatMessageArrayList, userId);
//                        recyclerViewMessages.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                        // scrollToBottom();
                    }

                }

                @Override
                public void onFailure(Call<RootChatMessage> call, Throwable t) {

                }
            });


        } else {
            showInternetConnectionToast();
        }
    }


    public void clearChatting() {
        if (Utility.isConnectingToInternet(context)) {

            ClearChat clearChat = new ClearChat();
            clearChat.setSenderId(userId);
            clearChat.setReciverId(receiverID);

            showLoading();
            ApiInterface apiInterface = ApiClient.getClient(Urls.PRIVATEMESSAGE_URL).create(ApiInterface.class);
            Call<Root> call = apiInterface.reqClearChat(accessToken, clearChat);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            myDatabase.taskDAO().delete();
                            chatMessageLinkedList.clear();
                            chatAdapter = new ChatAdapter(context, chatMessageLinkedList, userId, new ChatCallback() {
                                @Override
                                public void result(ChatMessage chatMessage, int position, View view) {

                                    if (chatMessage.getMessageType().equals("docs")) {
                                        if (chatMessage.getPath() != null && !chatMessage.getPath().isEmpty()) {
                                            Uri uri = Uri.fromFile(new File(chatMessage.getPath()));

                                            Utility.displayDocument(context, uri);
                                        } else {
                                            new DownloadFile(chatMessage.getDisplayFileName()).execute(chatMessage.getFile());
                                        }
                                    } else if (chatMessage.getMessageType().equals("image")) {
                                        if (chatMessage.getPath() != null && !chatMessage.getPath().isEmpty()) {
                                            showImage(0, chatMessage.getPath());

                                        } else {
                                            showImage(1, chatMessage.getFile());
                                        }
                                    }

                                }
                            });
                            recyclerViewMessages.setAdapter(chatAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }


    public void getCallingToken(String callingType)
    {
        if (Utility.isConnectingToInternet(context))
        {
            UserInformation userInformation=new UserInformation();
            userInformation.setUserId(userId);
            showLoading();
            ApiInterface apiInterface = ApiClient.getClient(Urls.USER_URL).create(ApiInterface.class);
            Call<AgoraRoot> call = apiInterface.reqAgoraToken(accessToken, userInformation);
            call.enqueue(new Callback<AgoraRoot>()
            {
                @Override
                public void onResponse(Call<AgoraRoot> call, Response<AgoraRoot> response)
                {
                    dismiss_loading();
                    Log.d(TAG,"before");
                    if(response.body()!=null)
                    {
                        if(response.body().getStatus()==200)
                        {
                            AgoraBody body=response.body().getAgoraBody();
                            Log.d(TAG,"before00 "+body.getAgorachannelName()+" "+body.getToken());
                            AudioVideoData audioVideoData=new AudioVideoData();
                            audioVideoData.setUserId(userId);
                            audioVideoData.setAgorachannelName(body.getAgorachannelName());
                            audioVideoData.setAgoraTockenID(body.getToken());
                            audioVideoData.setCallerName(user_name);
                            audioVideoData.setProfileUrl(profileUrl);
                            audioVideoData.setReciever_profile_url(receiverUrl);
                            audioVideoData.setReciverId(receiverID);
                            audioVideoData.setCallType(callingType);
                            audioVideoData.setReciverName(receiverName);
                            sendAudioCalling(audioVideoData);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AgoraRoot> call, Throwable t) {
                    Log.d(TAG,"before00 exp ");
                    dismiss_loading();
                }
            });

        } else {
            showInternetConnectionToast();
        }
    }


    private void scrollToBottom() {
        linearLayoutManager.scrollToPosition(chatMessageLinkedList.size() - 1);
    }

            private void sendMsg(String message)
            {

                Intent service = new Intent(this, MyForgroundService.class);
                service.putExtra(MyForgroundService.EXTRA_EVENT_TYPE, MyForgroundService.EVENT_VIDEO_CALLING);
                service.putExtra("USER_ID",userId);
                service.putExtra("MESSAGE", message);
                service.putExtra("SENDER_NAME",login_user_id);
                service.putExtra("RECEIVER_ID",receiverID);
                service.putExtra("RECIVER_NAME",receiverName);
                service.putExtra("RECIVER_PROFILE_URL",receiverUrl);
                service.putExtra("SENDER_PROFILE_URL",profileUrl);

                startService(service);
            }

    public void readMessage() {
        if (Utility.isConnectingToInternet(context)) {

            ReqMessageRead reqMessageRead = new ReqMessageRead();
            reqMessageRead.setUserId(userId);
            reqMessageRead.setReciverId(receiverID);
            showLoading();
            ApiInterface apiInterface = ApiClient.getClient(Urls.PRIVATEMESSAGE_URL).create(ApiInterface.class);
            Call<Root> call = apiInterface.reqReadMsg(accessToken, reqMessageRead);
            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    dismiss_loading();
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    dismiss_loading();
                }
            });
        } else {
            showInternetConnectionToast();
        }
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_clear_chat:

                        clearChatting();


                        return true;


                    case R.id.chat_block:
//                        Intent intent = new Intent(context, UpdateSubscriptionStoryActivity.class);
//                        intent.putExtra("DATA", storyData);
//                        Bundle arg = new Bundle();
//                        arg.putSerializable("List", (Serializable) brief_cvList);
//                        intent.putExtra("BUNDLE", arg);
//                        intent.putExtra("ID", storyData.get_id());
//                        startActivity(intent);
                        return true;


                    default:
                        return false;
                }
            }
        });// to implement on click event on items of menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_chat, popup.getMenu());


        popup.show();
    }


    public void setCameraPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }

            if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
                if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                    return;
                }
            }
        }
        TakePic();
    }

    public void setGalleryPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage")) {
                return;
            }
        }

        Gallery();
    }


    public void pickAudioFile() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage")) {
                return;
            }
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }

        }

        String[] mimeTypes = {"audio/mp3", "audio/mpeg"};
//        Intent intent = new Intent();
//        intent.setType("audio/mp3");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, Constant.PICK_AUDIO_CODE);
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, Constant.PICK_AUDIO_CODE);
        } else {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constant.PICK_AUDIO_CODE);
        }


    }


    public void Gallery() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.REQUEST_CODE_ALBUM);
        } else {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constant.REQUEST_CODE_ALBUM);
        }
    }

    public void TakePic() {

        try {
            isForCamera = true;
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File filesDir = context.getFilesDir();
            File f = new File(filesDir + "/session.png");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mCameraImageUri = Uri.fromFile(f);
            } else {
                mCameraImageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, Constant.REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void openFile(Uri pickerInitialUri) {

        String[] mimeTypes =
                {"application/msword",  // .doc & .docx
                        "application/vnd.ms-powerpoint",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel",
                        "application/vnd.oasis.opendocument.text",
                        "text/plain",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "application/pdf",
                        "application/zip"};
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), Constant.PICKFILE_RESULT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:


                    CropImage.activity(mCameraImageUri)
                            .start(this);

                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {

                        mImageCaptureUri = data.getData();

//                        CropImage.activity(mImageCaptureUri)
//                                .setAspectRatio(1, 1)
//                                .setGuidelines(CropImageView.Guidelines.ON)
//                                .setFixAspectRatio(false)
//                                .start(this);
                        int w = Utility.getScreenWidth(this);
                        CropImage.activity(mImageCaptureUri)
//                                .setCropShape(CropImageView.CropShape.RECTANGLE)
//                                .setAspectRatio(2, 1)
                                .start(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        try {
                            ByteArray = null;
                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            videoThumbBitmap = bm;
                            sendFile("image", resultUri.toString(), "", "0", "");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    break;
                case Constant.REQUEST_AUDIO_RECORD:
                    Uri audioFileUri = data.getData();
                    audioFile = new File(audioFileUri.getPath());

                    break;

                default:
                    break;


            }

            if (requestCode == Constant.PICKFILE_RESULT_CODE) {
                if (data != null) {
                    try {
                        mDocumentUri = data.getData();

                        String uri = mDocumentUri.toString();
                        selectedDocumentPath = Utility.getRealPathFromUri(context, mDocumentUri);

                        docName = getFileName(mDocumentUri);
                        sendFile("docs", selectedDocumentPath, docName, "0", uri);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


            if (requestCode == Constant.PICK_VIDEO_THUMB) {

                if (data != null) {

                    long location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0);
                    Uri uri = data.getParcelableExtra(EXTRA_URI);
                    videoThumbBitmap = ThumbyUtils.getBitmapAtFrame(context, uri, location, 200, 200);


                    ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                    videoThumbBitmap.compress(Bitmap.CompressFormat.PNG, 100, datasecond);


                    selectedVideoPath = data.getStringExtra(VIDEO_PATH);

                    //ima.setVisibility(View.GONE);

                    //callUploadImage(imgbase, language_cd + "_" + userId + "_thumbnail");
                    story_type = "video";


                }

            }

            if (requestCode == Constant.PICK_AUDIO_CODE) {
                if (data != null) {
                    try {
                        mDocumentUri = data.getData();
                        selectedDocumentPath = Utility.getRealPathFromUri(context, mDocumentUri);
                        docName = getFileName(mDocumentUri);
                        long milliseconds = Utility.getAudioDuration(context, selectedDocumentPath);


                        Double minxutes = (double) ((milliseconds / 1000) / 60);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
                        System.out.println("minutes " + minutes);
                        // long seconds = (milliseconds / 1000);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
                        String duration = "";
                        if (minutes > 0) {
                            duration = "" + minutes + ":00";
                        } else {
                            duration = "00:0" + seconds;
                        }


                        sendFile("audio", selectedDocumentPath, docName, duration, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


        }


    }

    private void persistImage(Bitmap bitmap, String name) {

        File filesDir = context.getFilesDir();
        imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }


    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void pickFile() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 1);
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {

            if (txtTyping != null) {
                txtTyping.setVisibility(View.VISIBLE);
                txtTyping.setText("online");
            }
        }
    };
    ChatMessage chatMessageFile;
    private ServiceResultReceiver mServiceResultReceiver;

    private void sendFile(String fileType, String path, String fileName, String duration, String uri) {

        mServiceResultReceiver = new ServiceResultReceiver(new Handler());
        mServiceResultReceiver.setReceiver(this);
        chatMessageFile = new ChatMessage();
        chatMessageFile.setSenderId(userId);
        chatMessageFile.setSenderName(login_user_id);
        chatMessageFile.setSenderProfileUrl(profileUrl);
        chatMessageFile.setReciverId(receiverID);
        chatMessageFile.setReciverName(receiverName);
        chatMessageFile.setReciverProfileUrl(receiverUrl);
        chatMessageFile.setMessageType(fileType);
        chatMessageFile.setPath(path);
        chatMessageFile.setUpload(false);
        chatMessageFile.setDisplayFileName(fileName);
        chatMessageFile.setDurationTime(duration);
        chatMessageFile.setUri(uri);


        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", selectedVideoPath);
        mIntent.putExtra("FileName", "");
        mIntent.putExtra("VIDEO_THUMB", videoThumbBitmap);//this for image
        mIntent.putExtra("TYPE", "CHAT");
        mIntent.putExtra("USER_ID", userId);
        mIntent.putExtra("TOKEN", accessToken);
        mIntent.putExtra("LANGUAGE_ID", "cd");
        mIntent.putExtra("CHAT_DATA", chatMessageFile);
        mIntent.putExtra(RECEIVER, mServiceResultReceiver);
        mIntent.setAction(ACTION_DOWNLOAD);
        FileUploadService.enqueueWork(context, mIntent);
        sendFileMsg();
    }

    private void sendFileMsg() {
        audioStatusList.addLast(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
        chatMessageLinkedList.addLast(chatMessageFile);
        chatAdapter.notifyDataSetChanged();
        scrollToBottom();
        insertInDB(chatMessageFile);//insert in db
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {


                }
                break;

            case STATUS:

                String id = "";
                if (resultData != null) {
                    String data = resultData.getString("DATA");
                    System.out.println("data " + data);
                    try {

                        JSONObject jsonObject = new JSONObject(data);
                        JSONObject body = jsonObject.getJSONObject("body");
                        JSONObject fileData = body.getJSONObject("fileData");
                        id = fileData.getString("_id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                chatMessageLinkedList.getLast().setUpload(true);
//                updatedChatDB(true, id);
//                chatAdapter.notifyDataSetChanged();
//                scrollToBottom();
                break;

            case FAIL:
                showToast("uploading is fail, please try again");
//                layProgress.setVisibility(View.GONE);
                break;

        }
    }


    private String outputFile = null, audioFileName = "";
    private int cnt;
    private boolean isRecordring;
    CountDownTimer countDownTimer;
    int sec = 0;
    long audioTime = 0;
    private MediaRecorder audioRecorder;

    public void callRecord() {
        try {
            showToast(context.getResources().getString(R.string.msg_audio_start));
            if (isRecordring) {
                this.stopRecording();
                countDownTimer.cancel();
            } else {
                if (audioRecorder == null) {
                    prepareMediaRecorder();
                }
//                audioRecordingText.setText(getResources().getString(com.applozic.mobicomkit.uiwidgets.R.string.stop));
                audioRecorder.prepare();
                audioRecorder.start();
                isRecordring = true;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_mic_inverted);
//                t.cancel();
                countDownTimer.start();
//                cnt = 0;
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAudio() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = "AUD_" + timeStamp + "_" + ".mp3";
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        outputFile = context.getFilesDir().getAbsolutePath();
        String name = "/" + Utility.getTime() + "/AudioRecording.mp3";
        outputFile += audioFileName;
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                cnt++;
                sec++;
                long millis = cnt;
                audioTime = millis;
                //  sec=(int)millis;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                if (sec == 60) {
                    sec = 0;
                }

//                txtAudioTimer.setText(String.format("%02d:%02d", seconds, sec));
////                txtAudioTimer.setText(String.format("%02d:%02d", minutes, seconds));

            }

            @Override
            public void onFinish() {

            }
        };

    }

    public void stopRecording() {
        if (audioRecorder != null) {
            try {
                showToast(context.getResources().getString(R.string.msg_audio_stop));
                audioRecorder.stop();
            } catch (RuntimeException stopException) {
//                Utils.printLog(context, "AudioMsgFrag:", "Runtime exception.This is thrown intentionally if stop is called just after start");
            } finally {
                audioRecorder.release();
                audioRecorder = null;
                isRecordring = false;
//                record.setImageResource(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_audio_normal);
//                audioRecordingText.setText(getResources().getText(com.applozic.mobicomkit.uiwidgets.R.string.start_text));
                countDownTimer.cancel();
            }

        }

    }

    public MediaRecorder prepareMediaRecorder() {

        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioRecorder.setAudioEncodingBitRate(256);
        audioRecorder.setAudioChannels(1);
        audioRecorder.setAudioSamplingRate(44100);
        audioRecorder.setOutputFile(outputFile);
        return audioRecorder;

    }


    @Override
    public void onAudioUpdate(int currentPosition) {
        int playingAudioPosition = -1;
        for (int i = 0; i < audioStatusList.size(); i++) {
            AudioStatus audioStatus = audioStatusList.get(i);
            if (audioStatus.getAudioState() == AudioStatus.AUDIO_STATE.PLAYING.ordinal()) {
                playingAudioPosition = i;
                break;
            }
        }

        if (playingAudioPosition != -1) {
//            AudioListAdapter.ViewHolder holder
//                    = (AudioListAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(playingAudioPosition);
//            if (holder != null) {
//                holder.seekBarAudio.setProgress(currentPosition);
//            }

            System.out.println("playingAudioPosition " + playingAudioPosition);
            int value = chatAdapter.getItemViewType(playingAudioPosition);
            System.out.println("value " + value);

            if (value == 1) {
                ChatAdapter.ItemMessageFriendHolder friendHolder = (ChatAdapter.ItemMessageFriendHolder) recyclerViewMessages.findViewHolderForAdapterPosition(playingAudioPosition);
                if (friendHolder != null) {
                    friendHolder.progressBar2.setProgress(currentPosition);
                }
            } else if (value == 0) {
                ChatAdapter.ItemMessageUserHolder userHolder = (ChatAdapter.ItemMessageUserHolder) recyclerViewMessages.findViewHolderForAdapterPosition(playingAudioPosition);
                if (userHolder != null) {
                    userHolder.progressBar2.setProgress(currentPosition);
                }
            }
        }
    }

    @Override
    public void onAudioComplete() {
        state = recyclerViewMessages.getLayoutManager().onSaveInstanceState();

        audioStatusList.clear();
        for (int i = 0; i < chatMessageLinkedList.size(); i++) {
            audioStatusList.add(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
        }
        //setRecyclerViewAdapter(contactList);
        chatAdapter.notifyDataSetChanged();


        // Main position of RecyclerView when loaded again
        if (state != null) {
            recyclerViewMessages.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    private void displayPdf(Uri uri) {

//        Uri uri = getFileUri(this, fileName);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        // FLAG_GRANT_READ_URI_PERMISSION is needed on API 24+ so the activity opening the file can read it
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(getPackageManager()) == null) {
            // Show an error
        } else {
            startActivity(intent);
        }
    }

    public File getFile(Context context, String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File storageDir = context.getExternalFilesDir(null);
        return new File(storageDir, fileName);
    }

    public Uri getFileUri(Context context, String fileName) {
        File file = getFile(context, fileName);
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }


    private class DownloadFile extends AsyncTask<String, Integer, File> {


        String fileName = "";

        DownloadFile(String fileName) {

            this.fileName = fileName;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected File doInBackground(String... strings) {
            int count;
            try {
                URL url = new URL(strings[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                File filesDir = context.getFilesDir();
                File audioFile = new File(filesDir, fileName);
                if (audioFile.exists()) {
                    audioFile.delete();

                }
                audioFile.createNewFile();
                InputStream input = new BufferedInputStream(url.openStream());
//                OutputStream output = new FileOutputStream("/sdcard/somewhere/nameofthefile.mp3");
                OutputStream output = new FileOutputStream(audioFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return audioFile;
            } catch (Exception e) {
                String s = e.toString();
                System.out.println(e.toString());
            }
            return null;
        }


        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            dismiss_loading();

            if (file != null) {

                String sharePath = file.getPath();
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                Utility.displayDocument(context, uri);


            }

        }

    }


    public void showImage(int type, String url) {


        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.activity_show_image);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            ImageView imageViewexpand = (ImageView) dd.findViewById(R.id.expanded_image);
            ImageView close = (ImageView) dd.findViewById(R.id.imgCross);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dd.dismiss();
                }
            });
            if (type == 1) {
                Glide.with(context)
                        .load(url)
                        .placeholder(R.mipmap.place_holder)
                        .error(R.mipmap.place_holder)
                        .into(imageViewexpand);
            } else {
                Uri resultUri = Uri.parse(url);
                Glide.with(context)
                        .load(new File(resultUri.getPath()))
                        .placeholder(R.mipmap.profile_icon)
                        .error(R.mipmap.profile_icon)
                        .into(imageViewexpand);
            }
            dd.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d(TAG, "Exception: " + e.getMessage());
        }


    }


    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
           // showToast("onReceive");
            mlog("onReceive");
            if(intent.getStringExtra("TYPE").equals("calling"))
            {
                mlog("onReceive 000");
                if(intent.getStringExtra("REQUEST").equals("caller"))
                {
                    try {
                        mlog("onReceive 1111");
                        String request = intent.getStringExtra("DATA");
                        JSONObject jsonObject=new JSONObject(request);
                        Intent callingintent1=null;
                        mlog("onReceive 222 ");
                        if(jsonObject.getString("callType").equals("audio"))
                        {
                            mlog("onReceive 33333");
                            callingintent1 = new Intent(context, VoiceChatViewActivity.class);
                        }
                        else {
                            mlog("onReceive 444 ");
                            callingintent1 = new Intent(context, VideoChatViewActivity.class);
                        }

                        callingintent1.putExtra("DATA", request);
                        startActivity(callingintent1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(intent.getStringExtra("REQUEST").equals("reciever"))
                {

                        String request = intent.getStringExtra("DATA");
                        Intent callingintent1=new Intent(context, CallIncomingActivity.class);
                        callingintent1.putExtra("DATA",request);
                        startActivity(callingintent1);

                }




            }


        }
    };


    private void sendAudioCalling(AudioVideoData audioVideoData)
    {
        Log.d(TAG,"sendAudioCalling ");
        Intent service = new Intent(this, MyForgroundService.class);
        service.putExtra(MyForgroundService.EXTRA_EVENT_TYPE, MyForgroundService.EVENT_VIDEO_CALLING);
        service.putExtra("DATA",audioVideoData);
        service.putExtra("RECEIVER_NAME",receiverName);
        startService(service);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver != null) {
         //   showToast("register broadcast");
            IntentFilter intentFilter = new IntentFilter(MyForgroundService.KEY_BROADCAST_MESSAGE);

            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (broadcastReceiver != null) {
           // showToast("unregister broadcast");
            LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
        }
    }

    MediaPlayer player;
    private void dailer()
    {
        player = MediaPlayer.create(this, R.raw.tring);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sendLeaveRoom();
//        sendOfflineStatus(userId);
//        sendOnlineStatus(userId);
        MediaPlayerUtils.releaseMediaPlayer();
    }


}
