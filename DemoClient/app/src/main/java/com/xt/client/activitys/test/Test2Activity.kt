package com.xt.client.activitys.test

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.xt.client.activitys.BaseActivity

class Test2Activity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.descText?.text = this.javaClass.simpleName
        Log.e("launchMode",this::class.java.simpleName)
    }

    override fun onClick(v: View?) {
        startActivity(Intent(this, Test3Activity::class.java))
    }
}