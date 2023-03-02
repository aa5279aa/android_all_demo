package com.xt.client.activitys

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.xt.client.R
import com.xt.client.fragment.KOOMFragment.Companion.byteArray

class LeakActivity : FragmentActivity() {

    companion object {
//        var activity: Activity? = null;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak)

        findViewById<TextView>(R.id.text).text = "泄漏的Activity"

        val byteArrayOf = ByteArray(3_1024_1024)
//        activity = this

    }

}