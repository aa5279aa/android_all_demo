package com.xt.client.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.xt.client.R
import com.xt.client.serialize.ProtoSerialize
import com.xt.client.util.IOHelper
import com.xt.client.viewmodel.DemoRequest
import com.xt.client.viewmodel.DemoResponse
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.concurrent.Executors

/**
 * baseUrl:http://127.0.0.1:5389/ProtobufWeb/test/serviceCode=10001&uid=S001
 */
class ProtobuffFragment : BaseFragment() {

    //如果真做服务，后续其实服务号和uid也应该加入序列化的内容
    var baseUrl = "http://127.0.0.1:5389/ProtobufWeb/test/serviceCode=10001&uid=S001"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewHolder?.button1?.text = "发送请求"
        viewHolder?.button2?.text = "解析请求"

        viewHolder?.button1?.setOnClickListener(this)
        viewHolder?.button2?.setOnClickListener(this)


    }

    var i = 0;
    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {
            startDemoRequest()
        } else if (id == R.id.button2) {
//            throw NullPointerException("null point")
            Thread(Runnable {
                viewHolder?.resultText?.text = "haha" + (i++)
            }).start()
        }

    }

    private fun startDemoRequest() {
        val demoRequest = DemoRequest()
        demoRequest.valueInt = 100
        demoRequest.valueInt64 = 1000
        demoRequest.valueString = "suc哈"

        val bytes = ProtoSerialize.serialize(demoRequest)//对象序列化为byte数组

        val threadPool = Executors.newSingleThreadExecutor()

        threadPool.execute {
            try {
                var rulConnection: URLConnection? = null// 此处的urlConnection对象实际上是根据URL的
                val url =
                    URL("http://10.32.151.155:5389/ProtobufWeb/test/serviceCode=10001&uid=S001")//不同的接口返回不同的数据
                rulConnection = url.openConnection()
                val httpUrlConnection = rulConnection as HttpURLConnection?
                httpUrlConnection!!.requestMethod = "POST"
                httpUrlConnection.connect()
                httpUrlConnection.outputStream.write(bytes)
                httpUrlConnection.outputStream.flush()
                httpUrlConnection.outputStream.close()

                val inStrm = httpUrlConnection.inputStream
                val input2byte = IOHelper.input2byte(inStrm)
                val demoResponse = DemoResponse()
                ProtoSerialize.unSerialize(demoResponse, input2byte)

                Log.i(
                    "lxltest",
                    "result:" + demoResponse.result + ",resultCode:" + demoResponse.resultCode + ",resultMessage:" + demoResponse.resultMessage
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
