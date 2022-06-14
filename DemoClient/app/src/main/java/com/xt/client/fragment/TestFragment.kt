package com.xt.client.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xt.client.util.IOHelper
import okhttp3.*
import java.io.*
import java.lang.StringBuilder
import java.net.Socket
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.net.ssl.SSLSocketFactory

class TestFragment : Base2Fragment() {

    lateinit var client: OkHttpClient
    lateinit var service: ExecutorService

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("发送请求")//0
            this.add("发送post请求")//1
            this.add("CSDN访问")//1
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
        }

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