package com.xt.appplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * plugin5为插件中正常使用的。
 */
public class Plugin5Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_plugin5);
        TextView text1 = findViewById(R.id.button1);
        text1.setOnClickListener(v -> {
            setResult(1);
//            Intent intent = new Intent(Plugin5Activity.this, PluginMainActivity.class);
//            startActivity(intent);

        });
        findViewById(R.id.button2).setOnClickListener(v -> {
//            finish();
//            Toast.makeText(Plugin5Activity.this, "button2", Toast.LENGTH_LONG).show();
        });

        String string = getString(R.string.button1);
        text1.setText(string);
        Log.i("lxltest", this.getClass().getSimpleName() + ",onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lxltest", this.getClass().getSimpleName() + ",onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lxltest", this.getClass().getSimpleName() + ",onResume");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i("lxltest", this.getClass().getSimpleName() + ",finish");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}