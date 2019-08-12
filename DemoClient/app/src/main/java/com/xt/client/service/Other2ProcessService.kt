package com.xt.client.service

import android.app.ActivityManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.xt.client.aidl.IClientCallBack
import com.xt.client.aidl.ProcessAidlInter
import com.xt.client.model.UserModel


class Other2ProcessService : IntentService("YourUploadService") {

    var mIsRunning = false
    var mTime = 0
    var mCallback: IClientCallBack? = null

    override fun onHandleIntent(intent: Intent?) {
        Log.i("lxltest", "onBind start")
        val currentThread = Thread.currentThread()
        Log.i("lxltest", mainLooper.thread.toString())
        Log.i("lxltest", currentThread.toString())


        while (mIsRunning) {
            Thread.sleep(5000)
            mTime++
            Log.i("lxltest", "Other2ProcessService,time:" + mTime)
            //如果time为10的倍数，通知activity
            if (mTime % 5 == 0) {
                mCallback?.callback(mTime)
            }

        }

    }

    override fun onCreate() {
        super.onCreate()
        Log.i("lxltest", "onCreate()")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.i("lxltest", "onStart()")
    }

    private val iBinder = object : ProcessAidlInter.Stub() {
        override fun setServiceRunning(state: Boolean): Boolean {
            mIsRunning = state
            return mIsRunning;
        }

        override fun getProcessTime(adjust: Int): Int {
            if (adjust != 0) {
                mTime = adjust
            }
            return mTime
        }

        override fun registerCallBack(callback: IClientCallBack?) {
            mCallback = callback
        }

        override fun getProcessName(): String {
            return getCurProcessName(baseContext)
        }

        override fun getThreadName(): String {
            return Thread.currentThread().name
        }

        override fun getUserModel(): UserModel {
            var userModel = UserModel()
            userModel.pid = android.os.Process.myPid()
            return userModel
        }

    }

    fun getCurProcessName(context: Context): String {
        var pid = android.os.Process.myPid()
        var mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE)
        (mActivityManager as? ActivityManager).let {
            for (info in it?.runningAppProcesses!!) {
                if (info.pid == pid) {
                    return info.processName
                }
            }
        }
        return ""
    }


    override fun onBind(intent: Intent): IBinder? {
        Log.i("lxltest", "----->onBind")
        return iBinder
    }

    override fun onUnbind(intent: Intent): Boolean {

        Log.i("lxltest", "----->onUnbind")
        return true
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.i("lxltest", "----->onRebind")
    }

}
