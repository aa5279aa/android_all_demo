package com.common.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


public class ToastUtil {

    public static void showToast(Context context, String tip) {
        new Handler(context.getMainLooper()).post(() -> Toast.makeText(context, tip, Toast.LENGTH_SHORT).show());
    }
}
