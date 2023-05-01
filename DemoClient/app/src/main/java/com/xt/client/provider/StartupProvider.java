package com.xt.client.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xt.client.util.ToastUtil;


/**
 * Created by lxl.
 */
public class StartupProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        ToastUtil.showCenterToast("query");
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        ToastUtil.showCenterToast("getType");
        return "getType";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i("lxltest", "method_insert:" + Thread.currentThread().getName());
        getContext().sendBroadcast(new Intent("crash"));
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("lxltest", "crash");
//                int i = 5 / 0;
//            }
//        }, 5000);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("lxltest", "insert end");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        ToastUtil.showCenterToast("delete");
        return 10;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        ToastUtil.showCenterToast("update");
        try {
            Thread.sleep(21000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 50;
    }


}