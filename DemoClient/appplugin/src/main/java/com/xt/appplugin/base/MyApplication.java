package com.xt.appplugin.base;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
