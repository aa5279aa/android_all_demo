package com.xt.client.function.dynamic.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

import androidx.annotation.RequiresApi;

import com.xt.client.HostActivity;
import com.xt.client.R;
import com.xt.client.cache.ObjectCache;
import com.xt.client.function.dynamic.manager.DynamicResourceManager;
import com.xt.client.util.StringUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyInstrumentation extends Instrumentation {

    public static final String ClassLoader = "classLoader";
    public static final String ClassName = "pluginClassName";

    MethodHandle h1;//通过methodHandle调用父类的同名方法


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SoonBlockedPrivateApi")
    public MyInstrumentation() {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            h1 = lookup.findSpecial(Instrumentation.class, "execStartActivity", MethodType.methodType(ActivityResult.class, Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class), MyInstrumentation.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //拦截指定的className
        if (!className.equals("com.xt.client.HostActivity")) {
            return super.newActivity(cl, className, intent);
        }
        String pluginClassName = intent.getStringExtra(ClassName);
        if (StringUtil.emptyOrNull(pluginClassName)) {
            return super.newActivity(cl, className, intent);
        }
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
                //activity中getResource的部分，要替换ContextThemeWrapper中的mResources
                Field declaredField = ContextThemeWrapper.class.getDeclaredField("mResources");
                declaredField.setAccessible(true);
                declaredField.set(activity, plugin);

                Resources.Theme theme = plugin.newTheme();
//                mTheme.setTo(theme);
//                onApplyThemeResource(mTheme, mThemeResource, first);

                /**
                 * 调用ContextThemeWrapper的onApplyThemeResource方法进行关联
                 * theme
                 * mThemeResource
                 * first
                 */
                Field mThemeResourceField = ContextThemeWrapper.class.getDeclaredField("mThemeResource");
                mThemeResourceField.setAccessible(true);
                int mThemeResource = (int) mThemeResourceField.get(activity);

//                Method onApplyThemeResourceMethod = ContextThemeWrapper.class.getDeclaredMethod("onApplyThemeResource", Resources.Theme.class, int.class, boolean.class);
//                onApplyThemeResourceMethod.setAccessible(true);
//                onApplyThemeResourceMethod.invoke(activity, theme, mThemeResource, false);
//                activity.setTheme(theme);

                //如果是xml中的resource的部分，应该替换的是ContextImpl中的mResouces才可以。这个看一下layout中的引用的部分。
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.callActivityOnCreate(activity, icicle);
    }

    //替换这个方法
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        Log.i("lxltest", "");

        //判断是否拦截，拦截的标准是插件的classLoader中是否存在该类
        try {
            Object classLoader = ObjectCache.getInstance().getParams(ClassLoader);
            if (classLoader instanceof ClassLoader) {
                ComponentName component = intent.getComponent();
                String className = component.getClassName();
                Class<?> aClass = ((ClassLoader) classLoader).loadClass(className);//不抛异常就是正常的
                intent.setClass(target, HostActivity.class);
                intent.putExtra(MyInstrumentation.ClassName, className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object invoke = null;
        try {
            invoke = h1.invoke(this, who, contextThread, token, target, intent, requestCode, options);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return (ActivityResult) invoke;
    }

}
