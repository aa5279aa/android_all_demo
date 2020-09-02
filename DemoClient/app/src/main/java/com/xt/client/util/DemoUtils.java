package com.xt.client.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.xt.client.application.DemoApplication;

import java.io.InputStream;
import java.security.MessageDigest;

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


    public static String getMD5Checksum(InputStream fis) throws Exception {
        byte[] b = createChecksum(fis);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static byte[] createChecksum(InputStream fis) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }
}
