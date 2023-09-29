package com.xt.client.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tencent.mmkv.MMKV
import com.xt.client.fragment.base.Base2Fragment
import com.xt.client.function.retrofit.Api
import okhttp3.OkHttpClient
import java.util.concurrent.ExecutorService
import kotlin.random.Random

class MMKVFragment : Base2Fragment() {

    var TAG = "MMKVFragment"
    lateinit var client: OkHttpClient
    lateinit var service: ExecutorService
    lateinit var api: Api

    override fun getShowData(): List<String> {
        return mutableListOf<String>().apply {
            this.add("SP方式写入1000次Int")//0
            this.add("MMKV方式写入1000次Int")//1
            this.add("SP方式写入1000次Boolean")//0
            this.add("MMKV方式写入1000次Boolean")//1
            this.add("SP方式写入1000次String")//0
            this.add("MMKV方式写入1000次String")//1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MMKV.initialize("/sdcard/mmkv")

    }

    override fun clickItem(position: Int) {
        val random = Random(1000)
        if (position == 0 || position == 2 || position == 4) {
            val sp = when (position) {
                0 -> {
                    requireContext().getSharedPreferences("sp_int", MODE_PRIVATE);
                }
                2 -> {
                    requireContext().getSharedPreferences("sp_boolean", MODE_PRIVATE);
                }
                else -> {
                    requireContext().getSharedPreferences("sp_string", MODE_PRIVATE);
                }
            }
            val edit = sp.edit()
            val currentTimeMillis = System.currentTimeMillis()
            for (i in 0 until 1000) {
                when (position) {
                    0 -> {
                        edit.putInt("key$i", random.nextInt())
                    }
                    1 -> {
                        edit.putBoolean("key$i", true)
                    }
                    else -> {
                        edit.putString("key$i", "key$i")
                    }
                }
                edit.commit()
            }
            Log.i(TAG, "SP spendTime:${System.currentTimeMillis() - currentTimeMillis}")
            return
        }
        if (position == 1 || position == 3 || position == 5) {
            val kv = when (position) {
                1 -> {
                    MMKV.defaultMMKV(0, "sp_int")
                }
                3 -> {
                    MMKV.defaultMMKV(0, "sp_boolean")
                }
                else -> {
                    MMKV.defaultMMKV(0, "sp_string")
                }
            }
            for (key in kv.all) {

            }

            val currentTimeMillis = System.currentTimeMillis()
            for (i in 0 until 1000) {
                if (position == 1) {
                    kv.putInt("key$i", random.nextInt())
                } else if (position == 3) {
                    kv.putBoolean("key$i", true)
                } else {
                    kv.putString("key$i", "key$i")
                }
            }
            Log.i(TAG, "MMKV spendTime:${System.currentTimeMillis() - currentTimeMillis}")
        }
    }

}