package com.xt.client.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log


class Other3ProcessService : Service() {


    override fun onCreate() {
        super.onCreate()
        Log.i("lxltest", "Other3ProcessService_onCreate()")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i("lxltest", "Other3ProcessService_onDestroy()")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}
