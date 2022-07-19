package com.xt.client.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ThreadService extends IntentService {

    public ThreadService() {
        super("ThreadService");
        Log.i("ThreadService", "ThreadService init");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("ThreadService", "onHandleIntent1");
        try {
            for (int i = 0; i < 21; i++) {
                Log.i("ThreadService", "onHandleIntent sleep:" + i);
                Thread.sleep(10_000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("ThreadService", "onHandleIntent2");
    }

    public class MyBinder extends Binder {
        public ThreadService getService() {
            return ThreadService.this;
        }
    }

    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("test_out", "----->onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.i("test_out", "----->onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("test_out", "----->onRebind");
    }

    public int getCount() {
        return (int) (Math.random() * 10);
    }


}
