package com.xt.appplugin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TestService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("lxltest", this.getClass().getSimpleName() + ",onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
