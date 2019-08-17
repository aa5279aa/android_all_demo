package com.xt.client.activitys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.MessageQueue
import android.util.Log
import android.view.View
import com.xt.client.R
import com.xt.client.activitys.prepare.PrepareMiddleActivity
import com.xt.client.cache.PageViewCache

class PrepareActivity : BaseActivity() {

    companion object {
        var isOpenPrepare = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHolder.button2?.text = "跳转到下一界面"
        refreshState()
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button1) {
            isOpenPrepare = !isOpenPrepare
            if (isOpenPrepare) {
                PageViewCache.instance.addCachePageView(PrepareMiddleActivity::class.java.name, R.layout.prepare_middle_page)
            } else {
                PageViewCache.instance.clearCachePageView(PrepareMiddleActivity::class.java.name)
            }
            refreshState()
        } else if (id == R.id.button2) {
            var intent = Intent(this, PrepareMiddleActivity::class.java)
            startActivity(intent)
        }
    }

    fun refreshState() {
        viewHolder.button1?.text = if (isOpenPrepare) "关闭缓存" else "开启缓存"
        viewHolder.resultText?.text = "缓存状态：" + if (isOpenPrepare) "开启" else "关闭"
    }


    override fun onDestroy() {
        super.onDestroy()
        //当前界面返回时界面关闭，则没可能直接跳转到下一界面，所以缓存ViewPage也不需要了，需要删除
        PageViewCache.instance.clearCachePageView(PrepareMiddleActivity::class.java.name)
    }

    override fun onStop() {
        super.onStop()
        if (!isOpenPrepare) {
            return
        }
        //跳转到下一界面完成后会调用该方法，可以完成一些界面释放的工作
        val findCachePageView = PageViewCache.instance.findCachePageView(PrepareMiddleActivity::class.java.name)
        Log.i("lxltest", "缓存状态" + if (findCachePageView == null) "已使用" else "未使用")
    }

    override fun onResume() {
        super.onResume()
        //onResume之后才会把当前view挂载到windowManager，所以在这之后在使用idelHandler生成下一界面缓存
        val idelHandler = MessageQueue.IdleHandler {
            PageViewCache.instance.addCachePageView(PrepareMiddleActivity::class.java.name, R.layout.prepare_middle_page)
            Log.i("lxltest", "添加缓存ViewPage")
            false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Looper.getMainLooper().queue.addIdleHandler(idelHandler)
        } else {

        }

    }

}