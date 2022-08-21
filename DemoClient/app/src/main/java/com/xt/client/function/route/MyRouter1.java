package com.xt.client.function.route;


import android.util.Log;

import com.xt.client.util.ToastUtil;
import com.xt.router_api.Route;

/**
 * 模式路由处理类
 */
@Route(moduleName = "a_router")
public class MyRouter1 implements RouterBase {
    @Override
    public void handleAction(String action, Object args) {
        if ("a_sayhello".equals(action)) {
            String text = String.valueOf(args);
            ToastUtil.showCenterToast(text);
            Log.i("lxltest", "a_sayhello action,args:" + text);
        }
    }
}
