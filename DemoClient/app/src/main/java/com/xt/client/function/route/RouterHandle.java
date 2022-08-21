package com.xt.client.function.route;


import java.util.HashMap;
import java.util.Map;

/**
 * 模式路由处理类
 * 这个类最终由APT生成。
 */
public class RouterHandle {

    Map<String, RouterBase> map = new HashMap<>();

    public RouterHandle() {
        try {
            Class<?> aClass = Class.forName("com.xt.client.RegisterRouter");
            RegisterRouterInter routerInter = (RegisterRouterInter) aClass.newInstance();
            routerInter.init();
            map.putAll(routerInter.getRouteMap());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void jump(String moduleName, String actionName, Object args) throws Exception {
        RouterBase routerBase = map.get(moduleName);
        if (routerBase == null) {
            throw new Exception("RouterBase is unregistered.");
        }
        routerBase.handleAction(actionName, args);
    }
}
