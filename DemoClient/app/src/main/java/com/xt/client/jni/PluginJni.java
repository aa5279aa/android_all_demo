package com.xt.client.jni;

public class PluginJni {

    //加载so库
    public PluginJni(String path) {
        System.load(path);
    }

    public native String pluginSpliceString(String str, String str2);
}
