package com.xt.client.jni

import android.content.Context
import android.os.Build
import android.os.Debug
import android.util.Log
import com.xt.client.fragment.JVMTIFragment
import com.xt.client.util.DateUtil
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class JVMTIMonitor {
    var mIsLoaded = false
    var mPath = ""

    companion object {
        init {
            System.loadLibrary("memorymonitor")
        }
    }

    fun init(context: Context, path: String) {
        if (!mIsLoaded) {
            load(context)
        }
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val currentTime = DateUtil.getCurrentTime()
        val file = File(path + File.separator + currentTime + ".txt");
        native_init(file.absolutePath)
        mPath = file.absolutePath
    }

    fun release() {
        native_release()
    }

    fun load(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.i(JVMTIFragment.TAG, "系统版本无法全用JVMTI")
            return
        }
        //查找SO的路径
        val libDir: File = File(context.filesDir, "lib")
        if (!libDir.exists()) {
            libDir.mkdirs()
        }
        //判断So库是否存在，不存在复制过来
        val libSo: File = File(libDir, JVMTIFragment.LIB_NAME)
        if (libSo.exists()) libSo.delete()

//            val fileno = File(context.packageManager.getApplicationInfo("pers.vaccae.memorymonitor", 0).sourceDir)
//            Log.i("jvmti", "fileno Path:$fileno")

        val findLibrary =
            ClassLoader::class.java.getDeclaredMethod("findLibrary", String::class.java)
        val libFilePath = findLibrary.invoke(context.classLoader, "memorymonitor") as String
        Log.i("jvmti", "so Path:$libFilePath")

        Files.copy(
            Paths.get(File(libFilePath).absolutePath), Paths.get(
                libSo.absolutePath
            )
        )


        //加载SO库
        val agentPath = libSo.absolutePath
        System.load(agentPath)

        //开启JVMTI事件监听
        val logDir = File(context.filesDir, "log")
        if (!logDir.exists()) logDir.mkdir()

        //agent连接到JVMTI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //Android 9.0+
            Debug.attachJvmtiAgent(agentPath, null, context.classLoader)
        } else {
            //android 9.0以下版本使用反射方式加载
            val vmDebugClazz = Class.forName("dalvik.system.VMDebug")
            val attachAgentMethod = vmDebugClazz.getMethod("attachAgent", String::class.java)
            attachAgentMethod.isAccessible = true
            attachAgentMethod.invoke(null, agentPath)
        }
        mIsLoaded = true;
    }

    /**
     * 开启JVMTI能力
     *
     * @param path
     */
    external fun native_init(path: String)

    /**
     * 关闭JVMTI能力
     *
     * @param path
     */
    external fun native_release()
}