package com.xt.client;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.xt.client.function.dynamic.manager.DynamicResourceManager;

/**
 * 这个类甚至都不需要创建
 */
public class HostActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lxltest","");
        setContentView(R.layout.content_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
