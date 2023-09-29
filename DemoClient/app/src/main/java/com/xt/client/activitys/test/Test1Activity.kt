package com.xt.client.activitys.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xt.client.activitys.BaseActivity

class Test1Activity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.descText?.text = this.javaClass.simpleName
        Log.e("launchMode", this::class.java.simpleName)
        Thread.sleep(30000)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val flags = intent?.flags ?: 0
        Log.e(
            "launchMode",
            "onNewIntent,flag:${Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT and flags == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT}"
        )

    }


    override fun onClick(v: View?) {
        Log.e("launchMode", "onClick")
        val intent = Intent(this, Test2Activity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(intent, 100)
//        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("launchMode", "onActivityResult,requestCode:${requestCode},resultCode:${resultCode}")
    }

    override fun onResume() {
        super.onResume()
        Log.e("launchMode", "onResume")
    }

}