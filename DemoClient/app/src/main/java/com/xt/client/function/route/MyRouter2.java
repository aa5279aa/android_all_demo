package com.xt.client.function.route;


import android.util.Log;

import com.xt.client.util.ToastUtil;
import com.xt.router_api.Route;
import com.xt.router_api.RouteMethod;

/**
 * 模式路由处理类
 */
@Route(moduleName = "b_router")
public class MyRouter2 implements RouterBase {
    @RouteMethod(methodKey = "router2_action1")
    public void handleRouter2Action1(String text) {
        ToastUtil.showCenterToast(text);
        Log.i("lxltest", "a_sayhello action,args:" + text);
    }
}
