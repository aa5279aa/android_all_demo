package com.xt.client.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcast3 extends BroadcastReceiver {

    public MyBroadcast3() {
        Log.i("lxltest", getClass().getSimpleName());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("lxltest", getClass().getSimpleName() + ",onReceive");

    }
}
