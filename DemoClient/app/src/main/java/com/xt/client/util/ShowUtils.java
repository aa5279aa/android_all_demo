package com.xt.client.util;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.xt.client.application.DemoApplication;


public class ShowUtils {
    private static Toast toast;

    public static void showCenterToast(final String text) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(DemoApplication.getInstance(), text, Toast.LENGTH_SHORT);
                }
                toast.setText(text);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }, 0);
    }
}
