package com.app.session.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SocketIOService extends Service implements SocketEventListener.Listener
{
    private static final String EVENT_JOIN = "join";
    private static final String EVENT_RECEIVED = "received";
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onEventCall(String event, Object... objects)
    {

    }




}
