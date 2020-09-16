package com.xt.client.activitys

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Printer
import android.view.Choreographer
import android.view.View
import com.xt.client.R

class PerformanceActivity : BaseActivity() {
    //Choreographer kao ri ao ge fe
    var printerBuilder = StringBuilder()
    var hander = Handler()
    var stopLooperCheck = false
    var stopChoreographerCheck = false
    var lastFrameTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.button1?.text = "Looper检测"
        viewHolder.button2?.text = "Choreographer检测"
        viewHolder.button3?.text = "停止"
        viewHolder.button4?.text = "清空"

        viewHolder.descText?.visibility = View.VISIBLE
        viewHolder.line2?.visibility = View.VISIBLE

        var desc1 = "Looper检测原理是在Looper.loop方法中，msg.target.dispatchMessage(msg)预计的前后记录时间，相减即为主线程耗时"
        var desc2 = "Choreographer检测原理是在Choreographer（UI刷新控制类）注册绘制回调，两次刷新之间的耗时即为界面绘制时间"

        viewHolder.descText?.text = desc1.plus("\n").plus(desc2)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {
            stopLooperCheck = false
            stopChoreographerCheck = true
            lastFrameTime = 0L
            printerBuilder.setLength(0)

            val mainLooper = Looper.getMainLooper()
            mainLooper.setMessageLogging(printer)
            showPrinterResult()
        } else if (id == R.id.button2) {
            stopChoreographerCheck = false
            stopLooperCheck = true
            lastFrameTime = 0L
            printerBuilder.setLength(0)

            val choreographer = Choreographer.getInstance()
            choreographer.postFrameCallback(frameCallback)
//            Choreographer.getInstance().postFrameCallback { frameCallback }//这两种写法有什么不同吗？
            showPrinterResult()
        } else if (id == R.id.button3) {
            stopLooperCheck = true
            stopChoreographerCheck = true
        } else if (id == R.id.button4) {
            printerBuilder.setLength(0)
            viewHolder.resultText?.text = ""
        }
    }

    private fun showPrinterResult() {
        if (isFinishing || (stopLooperCheck && stopChoreographerCheck)) {
            return
        }
        //每3秒刷新一次结果
        viewHolder.resultText?.text = printerBuilder
        printerBuilder.setLength(0)
        hander.postDelayed({
            run {
                showPrinterResult()
            }
        }, 3000)
    }


    val printer = Printer {
        val currentTimeMillis = System.currentTimeMillis()
        //其实这里应该是一一对应判断的，但是由于是运行主线程中，所以Dispatching之后一定是Finished，依次执行
        if (it.contains("Dispatching")) {
            lastFrameTime = currentTimeMillis
//            printerBuilder.append("\n" + it)
            return@Printer
        }
        if (it.contains("Finished")) {
            var useTime = lastFrameTime - currentTimeMillis
            printerBuilder.append("\n" + it + ",useTime:" + useTime)
        }
    }

    internal var frameCallback: Choreographer.FrameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
//            regisFrameCallBack()
            Choreographer.getInstance().postFrameCallback(this)
            if (lastFrameTime == 0L) {
                lastFrameTime = frameTimeNanos
                return
            }
            val userTime = (frameTimeNanos - lastFrameTime) / 1000 / 1000
            lastFrameTime = frameTimeNanos
            printerBuilder.append("绘制耗时：" + userTime + "\n")
        }
    }

    val frameCall = Choreographer.FrameCallback {
        val currentTimeMillis = System.currentTimeMillis()
        if (lastFrameTime == 0L) {
            lastFrameTime = currentTimeMillis
            regisFrameCallBack()
            return@FrameCallback
        }
        val userTime = lastFrameTime - currentTimeMillis
//        if (userTime > 20) {
        printerBuilder.append("绘制超时，耗时：" + userTime + "\n")
//        }
        regisFrameCallBack()
    }

    fun regisFrameCallBack() {
//        Choreographer.getInstance().removeFrameCallback { frameCall }


        Choreographer.getInstance().postFrameCallback { frameCallback }
    }


}