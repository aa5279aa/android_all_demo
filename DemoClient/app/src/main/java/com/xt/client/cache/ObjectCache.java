package com.xt.client.cache;

import java.util.HashMap;
import java.util.Map;

public class ObjectCache {
    public static Map<String, Object> map = new HashMap<>();
    public static volatile ObjectCache instance;

    public static ObjectCache getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (ObjectCache.class) {
            if (instance == null) {
                instance = new ObjectCache();
            }
        }
        return instance;
    }

    public void setParams(String key, Object o) {
        map.put(key, o);
    }

    public Object getParams(String key) {
        return map.get(key);
    }

    public void removeParams(String key) {
        map.remove(key);
    }
}
