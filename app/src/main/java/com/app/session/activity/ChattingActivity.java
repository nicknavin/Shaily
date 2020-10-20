package com.app.session.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.app.session.R;
import com.app.session.adapter.ChatAdapter;
import com.app.session.api.Urls;
import com.app.session.audio_record_view.AttachmentOption;
import com.app.session.audio_record_view.AttachmentOptionsListener;
import com.app.session.audio_record_view.AudioRecordView;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.model.ChatMessage;
import com.app.session.model.ChatMessageBody;
import com.app.session.model.ClearChat;
import com.app.session.model.Conversation;
import com.app.session.model.LoadMessage;
import com.app.session.model.MessageChat;
import com.app.session.model.ReqMessageRead;
import com.app.session.model.Root;
import com.app.session.model.RootChatMessage;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiInterface;
import com.app.session.thumbyjava.ThumbyUtils;
import com.app.session.utility.Constant;
import com.app.session.utility.PermissionsUtils;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.session.thumby.ThumbyActivity.EXTRA_THUMBNAIL_POSITION;
import static com.app.session.thumby.ThumbyActivity.EXTRA_URI;
import static com.app.session.thumby.ThumbyActivity.VIDEO_PATH;

public class ChattingActivity extends BaseActivity implements AudioRecordView.RecordingListener, View.OnClickListener, AttachmentOptionsListener {

    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    int load = 0;
    int total_pages = 0, pageno = 1;
    CircleImageView imgProfilepic;
    CustomTextView txtUserName, txtTyping;
    ChatAdapter chatAdapter;
    Conversation conversation;

