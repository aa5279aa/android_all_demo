package com.xt.client.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import com.xt.client.function.retrofit.Api
import com.xt.client.function.retrofit.RetrofitManager
import com.xt.client.util.IOHelper
import kotlinx.coroutines.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.lang.StringBuilder
import java.net.Socket
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.SSLSocketFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RetrofitFragment : Base2Fragment() {

    lateinit var client: OkHttpClient
    lateinit var service: ExecutorService
    lateinit var api: Api

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("普通请求")//0
            this.add("协程同步")//1
            this.add("协程异步")//2
            this.add("Retrofit请求")//3
            this.add("协程+Retrofit")//3
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = RetrofitManager.getRetrofit().create(Api::class.java)
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(File(context?.filesDir?.absolutePath + File.separator + "ok"), 100))
        client = builder.build()
        service = Executors.newSingleThreadExecutor()
    }


    @SuppressLint("ResourceType")
    override fun clickItem(position: Int) {
        val builder = Request.Builder()
        val cacheBuilder = CacheControl.Builder()
        cacheBuilder.noStore()
        builder.cacheControl(cacheBuilder.build())
        val request = builder.url("https://www.baidu.com").get().build()
        val newCall = client.newCall(request)
        if (position == 0) {
            service.execute {
                val response = newCall.execute()
                val content = IOHelper.readStrByCode(response.body()?.byteStream(), "utf-8")
                logI(content)
            }
            return
        }

        val mainScope = MainScope()
        val newSingleThreadExecutor = Executors.newSingleThreadExecutor()
        if (position == 1) {
            val launch = mainScope.launch {
                Log.i("lxltest", "start" + Thread.currentThread().name)
                val arg1 = sunpendF1()
                var arg2 = sunpendF2()

//                withContext(Dispatchers.Main) {
                Log.i("lxltest", "suspend finish arg1:$arg1  arg2:$arg2  result:${arg1 + arg2}")
//                }
            }
            newSingleThreadExecutor.execute {
                Thread.sleep(100)
                launch.cancel()
            }

            return
        }

        if (position == 2) {
            val coroutineDispatcher = newSingleThreadExecutor.asCoroutineDispatcher()

            mainScope.async {
                Log.i("lxltest", "start")
                val arg1 = async(coroutineDispatcher) {
                    sunpendF1()
                }
                val arg2 = async(coroutineDispatcher) {
                    sunpendF2()
                }

                val result = arg1.await() + arg2.await()

                Log.i("lxltest", "suspend finish，result:${result}")
            }
        }

        if (position == 3) {
            Log.i("lxltest", "Retrofit请求")
            val androidResult =  api.androidResult
            androidResult.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("lxltest", "onFailure")
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.i("lxltest", "response:" + response.code())
                }

            })
            return
        }
        if (position == 4) {
            mainScope.launch(Dispatchers.IO) {
                val requestData = requestData()
                withContext(Dispatchers.Main) {
                    Log.i("lxltest", "result:${requestData.string()}")
                }
            }
            return
        }
    }

    suspend fun requestData(): ResponseBody = suspendCancellableCoroutine { continuation ->
        //获取当前协程实例
        api.androidResult.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body != null) {
                    continuation.resume(body)
                } else {
                    continuation.resumeWithException(NullPointerException("Response Body is Null : $response"))
                }
            }
        })
    }

    private suspend fun sunpendF1(): Int {
//        delay(1000)
        Thread.sleep(1000)
        Log.i("lxltest", "suspend fun 1," + Thread.currentThread().name)
        return 2
    }

    private suspend fun sunpendF2(): Int {
        Thread.sleep(2000)
//        delay(2000)
        Log.i("lxltest", "suspend fun 2," + Thread.currentThread().name)
        return 4
    }

    private fun request() {
        try {
            val socket: Socket =
                SSLSocketFactory.getDefault().createSocket("www.baidu.com", 443)
            val bw = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
            //注意\r\n代表换行
            //注意\r\n代表换行
            bw.write("GET /?client=1 HTTP/1.1\r\n")
            //这里第二个\r\n代表空行，空行是http协议里要求的，不能省略
            //这里第二个\r\n代表空行，空行是http协议里要求的，不能省略
            bw.write("Host: www.baidu.com\r\n\r\n")
            bw.flush()
            val readListStrByCode = IOHelper.readStrByCode(socket.getInputStream(), "utf-8")
            logI(readListStrByCode)
            val br = BufferedReader(InputStreamReader(socket.getInputStream()))
            val stringBuilder = StringBuilder()
            while (true) {
                var readLine: String? = null
                if (br.readLine().also { readLine = it } != null) {
                    stringBuilder.append(readLine)
                } else {
                    break
                }
            }
            logI(stringBuilder.toString())

        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun logI(tag: String, content: String) {
        Log.i(tag, content)
    }

    fun logI(content: String) {
        logI("TestFragment", content)
    }
}