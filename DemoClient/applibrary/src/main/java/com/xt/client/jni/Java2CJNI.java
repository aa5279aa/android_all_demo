package com.xt.client.jni;

public class Java2CJNI {

    //加载so库
    static {
        System.loadLibrary("Java2C");
    }

    public String java2C2() {
        return "123";
    }

    //native方法
    public native String java2C();

}