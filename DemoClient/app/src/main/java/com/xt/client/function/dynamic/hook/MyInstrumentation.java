package com.xt.client.function.dynamic.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ContextThemeWrapper;

import com.xt.client.cache.ObjectCache;
import com.xt.client.function.dynamic.manager.DynamicResourceManager;

import java.lang.reflect.Field;

public class MyInstrumentation extends Instrumentation {

    public static final String ClassLoader = "classLoader";
    public static final String ClassName = "pluginClassName";

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //拦截指定的className
        if (!className.equals("com.xt.client.HostActivity")) {
            return super.newActivity(cl, className, intent);
        }
        String pluginClassName = intent.getStringExtra(ClassName);
        //插件的classLoader
        Object classLoader = ObjectCache.getInstance().getParams(ClassLoader);
        if (classLoader instanceof ClassLoader) {
            Class<?> aClass = ((ClassLoader) classLoader).loadClass(pluginClassName);
            Object o = aClass.newInstance();
            return (Activity) o;
        }

        return super.newActivity(cl, className, intent);
    }

//    @Override
//    public void callActivityOnCreate(Activity activity, Bundle icicle,
//                                     PersistableBundle persistentState) {
//        //如果插件存在，则替换activity的resource
//
//        super.callActivityOnCreate(activity, icicle, persistentState);
//    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        //替换掉resource
        Resources plugin = DynamicResourceManager.getInstance().resourcesMap.get("plugin");
        if (plugin != null) {
            try {
                Field declaredField = ContextThemeWrapper.class.getDeclaredField("mResources");
                declaredField.setAccessible(true);
                declaredField.set(activity, plugin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.callActivityOnCreate(activity, icicle);


    }
}
