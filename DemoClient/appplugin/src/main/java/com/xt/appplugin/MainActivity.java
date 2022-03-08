package com.xt.appplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
        findViewById(R.id.text2).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Plugin6Activity.class);
            startActivity(intent);
        });
    }
}
