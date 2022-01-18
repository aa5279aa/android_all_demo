package com.xt.client.function.dynamic.manager;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class DynamicResourceManager {

    private static volatile DynamicResourceManager instance;
    public Map<String, Resources> resourcesMap = new HashMap<>();


    public static DynamicResourceManager getInstance() {
        if (instance == null) {
            synchronized (DynamicResourceManager.class) {
                if (instance == null) {
                    instance = new DynamicResourceManager();
                }
            }
        }
        return instance;
    }
}
