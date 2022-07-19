package com.xt.client.activitys

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class TestActivity : BaseActivity() {

    override fun onClick(v: View?) {
        Handler().postDelayed(Runnable {  },2000)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_OPTIONS_PANEL)
        super.onCreate(savedInstanceState)
        getLifecycle().addObserver(MyObserver())
    }

    class MyObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onActivityResume() {
            Log.i("lxltest", "ON_RESUME")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onActivityPause() {
            Log.i("lxltest", "ON_PAUSE")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onActivityDestroy() {
            Log.i("lxltest", "ON_DESTROY")
            val i = 0
            //            StringBuilder builder = new StringBuilder();
//            Log.i("lxltest", "size:" + list.size());
//            for (long time : list) {
//                if (i == 60) {
//                    Log.i("lxltest", builder.toString());
//                    builder.setLength(0);
//                    i = 0;
//                } else {
//                    i++;
//                    builder.append(time + ",");
//                }
//            }
        }
    }


}