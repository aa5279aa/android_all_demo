package com.xt.client.function.route;

import java.util.HashMap;
import java.util.Map;

public abstract class RegisterRouterInter {
    protected Map<String, RouterProxyInter> map = new HashMap<>();

    public abstract void init();

    public void handleAction(RouterProxyInter routerProxy, String actionName, Object args) {
        routerProxy.handleAction(actionName, args);
    }

    public Map<String, RouterProxyInter> getRouteMap() {
        return map;
    }
}
