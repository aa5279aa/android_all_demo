package com.xt.client.jni;

import com.xt.client.model.JavaModel;

public class CalculationJNITest {

    static {
        System.loadLibrary("Calculation");
    }

    /**
     * @param value1
     * @param value2
     * @param str1
     * @param double1
     * @param chars
     * @param bytes
     * @return
     */
    public native String calculationSum(int value1, int value2, String str1, double double1, char[] chars, byte[] bytes);

    /**
     * JNI修改属性值
     * @param javaModel
     * @return
     */
    public native Object updateObjectValue(JavaModel javaModel);

}
