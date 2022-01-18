package com.xt.client.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import com.xt.client.HostActivity
import com.xt.client.R
import com.xt.client.cache.ObjectCache
import com.xt.client.function.dynamic.hook.MyInstrumentation
import com.xt.client.function.dynamic.manager.DynamicResourceManager
import com.xt.client.util.FileUtil
import com.xt.client.util.LogUtil
import com.xt.client.util.ToastUtil
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.ref.WeakReference
import java.lang.reflect.Method

class DynamicFragment : Base2Fragment() {

    companion object {
        const val APK_NAME = "appplugin-debug.apk"
        const val APK_ODEX = "app-odex"
        const val TEXT_PATH = "dynamic.txt"
    }

    var selfClassLoader: DexClassLoader? = null
    var loadDone = false

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("加载插件")
            this.add("调用插件apk中方法")
            this.add("启动插件apk中activity(需要mainfest注册)")
            this.add("启动插件apk中activity(无需mainfest注册)")
            this.add("宿主使用插件中的资源")
            this.add("启动插件activity(使用插件的资源)")
            this.add("启动插件activity(使用插件的layout)")
            this.add("待补充2")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //拷贝apk到data文件夹下
        Thread {
            context?.let {
                val file = File(it.filesDir.absolutePath + File.separator + APK_NAME)
//                if (file.exists()) {
//                    return@let
//                }
                val open = context?.assets?.open(APK_NAME) ?: return@let
                FileUtil.copyfile(open, file, true)
                loadDone = true;
            }
        }.start()

    }

    @SuppressLint("ResourceType")
    override fun clickItem(position: Int) {
        if (!loadDone) {
            ToastUtil.showCenterToast("初始化中，请稍后")
            return;
        }
        if (context == null) {
            return
        }
        val context = context as Context
        if (position == 0) {
            val apkPath = context.filesDir.absolutePath + File.separator + APK_NAME
            val odexPath = context.filesDir.absolutePath + File.separator + APK_ODEX
            selfClassLoader = DexClassLoader(apkPath, odexPath, null, javaClass.classLoader)
            ObjectCache.getInstance().setParams(MyInstrumentation.ClassLoader, selfClassLoader)
            return
        }
        if (selfClassLoader == null) {
            ToastUtil.showCenterToast("请先点击加载插件")
            return
        }
        if (position == 1) {
            if (selfClassLoader == null) {
                return
            }
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

                //hook了之后启动插件中的activity

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
            val myInstrumentation =
                MyInstrumentation()
            //替换Acitivty中的mInstrumentation

            val classLoader = javaClass.classLoader
            val loadedApkClass = classLoader.loadClass("android.app.LoadedApk")
            val activityThreadClass = classLoader.loadClass("android.app.ActivityThread")


            val activityThreadField =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            activityThreadField.isAccessible = true
            val activityThreadGet = activityThreadField.get(null)

            val instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
            instrumentationField.isAccessible = true

            instrumentationField.set(activityThreadGet, myInstrumentation)

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
            try {
                val string = context.resources.getString(R.string.dynamicload)
//                val assetsManager = context.assets
                val apkPath = context.filesDir.absolutePath + File.separator + APK_NAME
                //反射加载资源包
                val newInstance = AssetManager::class.java.newInstance()
                val setApkAssetsMedthod = AssetManager::class.java.getDeclaredMethod(
                    "addAssetPath",
                    String::class.java
                )
                setApkAssetsMedthod.invoke(newInstance, apkPath)
                val resources = Resources(
                    newInstance,
                    context.resources.displayMetrics,
                    context.resources.configuration
                )
                val string1 = resources.getString(0x7f040001)
                showResult(string1)
                DynamicResourceManager.getInstance().resourcesMap["plugin"] = resources;
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        if (position == 5) {
            //activity中使用插件的png和string
            if (DynamicResourceManager.getInstance().resourcesMap["plugin"] == null) {
                ToastUtil.showCenterToast("请先点击使用插件中的资源")
                return
            }
            val intent = Intent(context, HostActivity::class.java)
            intent.putExtra(MyInstrumentation.ClassName, "com.xt.appplugin.Plugin3Activity")
            startActivity(intent)
        }
    }

}