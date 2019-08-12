package com.xt.client.jni;

public class CalculationJNITest {

    static {
        System.loadLibrary("Calculation");
    }

    //native方法
    public native String calculationSum(int value1, int value2, String str1, double double1, char[] chars, byte[] bytes);

}
