package com.xt.client.activitys

import android.os.Handler
import java.util.concurrent.Executors

object ContinuousLog {
    val SEND_LOG_MSG = 0X0001
    val LOG_MSG_DATA_KEY = "log_msg_data_key"
    val executor = Executors.newSingleThreadExecutor()
    var mHandler: Handler? = null
    val dataList = mutableListOf<String>()

    fun start(){
        val runnable = Runnable {
            var i = 0
            do {
                Thread.sleep(1)
                d("msg: ${i++}"  )
            }while (true)
        }
        executor.execute(runnable)
    }

    fun d(msg: String){
        // render this msg to active UI View
        mHandler?.let {

        }
    }
}