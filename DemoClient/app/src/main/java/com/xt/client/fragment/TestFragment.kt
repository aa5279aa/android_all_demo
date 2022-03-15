package com.xt.client.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xt.client.util.IOHelper
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class TestFragment : Base2Fragment() {

    lateinit var client: OkHttpClient
    lateinit var service: ExecutorService

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("发送请求")//0
            this.add("发送post请求")//1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(File(context?.filesDir?.absolutePath + File.separator + "ok"), 100))
        client = builder.build()
        service = Executors.newSingleThreadExecutor()
    }

    @SuppressLint("ResourceType")
    override fun clickItem(position: Int) {
        val builder = Request.Builder()
        val request = builder.url("https://www.baidu.com").build()
        val newCall = client.newCall(request)
        if (position == 0) {
            service.execute {
                val response = newCall.execute()
                val content = IOHelper.readStrByCode(response.body()?.byteStream(), "utf-8")
                logI(content)
            }
            return
        }
        if (position == 1) {
            //异步
            newCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    logI(call.toString());
                }

                override fun onResponse(call: Call, response: Response) {
                    val content = IOHelper.readStrByCode(response.body()?.byteStream(), "utf-8")
                    logI(content)
                }

            })
        }

        if(position == 2){
            val newSingleThreadExecutor = Executors.newSingleThreadExecutor()
            val dispatcher = Dispatcher(newSingleThreadExecutor)
            val builder1 = OkHttpClient.Builder()
            builder1.dispatcher(dispatcher);
            val build = builder.build()
        }

    }

    fun logI(tag: String, content: String) {
        Log.i(tag, content)
    }

    fun logI(content: String) {
        logI("OKHTTP", content)
    }
}