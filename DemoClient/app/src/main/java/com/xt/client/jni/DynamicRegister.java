package com.xt.client.jni;

import android.app.Activity;

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

    public native String readStrByPath(String path);

    public native void refresh(String path, Activity activity);

    /**
     * 解密
     *
     * @param ciphertext
     * @return
     */
    public native String decrypt(String ciphertext);

    /**
     * 加密
     *
     * @param plaintext
     * @return
     */
    public native String encryption(String plaintext);
}


