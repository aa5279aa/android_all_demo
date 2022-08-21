package com.xt.client.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.xt.client.R
import com.xt.client.util.DemoUtils

class TryCrashFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder?.button1?.text = "开启崩溃捕获"
        viewHolder?.button2?.text = "触发崩溃"
        viewHolder?.button1?.setOnClickListener(this)
        viewHolder?.button2?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {
            Handler().post {
                while (true) {
                    try {
                        Log.i("lxltest", "loop启动")
                        Looper.loop()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (id == R.id.button2) {
            throw NullPointerException("null point")
        }


    }

}
