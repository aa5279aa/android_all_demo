package com.xt.client.fragment

import android.os.Bundle
import android.view.View
import com.xt.client.fragment.base.BaseMultiFragment
import com.xt.client.function.jvmti.AnalysisThread
import com.xt.client.jni.JVMTIMonitor
import com.xt.client.model.UserModel
import com.xt.client.util.ToastUtil
import java.io.File

/**
 * JVMTI的功能
 */
class JVMTIFragment : BaseMultiFragment() {
    companion object {
        const val TAG = "jvmti"
        const val LIB_NAME = "libmemorymonitor.so"
    }

    var monitor = JVMTIMonitor()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onItemClick(0)
    }

    override fun getButtonList(): List<String> {
        return ArrayList<String>().apply {
            this.add("开始性能监控")
            this.add("关闭性能监控")
            this.add("申请一个对象")
            this.add("占坑不用")
            this.add("创建一个线程")
            this.add("查看日志中线程创建")
        }
    }

    override fun onItemClick(postion: Int) {
        if (postion == 0) {
            ToastUtil.showCenterToast("initJVMTI")
            val externalFilesDir = requireContext().getExternalFilesDir("")
            val root = File(externalFilesDir, "jvmti")
            monitor.init(requireContext(), root.absolutePath)
            return
        }
        if (postion == 1) {
            ToastUtil.showCenterToast("releaseJVMTI")
            monitor.release()
            return
        }
        if (postion == 2) {
            ToastUtil.showCenterToast("new Object:UserModel")
            val userModel = UserModel()
            return
        }
        if (postion == 4) {
            ToastUtil.showCenterToast("start Thread")
            createThread()
            return
        }
        if (postion == 5) {
            //分析日志，确定哪个线程和堆栈创建的线程
            AnalysisThread().analysis(monitor.mPath)
            return
        }
    }


    fun createThread() {
        Thread {
            var i = 0;
            while (i++ < 10) {
                Thread.sleep(1000)
            }
        }.start()
    }

}