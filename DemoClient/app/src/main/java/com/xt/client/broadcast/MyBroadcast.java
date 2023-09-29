package com.xt.client.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MyBroadcast extends BroadcastReceiver {

    public MyBroadcast() {
        Log.i("lxltest", getClass().getSimpleName());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("lxltest", getClass().getSimpleName() + ",onReceive");
        for (int i = 0; i < 20; i++) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
