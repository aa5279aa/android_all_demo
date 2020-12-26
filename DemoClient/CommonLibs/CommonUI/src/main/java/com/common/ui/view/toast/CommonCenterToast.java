package com.common.ui.view.toast;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.ui.R;
import com.common.util.StringUtil;

/**
 * author: guangbing
 */
public class CommonCenterToast {
    private View ll;
    private static CommonCenterToast commonCenterToast;
    private Toast toast;

    public CommonCenterToast() {
    }

    public void ToastShow(Context context, String content) {
        ll = LayoutInflater.from(context).inflate(R.layout.layout_common_toast, null);
        ImageView iv = ll.findViewById(R.id.mt_iv);
        TextView tv = ll.findViewById(R.id.mt_tv);
        tv.setText(content);
        toast = new Toast(context);
        toast.setView(ll);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    public static void showMessage(Context context, String msg) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        new Handler(context.getMainLooper()).post(() -> {
            if (commonCenterToast == null) {
                commonCenterToast = new CommonCenterToast();
            }
            commonCenterToast.ToastShow(context, msg);
        });
    }
}