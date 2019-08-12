package com.xt.client.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ThreadService extends IntentService {

    public ThreadService() {
        super("ThreadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public class MyBinder extends Binder {
        public ThreadService getService(){
            return ThreadService.this;
        }
    }

    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("test_out","----->onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.i("test_out","----->onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("test_out","----->onRebind");
    }

    public int getCount(){
        return (int) (Math.random() * 10);
    }



}
