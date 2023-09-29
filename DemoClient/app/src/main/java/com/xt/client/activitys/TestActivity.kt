package com.xt.client.activitys

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.xt.client.R

class TestActivity : BaseActivity() {

    override fun onClick(v: View?) {
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_OPTIONS_PANEL)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.test1_layout)
        Log.i("lxltest", "TestActivity.onCreate")
//        Thread(Runnable() {
//            var i = 0;
//            while (i++ < 20) {
//                val toString = Looper.getMainLooper().thread.stackTrace.toString()
//                Log.i("lxltest", toString)
//                Thread.sleep(1000)
//            }
//        }).start()
    }

    override fun onResume() {
        super.onResume()
        Log.i("lxltest", "TestActivity.onResume")
//        Handler().post(Runnable {
//            Thread.sleep(20_000)
//        })
    }


}