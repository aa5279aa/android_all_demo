package com.xt.client.cache

import android.view.View
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.xt.client.application.DemoApplication

class PageViewCache {

    companion object {
        var instance: PageViewCache = PageViewCache()
    }

    private constructor() {

    }

    private var cacheMap = HashMap<String, View>()

    fun addCachePageView(pageClassName: String, layoutId: Int) {
        if (cacheMap[pageClassName] != null) {
            return
        }
        val context = DemoApplication.getInstance()
        val inflate = View.inflate(context, layoutId, null)
        inflate.measure(1,1)
        cacheMap.put(pageClassName, inflate)
    }


    fun clearCachePageView(pageClassName: String) {
        cacheMap.remove(pageClassName)
    }

    fun findCachePageView(pageClassName: String): View? {
        return cacheMap.remove(pageClassName)
    }

    fun getCachePageView(pageClassName: String): View? {
        return cacheMap.remove(pageClassName)
    }

}