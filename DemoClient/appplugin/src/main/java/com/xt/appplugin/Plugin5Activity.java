package com.xt.appplugin;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

/**
 * plugin4为插件中正常使用的。
 */
public class Plugin4Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        setContentView(R.layout.layout_plugin3);
        String string = resources.getString(R.string.plugin_str1);
        ((TextView) findViewById(R.id.text2)).setText(string);
    }
}