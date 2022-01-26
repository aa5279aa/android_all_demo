package com.xt.client.jni;

public class PluginJni {

    //加载so库
    static {
        System.loadLibrary("PluginJni");
    }


    public native String pluginSpliceString(String str, String str2);
}