package com.xt.client.fragment

//import com.xt.client.function.koom.test.LeakedActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.kwai.koom.javaoom.hprof.ForkStripHeapDumper
import com.kwai.koom.javaoom.monitor.OOMMonitor
import com.xt.client.application.DemoApplication
import com.xt.client.function.koom.OOMMonitorInitTask
import com.xt.client.function.koom.test.LeakedActivity
import java.io.File

class KOOMFragment : Base2Fragment() {

    var TAG = "KOOMFragment"
    lateinit var mContext: Context

    companion object {
        var byteArray: ByteArray? = null
    }

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("开启检测")//0
            this.add("泄漏内存")//1
            this.add("输出报告")//2
            this.add("3")//3
            this.add("4")//4
            this.add("5")//5
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireContext()

    }

    override fun clickItem(position: Int) {
        if (position == 0) {
            /*
            * Init OOMMonitor
            */
            OOMMonitorInitTask.init(DemoApplication.getInstance())
            OOMMonitor.startLoop(true, false, 5000L)

            /*
            * Make some leaks for test!
            */
//            LeakMaker.makeLeak(context)
            return
        }
        if (position == 1) {
            //制造泄漏
//            byteArray = ByteArray(100 * 1024 * 1024)
//            LeakMaker.makeLeak(mContext)
            startActivity(Intent(requireContext(), LeakedActivity::class.java))

//            for (i in 0..699) {
//                Thread {
//                    try {
//                        Thread.sleep(200000)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                }.start()
//            }

            return
        }
        if (position == 2) {
            //Pull the hprof from the devices.
            //adb shell "run-as com.kwai.koom.demo cat 'files/test.hprof'" > ~/temp/test.hprof
            ForkStripHeapDumper.getInstance().dump(
                requireContext().getFilesDir().getAbsolutePath() + File.separator + "test.hprof"
            )
            return
        }
        if (position == 3) {
            loopHandlerInvoker = { tttt() }
            val invoke = loopHandlerInvoker?.invoke()
            invoke?.post { }
        }

    }


    fun tttt(): Handler {
        return Handler()
    }


    var loopHandlerInvoker: (() -> Handler)? = null;
}