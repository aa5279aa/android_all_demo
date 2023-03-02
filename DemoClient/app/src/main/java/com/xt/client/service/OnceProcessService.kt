package com.xt.client.service

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class OnceProcessService : IntentService("OnceProcessService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.i("lxltest", "OnceProcessService_onBind start")
        val currentThread = Thread.currentThread()
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("lxltest", "OnceProcessService_onCreate()")
        Thread.sleep(30_000)
    }

    inner class MyBinder : Binder() {
        val service: OnceProcessService
            get() = this@OnceProcessService
    }

    private val mBinder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {
        Log.i("lxltest", "----->onBind")
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {

        Log.i("lxltest", "----->onUnbind")
        return true
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.i("lxltest", "----->onRebind")
    }

    fun getCount(): Int {
        return (Math.random() * 10).toInt()
    }

}
