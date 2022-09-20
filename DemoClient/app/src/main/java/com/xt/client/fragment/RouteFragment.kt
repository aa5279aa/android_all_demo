package com.xt.client.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xt.client.R
import com.xt.client.function.route.RouterHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RouteFragment : BaseFragment() {
    val routerHandle = RouterHandle()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolder?.line2?.visibility = View.VISIBLE
        viewHolder?.button1?.text = "执行路由1中方法1"
        viewHolder?.button2?.text = "执行路由1中方法2"
        viewHolder?.button3?.text = "执行路由2中方法"
        viewHolder?.button4?.text = "执行路由3中方法"
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        val id = v.id

        if (id == R.id.button1) {
            routerHandle.jump("a_router", "router1_action1", "button1 click in a_router")
            return
        }
        if (id == R.id.button2) {
            routerHandle.jump("a_router", "router1_action2", "button2 click in a_router")
            return
        }
        if (id == R.id.button3) {
            routerHandle.jump("b_router", "router2_action1", "button3 click in b_router")
            return
        }
        if (id == R.id.button4) {
            routerHandle.jump("c_router", "router3_action1", "button4 click in c_router")
        }
    }

}