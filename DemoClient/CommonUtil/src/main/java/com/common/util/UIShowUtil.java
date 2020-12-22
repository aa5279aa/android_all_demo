package com.common.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author lxl
 * UI辅助功能
 */
public class UIShowUtil {

    public static void showText(TextView textView, CharSequence content) {
        if (textView == null) {
            return;
        }
        if (StringUtil.emptyOrNull(content)) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setText(content);
    }

    public static void showImageView(ImageView imageView, int resourceId) {
        if (imageView == null) {
            return;
        }
        if (resourceId == 0) {
            imageView.setVisibility(View.GONE);
            return;
        }
        imageView.setImageResource(resourceId);
    }

    public static void showTextOrInvisible(View text, CharSequence msg) {
        if (!(text instanceof TextView)) {
            return;
        }
        TextView textView = (TextView) text;

        if (msg == null || StringUtil.emptyOrNull(msg.toString())) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(msg);
        }
    }

    //android9以上，不能复用toast，推荐使用Snackbar
    public static void showToast(final Context context, final CharSequence text) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
