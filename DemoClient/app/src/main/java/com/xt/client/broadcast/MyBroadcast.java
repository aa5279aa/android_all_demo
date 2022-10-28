package com.xt.client.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcast extends BroadcastReceiver {

    public MyBroadcast() {
        Log.i("lxltest", "MyBroadcast()");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("lxltest", "MyBroadcast.onReceive");
    }
}
