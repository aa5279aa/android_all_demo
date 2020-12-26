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
public class CommonToast {

    public CommonToast() {
    }

    public static Toast makeText(Context context, String content, int type) {
        View ll = LayoutInflater.from(context).inflate(R.layout.common_toast, null);
        Toast toast = new Toast(context);
        toast.setView(ll);
        toast.setDuration(type);
        return toast;
    }

}