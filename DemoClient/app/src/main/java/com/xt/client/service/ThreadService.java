package com.xt.client.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class ThreadService extends Service {

    public ThreadService() {
        Log.i("ThreadService", "ThreadService init");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ThreadService", "ThreadService onCreate");
//        try {
//            Thread.sleep(8_000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("ThreadService", "onStartCommand sleep 35");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.xt.appplugin", "com.xt.appplugin.PluginMainActivity"));
                startActivity(intent);
            }
        }, 10_000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
