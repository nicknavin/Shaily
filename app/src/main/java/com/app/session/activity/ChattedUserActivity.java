package com.app.session.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.app.session.R;
import com.app.session.adapter.AllChatUserAdapter;
import com.app.session.api.Urls;
import com.app.session.base.BaseActivity;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.ChatUserId;
import com.app.session.data.model.ChatedBody;
import com.app.session.data.model.ChatedPersonsResponse;
import com.app.session.data.model.MessageChat;
import com.app.session.network.ApiClient;
import com.app.session.network.ApiInterface;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class ChattedUserActivity extends BaseActivity
{
    private static final String TAG = "ChatActivity";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AllChatUserAdapter allChatUserAdapter;
    private Socket mSocket;
    private Boolean isConnected = true;
    private Handler mTypingHandler = new Handler();
    private static final int TYPING_TIMER_LENGTH = 600;
    ArrayList<ChatedBody> chatedPersonsList;
    //user 37 and psw 37

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectWithSocket();
        getChatedPersons();
    }

    private void initView()
    {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

    }


    public void getChatedPersons()
    {
        if (Utility.isConnectingToInternet(context))
        {
            showLoading();
            ChatUserId data=new ChatUserId();
            data.setUserId(userId);
            ApiInterface apiInterface = ApiClient.getClient(Urls.PRIVATEMESSAGE_URL).create(ApiInterface.class);
           Call<ChatedPersonsResponse> call= apiInterface.reqChatedPersons(accessToken,data);
           call.enqueue(new Callback<ChatedPersonsResponse>()
           {
               @Override
               public void onResponse(Call<ChatedPersonsResponse> call, Response<ChatedPersonsResponse> response)
               {
                   dismiss_loading();
                   if (response.body()!=null) {
                       if(response.body().getStatus()==200)
                       {
                           chatedPersonsList =response.body().getChatedPersonsBody();
                           allChatUserAdapter =new AllChatUserAdapter(context, chatedPersonsList, userId, new ApiItemCallback() {
                               @Override
                               public void result(int position)
                               {
                                   ChatedBody chatedBody=chatedPersonsList.get(position);
                                   String id="",url="",name="";
                                   if(userId.equals(chatedBody.getChatedPersonsBody().getSenderId()))
                                   {
                                       id=chatedBody.getChatedPersonsBody().getReciverId();
                                       name=chatedBody.getChatedPersonsBody().getReciverName();
                                       url=chatedBody.getChatedPersonsBody().getReciverProfileUrl();
                                   }
                                   else
                                   {
                                       id=chatedBody.getChatedPersonsBody().getSenderId();
                                       name=chatedBody.getChatedPersonsBody().getSenderName();
                                       url=chatedBody.getChatedPersonsBody().getSenderProfileUrl();
                                   }
                                   Intent intent=new Intent(context,ChattingActivity.class);
                                   intent.putExtra("ID",id);
                                   intent.putExtra("NAME",name);
                                   intent.putExtra("URL",url);
                                   intent.putExtra("NOTIFICATION_COUNT",chatedBody.getNotification());
                                   startActivity(intent);

                               }
                           });
                           recyclerView.setAdapter(allChatUserAdapter);
                       }
                   }
               }

               @Override
               public void onFailure(Call<ChatedPersonsResponse> call, Throwable t)
               {

               }
           });


        } else {
            showInternetConnectionToast();
        }
    }

    public void connectWithSocket() {
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[] { Polling.NAME };
//            Socket mSocket = IO.socket("http://example.com/", opts);
            mSocket = IO.socket(Constant.CHAT_SERVER_URL,opts);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Constant.NEW_MESSAGE, onNewMessage);
        mSocket.on(Constant.FILES_EVENT, onFiles);
        mSocket.on(Constant.TYPING, onTyping);
        mSocket.connect();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(Constant.NEW_MESSAGE, onNewMessage);

    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = new JSONObject();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    {
                        if(null!=userId)
                            try {
                                data.put("userId", userId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        System.out.println("json "+data);
                        mSocket.emit("join", data);


//                        Toast.makeText(getActivity().getApplicationContext(),
//                                R.string.connect, Toast.LENGTH_LONG).show();
//                        try {
//                            IO.Options options = new IO.Options();
//                            options.transports = new String[] { WebSocket.NAME, Polling.NAME};
//                            mSocket = IO.socket(Constant.CHAT_SERVER_URL, options);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        // showToast("connect");
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

                    mSocket.connect();

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
                   // showToast("Error connecting"+args[0]);
                    System.out.println("DATA"+args[0]);
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
                    //{"message":"sfsdfsdf","userId":"5f437ef1c4b8ce176fc80a91","senderName":"demo5","reciverId":"5f3cfba45a1d392b092e3fb8","reciverName":"demo1","msg_notification_reciver":4,"createdAt":"2020-10-22T13:38:24.850Z"}

                    JSONObject data = (JSONObject) args[0];
                    System.out.println("msg : "+data.toString() );
                    MessageChat messageChat = new MessageChat();
                    try {

                        String senderID=data.getString("senderId");
                        String message=data.getString("message");
                        String notification=data.getString("msg_notification_reciver");


                        for(int i=0;i<chatedPersonsList.size();i++)
                        {
                            ChatedBody chatedBody=chatedPersonsList.get(i);
                            if(chatedBody.getChatedPersonsBody().getSenderId().equals(senderID))
                            {
                                chatedPersonsList.get(i).getChatedPersonsBody().setMessage(message);
                                chatedPersonsList.get(i).setNotification(notification);
                                Collections.swap(chatedPersonsList,0,i);
                                allChatUserAdapter.notifyDataSetChanged();
                            }
                        }





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

                    try {
                        JSONObject data = (JSONObject) args[0];
                        System.out.println("msg : " + data.toString());
//{"reciverProfileUrl":"","senderProfileUrl":"","senderId":"5f3cfba45a1d392b092e3fb8","reciverId":"5f437ef1c4b8ce176fc80a91","file":"userFiles\/chatFiles\/chatFiles-1604558545000-408-Ehx_ryvVkAIUrli.jpg","displayFileName":"Ehx_ryvVkAIUrli.jpg","durationTime":"0","messageType":"image","msg_notification_reciver":4,"createdAt":"2020-11-05T06:42:34.007Z"}

                        String senderID=data.getString("senderId");
                        String messageType=data.getString("messageType");
                        String notification=data.getString("msg_notification_reciver");


                        for(int i=0;i<chatedPersonsList.size();i++)
                        {
                            ChatedBody chatedBody=chatedPersonsList.get(i);
                            if(chatedBody.getChatedPersonsBody().getSenderId().equals(senderID))
                            {
                                chatedPersonsList.get(i).getChatedPersonsBody().setMessageType(messageType);
                                chatedPersonsList.get(i).setNotification(notification);
                                Collections.swap(chatedPersonsList,0,i);
                                allChatUserAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };
    private Emitter.Listener onTyping=new Emitter.Listener() {

        @Override
        public void call(Object... args)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    //{"type":true,"typing":"typing.."}
                    if(args.length>0) {
                        //if (txtTyping != null)
                        {
                            //{"type":true,"typing":"typing..","typerUserId":"5f437ef1c4b8ce176fc80a91","userName":"demo5"}
                            try {
                                //{"type":true,"typing":"typing..","typerUserId":"5f413256c4b8ce176fc80a8f","userName":"bluebird"}


                                JSONObject data = (JSONObject) args[0];

                                boolean type=data.getBoolean("type");

                                System.out.println("data "+data.toString());
                                if(type) {
                                    String typerUserId=data.getString("typerUserId");
                                    allChatUserAdapter.onTyping(typerUserId);
                                  //  txtTyping.setText(" typing... ");
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



    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {

         allChatUserAdapter.onTyping("");
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(Constant.NEW_MESSAGE, onNewMessage);
        mSocket.off(Constant.FILES_EVENT, onFiles);
    }
}