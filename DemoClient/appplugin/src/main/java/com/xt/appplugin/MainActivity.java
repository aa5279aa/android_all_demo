package com.xt.appplugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xt.client.jni.PluginJni;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        findViewById(R.id.text1).setOnClickListener(v -> {
            PluginJni pluginJni = new PluginJni();
            String s = pluginJni.pluginSpliceString("hello", "world");
            ((TextView) findViewById(R.id.result)).setText(s);
        });
    }
}
