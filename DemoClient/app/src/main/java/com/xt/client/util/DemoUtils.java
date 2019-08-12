package com.xt.client.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.xt.client.application.DemoApplication;

public class DemoUtils {

    /**
     * 不care是否主线程
     */
    public static void showToast(final String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 不care是否主线程
     */
    public static void showToast(final String message, final int duration) {
        final DemoApplication instance = DemoApplication.getInstance();
        if (TextUtils.isEmpty(message) || instance == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(instance, message, duration);
                toast.show();
            }
        });
    }
}
