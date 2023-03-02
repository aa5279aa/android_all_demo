package com.xt.appplugin;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xt.client.jni.PluginJni;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PluginMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        findViewById(R.id.text1).setOnClickListener(v -> {
//            PluginJni pluginJni = new PluginJni();
//            String s = pluginJni.pluginSpliceString("hello", "world");
//            ((TextView) findViewById(R.id.result)).setText(s);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put("key_main", "value_main");
                    String AUTHORITY = "com.xt.client.android_startup.multiple";
                    Uri uri = Uri.parse("content://" + AUTHORITY);
                    ContentProviderClient client = getContentResolver().acquireContentProviderClient(uri);
                    //反射调用呢？
                    try {
//                        client.update()
                        //反射给mAnrTimeout赋值
//                        Field declaredField = ContentProviderClient.class.getDeclaredFields()[0];
//                        Object o = declaredField.get(client);
                        //反射给mAnrRunnable赋值
                        Class<?> aClass = Class.forName("android.app.ActivityThread");
                        Field sCurrentActivityThread = aClass.getDeclaredField("sCurrentActivityThread");
                        sCurrentActivityThread.setAccessible(true);
                        Object activityThread = sCurrentActivityThread.get(null);

                        Class<?> aClass2 = Class.forName("android.app.IActivityManager");
//                        android.app.IActivityManager
//                        void appNotRespondingViaProvider(in IBinder connection);


                        Log.i("lxltest", "");
//                        ActivityManager.getSer


//                        setDetectNotResponding.invoke(client, 5000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        int query = client.update(uri, values, null, null);
                        client.close();
                        Log.i("lxltest", "query:" + query);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });


        findViewById(R.id.text2).setOnClickListener(v -> {
            Log.i("lxltest", "button press");
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    ContentValues values = new ContentValues();
//                    values.put("key_main", "value_main");
//                    String AUTHORITY = "com.xt.client.android_startup.multiple";
//                    Uri uri = Uri.parse("content://" + AUTHORITY);
//                    getContentResolver().insert(uri, values);

                    Intent intent = new Intent(PluginMainActivity.this, Plugin5Activity.class);
                    startActivity(intent);
                }
            }).start();
            Log.i("lxltest", "button end");
        });

        requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_AUDIO, "android.permission.REMOVE_TASKS"}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i("lxltest", "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("lxltest", "onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lxltest", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lxltest", "onPause");
    }
}
