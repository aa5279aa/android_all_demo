package com.xt.client.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.xt.client.R
import com.xt.client.serialize.ProtoSerialize
import com.xt.client.util.DemoUtils
import com.xt.client.util.IOHelper
import com.xt.client.viewmodel.DemoRequest
import com.xt.client.viewmodel.DemoResponse
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.concurrent.Executors

/**
 * 字节码插桩
 */
class InstrumentationFragment : BaseFragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder?.descText?.text = "字节码插桩需要hook打包阶段，"
        viewHolder?.button1?.text = "发送请求"
        viewHolder?.button2?.text = "解析请求"

        viewHolder?.button1?.setOnClickListener(this)
        viewHolder?.button2?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {

        } else if (id == R.id.button2) {
            throw NullPointerException("null point")
        }

    }

}
