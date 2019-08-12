package com.xt.client.application;

import android.app.Application;

public class DemoApplication extends Application {

    private static DemoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static DemoApplication getInstance() {
        return instance;

    }
}
