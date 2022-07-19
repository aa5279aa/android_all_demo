package com.xt.client.util;

import android.util.Log;

import com.xt.client.constant.Constant;

public class LogUtil {

//    static {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void logI(String message) {
        logI("", message);
    }

    public static void logI(String tag, String message) {
        Log.i(Constant.TAG + tag, message);
    }
}
