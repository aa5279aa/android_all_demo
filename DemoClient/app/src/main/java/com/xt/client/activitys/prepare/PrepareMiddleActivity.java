package com.xt.client.activitys.prepare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xt.client.R;
import com.xt.client.activitys.PrepareActivity;
import com.xt.client.cache.PageViewCache;

import androidx.annotation.Nullable;

public class PrepareMiddleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long currentTimeMillis = System.currentTimeMillis();
        View cachePageView = PageViewCache.Companion.getInstance().getCachePageView(PrepareMiddleActivity.class.getName());
        if (cachePageView != null) {
            setContentView(cachePageView);
        } else {
            setContentView(R.layout.prepare_middle_page);
        }
        long useTime = System.currentTimeMillis() - currentTimeMillis;
        TextView descText = findViewById(R.id.desc_text);
        String str = "缓存状态:" + ((PrepareActivity.Companion.isOpenPrepare() ? "开启" : "关闭") + ",useTime:" + useTime);
        descText.setText(str);
        Log.i("lxltest", str);
    }
}
