package com.xt.appplugin;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * 插件activity中使用插件中的资源
 */
public class Plugin3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        setContentView(R.layout.layout_plugin3);
        String string = resources.getString(R.string.plugin_str3);
        ((TextView) findViewById(R.id.text2)).setText(string);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            Thread.sleep(20_000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}