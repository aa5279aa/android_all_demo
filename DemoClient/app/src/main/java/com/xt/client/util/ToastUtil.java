package com.xt.client.util;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import com.xt.client.application.DemoApplication;


/**
 * toast显示
 * 后续考虑Snackbar替代
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context, String tip) {
        showMessage(tip);
    }

    public static void showMessage(int resId) {
        showMessage(null);
    }

    public static void showMessage(CharSequence text) {
        showMessage(text, 0);
    }

    public static void showMessage(int resId, long delayMillis) {
        showMessage(null, delayMillis);
    }

    //android9以上，不能复用toast，推荐使用Snackbar
    public static void showMessage(final CharSequence text, long delayMillis) {
        DemoApplication.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast == null || (Build.VERSION.SDK_INT >= 28)) {
                    toast = Toast.makeText(DemoApplication.getInstance(), text, Toast.LENGTH_LONG);
                }
                toast.setText(text);
                toast.show();
            }
        }, delayMillis);
    }

    public static void showCenterToast(final String text) {
        DemoApplication.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast == null || (Build.VERSION.SDK_INT >= 28)) {
                    toast = Toast.makeText(DemoApplication.getInstance(), text, Toast.LENGTH_SHORT);
                }
                toast.setText(text);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }, 0);
    }
}
