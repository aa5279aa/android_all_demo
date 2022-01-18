package com.xt.client;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.xt.client.function.dynamic.manager.DynamicResourceManager;

/**
 * 这个类甚至都不需要创建
 */
public class HostActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Resources getResources() {
        //这里hook一下，插件中的资源，则要通过插件的resource获取。
//        如果使用到hostActivity，那么一定是插件中的activity
        Resources plugin = DynamicResourceManager.getInstance().resourcesMap.get("plugin");
        if (plugin != null) {
            return plugin;
        }
        return super.getResources();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
