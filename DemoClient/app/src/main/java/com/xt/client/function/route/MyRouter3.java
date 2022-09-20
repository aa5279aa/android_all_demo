package com.xt.client.function.route;


import android.util.Log;

import com.xt.client.util.ToastUtil;
import com.xt.router_api.Route;
import com.xt.router_api.RouteMethod;

/**
 * 模式路由处理类
 */
@Route(moduleName = "c_router")
public class MyRouter3 implements RouterBase {
    @RouteMethod(methodKey = "router3_action1")
    public void handleRouter3Action1(Object args) {
        String text = String.valueOf(args);
        ToastUtil.showCenterToast(text);
        Log.i("lxltest", "a_sayhello action,args:" + text);
    }
}
