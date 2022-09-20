package com.xt.client.function.route;


import java.util.HashMap;
import java.util.Map;

/**
 * 模式路由处理类
 * 这个类最终由APT生成。
 */
public class RouterHandle {

    Map<String, RouterProxyInter> map = new HashMap<>();
    RegisterRouterInter routerInter;

    public RouterHandle() {
        try {
            Class<?> aClass = Class.forName("com.xt.client.RegisterRouter");
            routerInter = (RegisterRouterInter) aClass.newInstance();
            routerInter.init();
            map.putAll(routerInter.getRouteMap());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void jump(String moduleName, String actionName, Object args) throws Exception {
        RouterProxyInter proxyInter = map.get(moduleName);
        if (proxyInter == null) {
            throw new Exception("RouterBase is unregistered.");
        }
        routerInter.handleAction(proxyInter, actionName, args);
    }
}
