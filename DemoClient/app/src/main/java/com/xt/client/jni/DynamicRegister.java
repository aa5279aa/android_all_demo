package com.xt.client.jni;

import android.app.Activity;

public class DynamicRegister {

    static {
        System.loadLibrary("DynamicRegister");
    }

    /**
     * 拼接字符串str1和str2
     * @param str1
     * @param str2
     * @return
     */
    public native String spliceString(String str1,String str2);

    /**
     * 读取指定路径的文件内容
     * @param path
     * @return
     */
    public native String readStrByPath(String path);

    /**
     * JNI线程通知安卓刷新
     * @param path
     * @param activity
     */
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

    /**
     * 根据类名查找父类名
     * @param class
     * @return
     */
    public native Class getSuperClassName(String className);

    /**
     * 字符串加密
     *
     * @param str
     * @return
     */
    public native String encryptionStr(String str);

    public static native String staticencryptionStr(String str);
}


