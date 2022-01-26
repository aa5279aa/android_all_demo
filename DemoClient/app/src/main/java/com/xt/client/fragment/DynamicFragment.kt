package com.xt.client.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import com.xt.client.HostActivity
import com.xt.client.R
import com.xt.client.cache.ObjectCache
import com.xt.client.function.dynamic.hook.MyInstrumentation
import com.xt.client.function.dynamic.hook.MyResources
import com.xt.client.function.dynamic.manager.DynamicResourceManager
import com.xt.client.jni.PluginJni
import com.xt.client.util.FileUtil
import com.xt.client.util.LogUtil
import com.xt.client.util.ToastUtil
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.ref.WeakReference
import java.lang.reflect.Method

class DynamicFragment : Base2Fragment() {

    companion object {
        const val APK_NAME_DEBUG = "appplugin-debug.apk"
        const val APK_NAME_MEITUAN = "meituan.apk"
        const val APK_ODEX = "app-odex"
        const val TEXT_PATH = "dynamic.txt"
    }

    var selfClassLoader: DexClassLoader? = null
    val state = State()

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("加载插件")//0
            this.add("调用插件apk中方法")//1
            this.add("启动插件apk中activity(需要mainfest注册)")//2
            this.add("启动插件apk中activity(无需mainfest注册)")//3
            this.add("宿主使用插件中的资源")//4
            this.add("启动插件activity(使用插件的资源)")//5
            this.add("使用插件中的so")//6
            this.add("插件化加载美团APP")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //拷贝apk到data文件夹下
        Thread {
            context?.let {
                val file = File(it.filesDir.absolutePath + File.separator + APK_NAME_DEBUG)
                val open = context?.assets?.open(APK_NAME_DEBUG) ?: return@let
                FileUtil.copyFile(open, file, true)

                val file2 = File(it.filesDir.absolutePath + File.separator + APK_NAME_MEITUAN)
                val open2 = context?.assets?.open(APK_NAME_MEITUAN) ?: return@let
                FileUtil.copyFile(open2, file2, true)
                state.isCopyApk = true
            }
        }.start()
    }

    @SuppressLint("ResourceType")
    override fun clickItem(position: Int) {
        if (!state.isCopyApk) {
            ToastUtil.showCenterToast("初始化中，请稍后")
            return;
        }
        if (context == null) {
            return
        }
        val context = context as Context
        if (position == 0) {
            checkLoadApk(context)
            return
        }
        if (position == 1) {
            checkLoadApk(context)
            selfClassLoader?.let {
                val loadClass = it.loadClass("com.xt.appplugin.util.PluginUtil")
                LogUtil.logI("loadClass.name:${loadClass.name}")
                val method: Method = loadClass.getDeclaredMethod(
                    "pluginMethodStaticSplicing",
                    String::class.java,
                    String::class.java
                )
                val invoke = method.invoke(null, "Hello", "World")
                (invoke as? String)?.let { it1 ->
                    showResult(it1)
                }
                return
            }
        }
        if (position == 2) {
            try {
                checkLoadApk(context)
                checkHookClassLoader(context)
                val intent = Intent()
                intent.setClassName(context, "com.xt.appplugin.Plugin1Activity")
                startActivity(intent)
                Log.i("lxltest", "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        if (position == 3) {
            /**
             * 启动acitivyt，通过插桩的方式进行
             * hook Instrumentation
             */
            checkLoadApk(context)
            checkHookClassLoader(context)
            checkInstrumentation(context)
            //hook Instrumentation之后，启动activity
            val intent = Intent(context, HostActivity::class.java)
            intent.putExtra(MyInstrumentation.ClassName, "com.xt.appplugin.Plugin2Activity")
            startActivity(intent)
            return
        }
        if (position == 4) {
            /**
             *  加载资源包
             */
            checkLoadApk(context)
            checkLoadResource(context)
            return
        }
        if (position == 5) {
            //插件的activity中使用插件的png和string
            checkLoadApk(context)
            checkLoadResource(context)
            val intent = Intent(context, HostActivity::class.java)
            intent.putExtra(MyInstrumentation.ClassName, "com.xt.appplugin.Plugin3Activity")
            startActivity(intent)
            return
        }
        if (position == 6) {
            //
//          拷贝so文件到files/lib目录
            val apkPath = context.filesDir.absolutePath + File.separator + APK_NAME_DEBUG
            val toPath = context.filesDir.absolutePath + File.separator + "plugin"
            FileUtil.unzipPack(apkPath, toPath, ".so")
            val soFile = File(toPath + File.separator + "lib/armeabi-v7a/libPluginJni.so")
            if (!soFile.exists()) {
                return
            }
            val pluginJni = PluginJni(soFile.absolutePath)
            val result = pluginJni.pluginSpliceString("aaa", "bbb")
            showResult(result)
            return
        }
        if (position == 7) {
            //加载美团APP
            checkLoadApk(context, APK_NAME_MEITUAN)
            checkHookClassLoader(context)
            checkInstrumentation(context)
            checkLoadResource(context)
            val intent = Intent(context, HostActivity::class.java)
            intent.putExtra(
                MyInstrumentation.ClassName,
                "com.meituan.android.pt.homepage.activity.MainActivity"
            )
            startActivity(intent)
            return
        }
    }


    private fun checkLoadApk(context: Context) {
        checkLoadApk(context, APK_NAME_DEBUG)
    }

    /**
     * 检查是否加载了APP，如果没加载就加载呀
     */
    private fun checkLoadApk(context: Context, name: String) {
        if (state.isLoadApk) {
            return
        }
        state.isLoadApk = true
        val apkPath = context.filesDir.absolutePath + File.separator + name
        val odexPath = context.filesDir.absolutePath + File.separator + APK_ODEX
        selfClassLoader = DexClassLoader(apkPath, odexPath, null, javaClass.classLoader)
        ObjectCache.getInstance().setParams(MyInstrumentation.ClassLoader, selfClassLoader)
    }

    private fun checkHookClassLoader(context: Context) {
        if (state.isHookClassLoader) {
            return
        }
        state.isHookClassLoader = true
        val classLoader = javaClass.classLoader
        val loadedApkClass = classLoader.loadClass("android.app.LoadedApk")
        val activityThreadClass = classLoader.loadClass("android.app.ActivityThread")
        val activityThreadField =
            activityThreadClass.getDeclaredField("sCurrentActivityThread")
        activityThreadField.isAccessible = true
        val activityThreadGet = activityThreadField.get(null)
        val packageField = activityThreadClass.getDeclaredField("mPackages")
        packageField.isAccessible = true
        val arrayMap = packageField.get(activityThreadGet)
        val loadedApkWeak = (arrayMap as ArrayMap<*, *>).get(context.packageName)
        val loadedApkObject = (loadedApkWeak as WeakReference<*>).get()
//                替换classLoader

        val mBaseClassLoaderField = loadedApkClass.getDeclaredField("mBaseClassLoader")
        mBaseClassLoaderField.isAccessible = true
        mBaseClassLoaderField.set(loadedApkObject, selfClassLoader)

        val mDefaultClassLoaderField =
            loadedApkClass.getDeclaredField("mClassLoader")
        mDefaultClassLoaderField.isAccessible = true
        mDefaultClassLoaderField.set(loadedApkObject, selfClassLoader)
    }

    /**
     * 检查是否加载了资源博，如果没加载就加载呀
     */
    private fun checkInstrumentation(context: Context) {
        if (state.isHookInstrumentation) {
            return
        }
        state.isHookInstrumentation = true
        val myInstrumentation =
            MyInstrumentation()
        //替换Acitivty中的mInstrumentation
        val classLoader = javaClass.classLoader
        val activityThreadClass = classLoader.loadClass("android.app.ActivityThread")
        val activityThreadField =
            activityThreadClass.getDeclaredField("sCurrentActivityThread")
        activityThreadField.isAccessible = true
        val activityThreadGet = activityThreadField.get(null)

        val instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
        instrumentationField.isAccessible = true
        instrumentationField.set(activityThreadGet, myInstrumentation)
    }

    /**
     * 检查是否加载了资源博，如果没加载就加载呀
     */
    @SuppressLint("ResourceType")
    private fun checkLoadResource(context: Context) {
        if (state.isHookResources) {
            return
        }
        state.isHookResources = true
        try {
            val string = context.resources.getString(R.string.dynamicload)
//                val assetsManager = context.assets
            val apkPath = context.filesDir.absolutePath + File.separator + APK_NAME_DEBUG
            //反射加载资源包
            val newInstance = AssetManager::class.java.newInstance()
            val setApkAssetsMedthod = AssetManager::class.java.getDeclaredMethod(
                "addAssetPath",
                String::class.java
            )
            setApkAssetsMedthod.invoke(newInstance, apkPath)
            val resources = MyResources(
                newInstance,
                context.resources.displayMetrics,

                context.resources.configuration
            )
            val string1 = resources.getString(0x7f060001)
            showResult(string1)
            DynamicResourceManager.getInstance().resourcesMap["plugin"] = resources
            DynamicResourceManager.getInstance().resourcesMap["host"] = getResources()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class State {
        var isCopyApk = false
        var isLoadApk = false
        var isHookClassLoader = false
        var isHookInstrumentation = false
        var isHookResources = false
    }

}