    LinkedList<ChatMessage> chatMessageLinkedList = new LinkedList<>();
    private static final String TAG = "ChatActivity";
    String receiverID = "", receiverName = "", url = "", senderProfileUrl = "";
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
    String roomOne="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        connectWithSocket();
        initView();
        readMessage();

    }

    private void initView()
    {
        if (getIntent().getStringExtra("ID") != null) {
            receiverID = getIntent().getStringExtra("ID");
            System.out.println("receiverID : " + receiverID);
            roomOne=userId+"."+receiverID;
        }
        if (getIntent().getStringExtra("NAME") != null)
        {
            receiverName = getIntent().getStringExtra("NAME");
            System.out.println("receiverName : "+receiverName);
        }
        if (getIntent().getStringExtra("URL") != null) {
            url = getIntent().getStringExtra("URL");
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
                .load(url)
                .placeholder(R.mipmap.profile_image)
                .error(R.mipmap.profile_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilepic);

        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);

        audioRecordView.removeAttachmentOptionAnimation(false);

        //setUpRecyclerListener();
        swipeRefreshLayout = containerView.findViewById(R.id.swipeRefreshLayout);
        setUpRecyclerView(containerView);
        //  setUpRecyclerListener();
        loadChatting();
        setSwipeLayout();
    }



    private void setUpRecyclerView(View containerView) {
        recyclerViewMessages = containerView.findViewById(R.id.recyclerViewMessages);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);

        recyclerViewMessages.setHasFixedSize(false);
        chatAdapter = new ChatAdapter(context, chatMessageLinkedList, userId);
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
                showToast("Emoji Icon Clicked");
            }
        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                showToast("Camera Icon Clicked");
                setCameraPermission();
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString().trim();
                audioRecordView.getMessageView().setText("");
                //messageAdapter.add(new Message(msg));
                SendMessage(msg);
            }
        });


        audioRecordView.getMessageView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (count > 0) {
                    if (!mTyping) {
                        mTyping = true;
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
    }

    @Override
    public void onRecordingLocked() {
        showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
        showToast("completed");
        debug("completed");

        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 1) {
            //messageAdapter.add(new Message(recordTime));
        }
    }

    @Override
    public void onRecordingCanceled() {
        showToast("canceled");
        debug("canceled");
    }


    private void debug(String log) {
        Log.d("", log);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layBack:
                finish();
                break;
            case R.id.imgSetting:
                showMenu(view);
                break;
        }

    }


    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {

            case AttachmentOption.DOCUMENT_ID:
                showToast("Document Clicked");
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
        mSocket.on(Constant.TYPING, onTyping);
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
                                mSocket.emit("join", data);

                                JSONObject jsonRoom=new JSONObject();
                                jsonRoom.put("roomOne",roomOne);
                                mSocket.emit("connected_both", jsonRoom);

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
                    showToast("diconnected");
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
                    showToast("Error connecting" + args[0]);
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

                    MessageChat messageChat = new MessageChat();
                    try {

                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setSenderId(data.getString("userId"));
                        chatMessage.setReciverId(data.getString("reciverId"));
                        chatMessage.setMessage(data.getString("message"));
                        chatMessage.setIsRead(true);
                        chatMessage.setReciverName(receiverName);
                        chatMessage.setSenderName(user_name);
                        chatMessageLinkedList.addLast(chatMessage);
                        chatAdapter.notifyDataSetChanged();
                        scrollToBottom();

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }


                    //   conversation.getListMessageData().add(messageChat);


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
                                if (type) {
                                    txtTyping.setText(" typing... ");
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

        mSocket.emit("isTyping", jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                if (args.length > 0) {
                    System.out.println("typing......" + args[0]);
                }
            }
        });
    }

    private void SendMessage(String message) {
        if (!mSocket.connected()) return;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reciverId", receiverID);
            jsonObject.put("userId", userId);
            jsonObject.put("message", message);
            jsonObject.put("senderName", login_user_id);
            jsonObject.put("reciverName", receiverName);
            jsonObject.put("reciverProfileUrl", url);
            jsonObject.put("senderProfileUrl", profileUrl);
            jsonObject.put("createdAt", Utility.getTime1());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(userId);
        chatMessage.setReciverId(receiverID);
        chatMessage.setMessage(message);
        chatMessage.setIsRead(true);
        chatMessage.setReciverName(receiverName);
        chatMessage.setSenderName(user_name);
        chatMessageLinkedList.addLast(chatMessage);
        chatAdapter.notifyDataSetChanged();
        scrollToBottom();
        mSocket.emit("privateMessage", jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                if (args.length > 0) {

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


    private void scrollToBottom() {
        linearLayoutManager.scrollToPosition(chatMessageLinkedList.size() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            JSONObject jsonRoom=new JSONObject();
            jsonRoom.put("roomOne",roomOne);
            mSocket.emit("leaveRoom", jsonRoom);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(Constant.NEW_MESSAGE, onNewMessage);

    }

    public void loadChatting() {
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
                    if (response.body()!=null)
                    {
                        RootChatMessage rootChatMessage = response.body();
                        if (rootChatMessage.getStatus() == 200) {
                            ChatMessageBody chatMessageBody = rootChatMessage.getChatMessageBody();
                            total_pages = chatMessageBody.getTotal_Page();
                            loading = false;
                            LinkedList<ChatMessage> list = new LinkedList<>();
                            list = chatMessageBody.getChatMessages();
                            for (ChatMessage chatMessage : list) {
                                chatMessageLinkedList.addLast(chatMessage);
                            }
    //                        chatAdapter = new ChatAdapter(context, chatMessageArrayList, userId);
    //                        recyclerViewMessages.setAdapter(chatAdapter);
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
                            chatMessageLinkedList.clear();
                            chatAdapter = new ChatAdapter(context, chatMessageLinkedList, userId);
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

    public void readMessage()
    {
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
                        if (response.body().getStatus() == 200)
                        {

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


                            story_type = "image";
                            ByteArray = null;
                            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                            videoThumbBitmap = bm;


//                            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
//                            bm.compress(Bitmap.CompressFormat.PNG, 100, datasecond);
//                            ByteArray = datasecond.toByteArray();
//                            story_imgBase64 = base64String(ByteArray);

                            persistImage(bm, "story_image");

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

                        selectedDocumentPath = getRealPathFromUri(mDocumentUri);
                        docName = getFileName(mDocumentUri);
//                        if (docName.contains(".doc")) {
//                            imgDoc.setImageResource(R.mipmap.docs_story);
//                        }
//                        if (docName.contains(".pdf")) {
//                            imgDoc.setImageResource(R.mipmap.pdf_story);
//                        }
//                        if (docName.contains(".zip")) {
//                            imgDoc.setImageResource(R.mipmap.zip_story);
//                        }
//                        if (docName.contains(".xls")) {
//                            imgDoc.setImageResource(R.mipmap.xls_story);
//                        }


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

    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
                txtTyping.setText(null);
            }
        }
    };
}
