package com.xt.client.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.xt.client.application.DemoApplication
import com.xt.client.util.IOHelper
import com.xt.client.util.ToastUtil
import kotlinx.coroutines.*
import okhttp3.*
import java.io.*
import java.net.Socket
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.SSLSocketFactory
import kotlin.math.log

class TestFragment : Base2Fragment() {

    lateinit var client: OkHttpClient
    lateinit var service: ExecutorService

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("发送请求")//0
            this.add("发送post请求")//1
            this.add("CSDN访问")//2
            this.add("测试协程")//3
            this.add("测试请求")//4
            this.add("ViewModel")//5
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(File(context?.filesDir?.absolutePath + File.separator + "ok"), 100))
//        builder.addInterceptor(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val response = chain.proceed(chain.request())
//                val body = response.body()!!
//                val bytes = body.bytes()
//                val newBytes = decrypt(bytes)
//                //解密转换
//                val buffer = Buffer()
//                buffer.write(newBytes)
//                val newBody =
//                    RealResponseBody(
//                        body.contentType()!!.type() + File.separator + body.contentType()!!
//                            .subtype(),
//                        body.contentLength(),
//                        buffer
//                    )
//                val responseBuilder = response.newBuilder()
//                responseBuilder.body(newBody)
//                val newRresponse = responseBuilder.build()
//                return newRresponse
//            }
//        })
//        builder.addNetworkInterceptor(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                var response = chain.proceed(chain.request())
//                val request = chain.request()
//                val url = request.url()
//                val host = url.host()
//                if (host.endsWith("?cache=1")) {
//                    val urlBuilder = url.newBuilder()
//                    urlBuilder.host(host.replace("?cache=1", ""))
//
//                    val requestBuilder = request.newBuilder()
//                    requestBuilder.url(urlBuilder.build())
//
//                    val responseBuilder = response.newBuilder()
//                    responseBuilder.request(requestBuilder.build())
//                    response = responseBuilder.build()
//                }
//
//                return response
//            }
//        })
        client = builder.build()
        service = Executors.newSingleThreadExecutor()
    }

    private fun decrypt(bytes: ByteArray): ByteArray {
        return bytes;
    }

    @SuppressLint("ResourceType")
    override fun clickItem(position: Int) {
        val builder = Request.Builder()
        val cacheBuilder = CacheControl.Builder()
        cacheBuilder.noStore()
        builder.cacheControl(cacheBuilder.build())
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
            return
        }
        if (position == 2) {
            service.execute {
                request()
            }
            return
        }
        if (position == 3) {
            logI("before test4")
            test4()
            logI("after test4")
            return
        }


        if (position == 4) {
            GlobalScope.launch(Dispatchers.Main) {
                logI("click 4")
                var reqeustTest: String
                val job = withContext(Dispatchers.IO) {
                    delay(1000)
                    reqeustTest = reqeustTest()
                }
                logI("主线程防水:$reqeustTest")
            }
            return
        }
        if (position == 5) {

            Thread(Runnable {
                Toast.makeText(DemoApplication.getInstance(), "Toast", Toast.LENGTH_SHORT).show()
            }).start()
            return
        }
    }

    private fun reqeustTest(): String {
        Thread.sleep(1000)
        throw Exception("error")
        return "1111";
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

    fun test2() {
        logI("test2," + Thread.currentThread().name)
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            logI("world2," + Thread.currentThread().name)
        }
        logI("hello," + Thread.currentThread().name)
        Thread.sleep(2000)
        logI("test3," + Thread.currentThread().name)
    }

    private fun test4() = runBlocking {
//        logI("test4," + Thread.currentThread().name)
//        val launch = launch {
//            delay(5_000)
//            logI("world," + Thread.currentThread().name)
//        }
//
//        logI("先刮起")
////        launch.join()
//        println("hello")
//        delay(2000L)

        val createNames = createNames()
        showNames(createNames)

    }

    fun showNames(sequence: Sequence<String>) {
        val iterator = sequence.iterator()
        var next = iterator.next()
        logI("showNames_$next")
        next = iterator.next()
        logI("showNames_$next")
        next = iterator.next()
        logI("showNames_$next")
    }

    /**
     * 协作式
     */
    fun createNames() = sequence<String> {
        yield("AAA")
        logI("AAA")
        yield("BBB")
        logI("BBB")
        yield("CCC")
        logI("CCC")
    }


    fun logI(tag: String, content: String) {
        Log.i(tag, content)
    }

    fun logI(content: String) {
        logI("TestFragment", content)
    }
}