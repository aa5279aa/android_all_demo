package com.xt.client.application;

import android.app.Application;
import android.os.Handler;

public class DemoApplication extends Application {

    private static DemoApplication instance;
    Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }
}
