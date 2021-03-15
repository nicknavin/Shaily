package com.app.session;

import android.app.Application;

import com.app.session.utility.Foreground;

public class Sessionway extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Foreground.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("App is terminate ");
    }
}
