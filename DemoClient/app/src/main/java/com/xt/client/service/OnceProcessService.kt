package com.xt.client.service

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class OnceProcessService : IntentService("OnceProcessService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.i("lxltest", "onBind start")
        val currentThread = Thread.currentThread()
        Log.i("lxltest", mainLooper.thread.toString())
        Log.i("lxltest", currentThread.toString())
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("lxltest", "onCreate()")
    }


    inner class MyBinder : Binder() {
        val service: OnceProcessService
            get() = this@OnceProcessService
    }

    private val mBinder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {
        Log.i("test_out", "----->onBind")
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {

        Log.i("test_out", "----->onUnbind")
        return true
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.i("test_out", "----->onRebind")
    }

    fun getCount(): Int {
        return (Math.random() * 10).toInt()
    }

}
