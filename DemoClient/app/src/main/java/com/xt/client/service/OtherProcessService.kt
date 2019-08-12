package com.xt.client.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log


class OtherProcessService : IntentService("YourUploadService") {

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


    internal inner class ServiceHandler : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what === 1) {
                val data = msg.data
                val string = data.getString("lxl")
                Log.i("lxltest", "------>receive data from client!" + string)
                val replyTo = msg.replyTo

                var sendMsg = Message()
                sendMsg.what = 200
                replyTo.send(sendMsg)

            }
        }
    }

    val messager = Messenger(ServiceHandler())


    override fun onBind(intent: Intent): IBinder? {
        Log.i("lxltest", "----->onBind")
        return messager.binder
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
