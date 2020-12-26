package com.common.util;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class LogUtil {

    private static String TAG = "lxltest";
    private static boolean mIsShow = false;
    private static Set<String> filterSet = new HashSet<>();

    public static void init(String filterStr, boolean isShow) {
        filterSet.clear();
        mIsShow = isShow;
        String[] split = filterStr.split(",");
        for (String str : split) {
            if (StringUtil.emptyOrNull(str)) {
                continue;
            }
            filterSet.add(str);
        }
    }

    public static void d(String message) {
        log(null, null, message, 2);
    }

    public static void d(String tab, String message) {
        log(null, tab, message, 2);
    }

    public static void i(String message) {
        log(null, null, message, 3);
    }

    public static void i(String tab, String message) {
        log(null, tab, message, 3);
    }

    public static void w(String message) {
        log(null, null, message, 4);
    }

    public static void w(String tab, String message) {
        log(null, tab, message, 4);
    }

    public static void e(String message) {
        log(null, null, message, 5);
    }

    public static void e(String tab, String message) {
        log(null, tab, message, 5);
    }

    private static void log(Context context, String tab, String message, int level) {
        if (!mIsShow) {
            return;
        }
        if (StringUtil.emptyOrNull(tab)) {
            tab = TAG;
        }
        if (filterSet.size() > 0 && !filterSet.contains(tab)) {
            return;
        }
        String content = message + "---" + "thread:" + Thread.currentThread().getName();
        if (context != null) {
            String currentProcessName = DeviceUtil.getCurrentProcessName(context);
            content = message + "---" + "ProcessName:" + currentProcessName + ",thread:" + Thread.currentThread().getName();
        }
        switch (level) {
            case 2:
                Log.d(tab, content);
                break;
            case 4:
                Log.w(tab, content);
                break;
            case 5:
                Log.e(tab, content);
                break;
            default:
                Log.i(tab, content);
        }
    }

    public static String getStackTrace() {
        try {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            if (sts != null) {
                StringBuilder builder = new StringBuilder();
                builder.append(Thread.currentThread().getName() + ",\n");
                for (StackTraceElement st : sts) {
                    if (st.isNativeMethod()) {
                        continue;
                    }
                    if (st.getClassName().equals(Thread.class.getName())) {
                        continue;
                    }
                    if (st.getClassName().equals(LogUtil.class.getName())) {
                        continue;
                    }
                    builder.append("at " + st.getClassName() + "." + st.getMethodName()
                            + "(" + st.getFileName() + ":" + st.getLineNumber() + ")\n");
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
