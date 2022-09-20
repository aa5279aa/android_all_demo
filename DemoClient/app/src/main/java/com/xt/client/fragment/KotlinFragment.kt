package com.xt.client.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xt.client.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class KotlinFragment : BaseFragment() {

    companion object {
        val list = ArrayList<String>()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder?.line2?.visibility = View.VISIBLE
        viewHolder?.button1?.text = "测试1"
        viewHolder?.button2?.text = "测试2"
        viewHolder?.button3?.text = "测试3"
        viewHolder?.button4?.text = "测试4"
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        val id = v.id
        if (id == R.id.button1) {
            test1()
            return
        }
        if (id == R.id.button2) {
            test2()
            return
        }
        if (id == R.id.button3) {
            test3()
            return
        }
        if (id == R.id.button4) {
            test4()
        }
    }


    fun test1() {
        lifecycleScope.launch {
            flow {
                Log.i("lxltest", "test1,thread:${Thread.currentThread().name}")
                for (i in 1..4) {
                    kotlinx.coroutines.delay(200)
                    emit(i)
                    Log.i("lxltest", "test1,emit${i},thread:${Thread.currentThread().name}")
                }
            }.flowOn(Dispatchers.IO)//将上面的
                .collect {
                    Log.i("lxltest", "value${it},thread:${Thread.currentThread().name}")
                }
        }
    }


    fun test2() {
        lifecycleScope.launch {
            flow {
                Log.i("lxltest", "test2,thread:${Thread.currentThread().name}")
                for (i in 1..4) {
                    kotlinx.coroutines.delay(200)
                    emit(i)
                }
            }.collect { value ->
                Log.i("lxltest", "value${value},thread:${Thread.currentThread().name}")
            }


        }
    }

    fun test3() {
        Log.i("lxltest", "click test3")
        val flow = flow<Int> {
            for (i in 1..4) {
                delay(200)
                emit(i)
                Log.i("lxltest", "test3,thread:${Thread.currentThread().name}")
            }
        }

        lifecycleScope.launch {
            flow.collect {
                Log.i("lxltest", "test3,value${it},thread:${Thread.currentThread().name}")
            }
        }
    }

    fun test4() {

        lifecycleScope.launch {

            val nums = (1..3).asFlow().onEach { delay(300) }
            val strs = flowOf("one", "two", "three", "four").onEach { delay(400) }
            nums.zip(strs) { a, b ->
                val s = "$a -> $b"
                Log.i("lxltest", "zip:$s")
            }.collect {
                Log.i("lxltest", "result:$it")
            }
        }
    }
}