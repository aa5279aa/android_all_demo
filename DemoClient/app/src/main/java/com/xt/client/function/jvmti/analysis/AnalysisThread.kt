package com.xt.client.function.jvmti.analysis

import android.util.Log
import com.xt.client.util.IOHelper
import com.xt.client.util.ToastUtil
import java.io.File

class AnalysisThread : AnalysisBase() {

    /**
     * 直接log输出
     */
    fun analysis(path: String) {
        val io = IOHelper.fromFileToIputStream(File(path))
        val readList = IOHelper.readListStrByCode(io, "utf-8")
        var isFind = false
        for (line in readList) {
            if (!line.startsWith("startThread")) {
                continue
            }
            isFind = true
            val parentThread = findValueByKey(line, "parentThread")
            val where = findValueByKey(line, "where").replace("|", "\n")
            Log.e(
                "AnalysisThread",
                String.format(
                    "find start thread! \n" +
                            "parent thread is:%s \n" +
                            "statck:%s",
                    parentThread,
                    where
                )
            );
        }
        ToastUtil.showCenterToast(if (isFind) "发现创建线程，请查看日志" else "未发现创建线程")
    }


}