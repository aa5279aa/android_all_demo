package com.xt.client.jni;

public class DynamicRegister {

    static {
        System.loadLibrary("DynamicRegister");
    }

    /**
     * 字符串加密
     *
     * @param str
     * @return
     */
    public native String encryptionStr(String str);

    public static native String staticencryptionStr(String str);

}
