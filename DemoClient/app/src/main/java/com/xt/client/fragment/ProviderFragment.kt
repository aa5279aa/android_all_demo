package com.xt.client.fragment

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.xt.client.R
import com.xt.client.fragment.base.BaseFragment

class ProviderFragment : BaseFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder?.line2?.visibility = View.VISIBLE
        viewHolder?.button1?.text = "启动service进程"
        viewHolder?.button2?.text = "显示进程名"
        viewHolder?.button3?.text = "显示进程计数次数"
        viewHolder?.button4?.text = "关闭service进程"
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {
            callProvider()
            return
        }

        if (id == R.id.button2) {
            registerContentObserver()
            return
        }
        if (id == R.id.button3) {
            return
        }
        if (id == R.id.button4) {
            checkProviderExist()
            return
        }

    }

    /**
     * provider调用
     */
    fun callProvider() {
        val contentResolver = requireContext().contentResolver
        val uri = Uri.parse("content://com.xt.watch.watchapp.WatchAppProvider")
        //方法名
        val method = "STATE";
        //参数
        val arg = "com.xt.demo"
        contentResolver.call(uri, method, arg, null)?.let {
            uri.host
            val packageName = it.getString("packageName")
            val state = it.getString("state")
            Log.i(TAG, "packageName,${packageName},state:${state}")
        }
    }

    /**
     * 观察某个provider变化
     */
    fun registerContentObserver() {
        val contentResolver = requireContext().contentResolver
        contentResolver.registerContentObserver(
            Uri.parse("content://com.xt.watch.watchapp.WatchAppProvider"), true,
            object : ContentObserver(Handler()) {

                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    super.onChange(selfChange, uri)
                    Log.i(TAG, "onChange,$selfChange,uri:${uri.toString()}")
                }
            })

    }

    /**
     * 检查某个provider是否存在
     */
    fun checkProviderExist() {
        val providerAuthority = "com.xt.watch.watchapp.WatchAppProvider"
        val packageManager = requireContext().packageManager
        // 使用 resolveContentProvider 方法来获取 ContentProvider 的信息
        val providerInfo = packageManager.resolveContentProvider(providerAuthority, 0)
        // 如果 providerInfo 不为 null，表示 ContentProvider 存在
        Log.i(TAG, if (providerInfo != null) "exist" else "non-existent")
        return
    }

}