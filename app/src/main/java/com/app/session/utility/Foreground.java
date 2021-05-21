package com.app.session.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.app.session.api.Urls;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.Polling;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Usage:
 * <p>
 * 1. Get the Foreground Singleton, passing a Context or Application object unless you
 * are sure that the Singleton has definitely already been initialised elsewhere.
 * <p>
 * 2.a) Perform a direct, synchronous check: Foreground.isForeground() / .isBackground()
 * <p>
 * or
 * <p>
 * 2.b) Register to be notified (useful in Service or other non-UI components):
 * <p>
 * Foreground.Listener myListener = new Foreground.Listener(){
 * public void onBecameForeground(){
 * // ... whatever you want to do
 * }
 * public void onBecameBackground(){
 * // ... whatever you want to do
 * }
 * }
 * <p>
 * public void onCreate(){
 * super.onCreate();
 * Foreground.get(this).addListener(listener);
 * }
 * <p>
 * public void onDestroy(){
 * super.onCreate();
 * Foreground.get(this).removeListener(listener);
 * }
 */
public class Foreground implements Application.ActivityLifecycleCallbacks {

    public static final long CHECK_DELAY = 500;
    public static final String TAG = Foreground.class.getName();

    public interface Listener {

        public void onBecameForeground();

        public void onBecameBackground();

    }

    private static Foreground instance;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;

    /**
     * Its not strictly necessary to use this method - _usually_ invoking
     * get with a Context gives us a path to retrieve the Application and
     * initialise, but sometimes (e.g. in test harness) the ApplicationContext
     * is != the Application, and the docs make no guarantees.
     *
     * @param application
     * @return an initialised Foreground instance
     */
    public static Foreground init(Application application) {
        if (instance == null) {
            instance = new Foreground();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static Foreground get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static Foreground get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    public static Foreground get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground) {
//            Utility.getLang(activity.getApplicationContext());

            notificationCountApi(activity, "true");
            Log.i(TAG, "went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }
        } else {
            notificationCountApi(activity, "true");
            Log.i(TAG, "still foreground");
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    notificationCountApi(activity, "false");
                    Log.i(TAG, "went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            Log.e(TAG, "Listener threw exception!", exc);
                        }
                    }
                } else {
                    notificationCountApi(activity, "true");
                    Log.i(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        connectWithSocket();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "onActivityStarted");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(TAG, "onActivitySaveInstanceState");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.i(TAG, "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG, "onActivityDestroyed");
    }


    private void notificationCountApi(Activity activity, String flag)
    {
        System.out.println("status "+flag);
        String userID = DataPrefrence.getPref(activity.getApplicationContext(), Constant.USER_ID, "");
        if(flag.equals("false"))
        {

         sendOfflineStatus(userID);
        }
        else
        {
            if (DataPrefrence.getPref(activity.getApplicationContext(), Constant.LOGIN_FLAG, false)) {
                sendOnlineStatus(userID);
            }
        }

    }

    private void sendOnlineStatus(String userId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            mSocket.emit(Constant.JOINMOBILEUSERS, jsonObject, new Ack() {
                @Override
                public void call(Object... args) {

                    if (args.length > 0) {
                        //log("sendOnlineStatus ");
                        System.out.println("sendOnlineStatus "+"true");
                    }
                }
            });

    }
    private void sendOfflineStatus(String userId) {

        System.out.println("sendOfflineStatus");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }



            mSocket.emit(Constant.BACKGROUNDSTATE, jsonObject, new Ack() {
                @Override
                public void call(Object... args) {

                    if (args.length > 0) {
                        //log("sendOnlineStatus ");
                        System.out.println("sendOnlineStatus "+"false");
                    }
                }
            });
        }

//    private void notificationCountApi(Activity activity, String flag) {
//
//
//        if (DataPrefrence.getPref(activity.getApplicationContext(), Constant.LOGIN, false)) {
//            if (isInternetConnected(activity)) {
//
//                new BaseAsych(notificationObject(activity.getApplicationContext(), flag), new ApiCallback() {
//                    @Override
//                    public void result(String x) {
//                        try {
//                            System.out.println(x);
//                            JSONObject responseobject = new JSONObject(x);
//                            if (responseobject.getBoolean("status")) {
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, MyEnum.STATUS_UPDATE).execute();
//            } else {
//
//            }
//        }
//    }
//
//    JSONObject jsonObject;
//
//    private String notificationObject(Context context, String flag) {
//        try {
//            jsonObject = null;
//            jsonObject = new JSONObject();
//            UserData data = DataPrefrence.getUserData(context, Constant.LOGIN_DATA);
//            jsonObject.put("apikey", Urls.API_KEY);
//            jsonObject.put("user_id", data.getId());
//            jsonObject.put("user_token", data.getUser_token());
//            jsonObject.put("flag", flag);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject.toString();
//    }

    public Boolean isInternetConnected(Activity activity) {
        return Methods.isInternetConnected(activity);
    }




    private Socket mSocket;

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
        mSocket.connect();
    }
    public Socket getmSocket()
    {
        if(mSocket!=null) {
            return mSocket;
        }
        return null;
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = new JSONObject();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    {
//                        if(null!=userId)
//                            try {
//                                data.put("userId", userId);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        System.out.println("json "+data);
//                        mSocket.emit("join", data);
//
//
////                        Toast.makeText(getActivity().getApplicationContext(),
////                                R.string.connect, Toast.LENGTH_LONG).show();
////                        try {
////                            IO.Options options = new IO.Options();
////                            options.transports = new String[] { WebSocket.NAME, Polling.NAME};
////                            mSocket = IO.socket(Constant.CHAT_SERVER_URL, options);
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
//                        // showToast("connect");
//                        isConnected = true;
//                    }
//                }
//            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args)
        {
//            runOnUiThread(new Runnable()
//            {
//                @Override
//                public void run() {
//                    Log.i(TAG, "diconnected");
//                    System.out.println("DATA"+args[0]);
//                    isConnected = false;
//
//                    mSocket.connect();
//
////                    Toast.makeText(getActivity().getApplicationContext(),
////                            R.string.disconnect, Toast.LENGTH_LONG).show();
//                }
//            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args)
        {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(TAG, "Error connecting");
//                    // showToast("Error connecting"+args[0]);
//                    System.out.println("DATA"+args[0]);
//                    mSocket.connect();
//                    //Toast.makeText(getActivity().getApplicationContext(),R.string.error_connect, Toast.LENGTH_LONG).show();
//                }
//            });
        }
    };
}
