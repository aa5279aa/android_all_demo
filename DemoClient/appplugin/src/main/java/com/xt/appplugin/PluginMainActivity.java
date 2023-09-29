package com.xt.appplugin;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.xt.appplugin.base.PluginBaseActivity;

public class PluginMainActivity extends PluginBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lxltest", "PluginMainActivity onCreate");
        setContentView(R.layout.layout_main);
        findViewById(R.id.text1).setOnClickListener(v -> {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName("com.xt.client", "com.xt.client.service.ThreadService");
//            intent.setAction("com.xt.client.service.ThreadService");
//            intent.setPackage("com.xt.client");
            intent.setComponent(componentName);
            startForegroundService(intent);
        });

        findViewById(R.id.text2).setOnClickListener(v -> {
            Log.v("lxltest", "level verbose");
            Log.d("lxltest", "level debug");
            Log.i("lxltest", "xxxxx info");
            Log.w("lxltest", "level warn");
            Log.e("lxltest", "level error");
            Log.println(Log.ASSERT, "lxltest", "level assert");
            testStartActivity();
        });

        findViewById(R.id.text3).setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i("lxltest", "button click");
                    testProvider2();
                }
            }).start();
        });

        findViewById(R.id.text4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        });

        requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_AUDIO, "android.permission.REMOVE_TASKS"}, 0);

        try {
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        Log.i("lxltest", "onResume start");
        super.onResume();
        Log.i("lxltest", "onResume end");
    }

    @Override
    protected void onStart() {
        Log.i("lxltest", "onStart start");
        super.onStart();
        Log.i("lxltest", "onStart end");
    }

    @Override
    protected void onStop() {
        Log.i("lxltest", "onStop start");
        super.onStop();
        Log.i("lxltest", "onStop end");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lxltest", "onPause start");
        Log.i("lxltest", "onPause end");
    }

    private void testProvider() {
        ContentValues values = new ContentValues();
        values.put("key_main", "value_main");
        String AUTHORITY = "com.xt.client.android_startup.multiple";
        Uri uri = Uri.parse("content://" + AUTHORITY);
        getContentResolver().insert(uri, new ContentValues());
    }

    private void testProvider2() {
        ContentValues values = new ContentValues();
        values.put("QUERY", "");
        String AUTHORITY = "content://com.beantechs.stemcells.contentprovider.dynamic_authorities2";
        Bundle bundle = new Bundle();
        bundle.putString("shadow_cp_bundle_key", AUTHORITY + "/com.beantechs.sceneengineservice.receiver.DataBaseSyncContentProvider");
//         getContentResolver().call(Uri.parse("content://com.beantechs.stemcells.contentprovider.dynamic_authorities0"), "test", "argtest", bundle);
        Uri uri = Uri.parse(AUTHORITY);
        Bundle query = getContentResolver().call(uri, "QUERY", "", bundle);
        Log.i("lxltest", String.valueOf(query));
    }


    private void testStartActivity() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xt.client", "com.xt.client.MainActivity"));
        startActivity(intent);
    }
}
