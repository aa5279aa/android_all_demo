package com.xt.client.function.route;

import java.util.HashMap;
import java.util.Map;

public abstract class RegisterRouterInter {
    protected Map<String, RouterBase> map = new HashMap<>();

    public abstract void init();

    public Map<String, RouterBase> getRouteMap() {
        return map;
    }
}
