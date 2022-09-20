package com.xt.client.function.route;


import android.util.Log;

import com.xt.client.util.ToastUtil;
import com.xt.router_api.Route;
import com.xt.router_api.RouteMethod;

/**
 * 模式路由处理类
 */
@Route(moduleName = "a_router")
public class MyRouter1 implements RouterBase {

    @RouteMethod(methodKey = "router1_action1")
    public void handleRouter1Action1(String text) {
        ToastUtil.showCenterToast(text);
        Log.i("lxltest", "a_sayhello action,args:" + text);
    }


    @RouteMethod(methodKey = "router1_action2")
    public void handleRouter1Action2(Object args) {
        String text = String.valueOf(args);
        ToastUtil.showCenterToast(text);
        Log.i("lxltest", "a_sayhello action,args:" + text);
    }
}
