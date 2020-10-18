package com.app.session.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.adapter.ChatAdapter;
import com.app.session.audio_record_view.AttachmentOption;
import com.app.session.audio_record_view.AttachmentOptionsListener;
import com.app.session.audio_record_view.AudioRecordView;
import com.app.session.base.BaseActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomEditText;
import com.app.session.customview.CustomTextView;
import com.app.session.model.Conversation;
import com.app.session.model.MessageChat;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChatActivity extends BaseActivity implements View.OnClickListener , AudioRecordView.RecordingListener, AttachmentOptionsListener {
    private static final String TAG = "ChatActivity";
    String receiverID = "", receiverName = "";
    CustomEditText edtChatBox;
    private Socket mSocket;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ChatAdapter chatAdapter;
    Conversation conversation;
    ImageView imgProfilepic;

String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (getIntent().getStringExtra("ID") != null) {
            receiverID = getIntent().getStringExtra("ID");
            System.out.println("receiverID : "+receiverID);
        }
        if (getIntent().getStringExtra("NAME") != null) {
            receiverName = getIntent().getStringExtra("NAME");
        }
        if (getIntent().getStringExtra("URL") != null) {
            url = getIntent().getStringExtra("URL");
        }
        conversation = new Conversation();

        initView();
        connectWithSocket();
    }




    private void initView() {



        ((ImageView) findViewById(R.id.imgSend)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.layBack)).setOnClickListener(this);
        imgProfilepic=(CircleImageView)findViewById(R.id.imgProfilepic);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        edtChatBox = (CustomEditText) findViewById(R.id.edtChatBox);
        ((CustomTextView)findViewById(R.id.txtUserName)).setText(receiverName);
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.profile_image)
                .error(R.mipmap.profile_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfilepic);




        edtChatBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        chatAdapter = new ChatAdapter(context, conversation, userId);
//        recyclerView.setAdapter(chatAdapter);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSend:
                attemptSend();
                break;
            case R.id.layBack:
                finish();
                break;
        }
    }

    private void attemptSend() {

        if (!mSocket.connected()) return;


        String message = edtChatBox.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            edtChatBox.requestFocus();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reciverId",receiverID);
            jsonObject.put("userId",userId);
            jsonObject.put("message",message);
            jsonObject.put("senderName",user_name);
            jsonObject.put("reciverName",receiverName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edtChatBox.setText("");
//        addMessage("fadel", message);

        // perform the sending message attempt.
        MessageChat messageChat = new MessageChat();
        messageChat.text=message;
        messageChat.idSender=userId;
        messageChat.idReceiver=receiverID;
        messageChat.timestamp=Long.parseLong(Utility.getTimeStamp());
        conversation.getListMessageData().add(messageChat);
        chatAdapter.notifyDataSetChanged();
        linearLayoutManager.scrollToPosition(conversation.getListMessageData().size() - 1);
        mSocket.emit("privateMessage", jsonObject, new Ack() {
            @Override
            public void call(Object... args) {

                if (args.length>0) {

                }
            }
        });
    }

    private Boolean isConnected = true;

    public void connectWithSocket() {
        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new_message", onNewMessage);
        mSocket.connect();
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = new JSONObject();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected)
                    {
                        if(null!=userId)
                        try {
                            data.put("userId", userId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            mSocket.emit("join", data);

//                        Toast.makeText(getActivity().getApplicationContext(),
//                                R.string.connect, Toast.LENGTH_LONG).show();
                        showToast("connect");
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    System.out.println("DATA"+args[0]);
                    isConnected = false;
                    showToast("diconnected");
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    showToast("Error connecting"+args[0]);
                    System.out.println("DATA"+args[0]);
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.error_connect, Toast.LENGTH_LONG).show();
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
                        messageChat.text=data.getString("message");
                        messageChat.idSender=data.getString("userId");
                        messageChat.idReceiver=data.getString("reciverId");
                        messageChat.timestamp=Long.parseLong(Utility.getTimeStamp());
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    conversation.getListMessageData().add(messageChat);
                    chatAdapter.notifyDataSetChanged();
                    scrollToBottom();

                }
            });
        }
    };


    private void scrollToBottom() {
        linearLayoutManager.scrollToPosition(conversation.getListMessageData().size() - 1);
    }

    @Override
    public void onRecordingStarted() {

    }

    @Override
    public void onRecordingLocked() {

    }

    @Override
    public void onRecordingCompleted() {

    }

    @Override
    public void onRecordingCanceled() {

    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {

    }
}
