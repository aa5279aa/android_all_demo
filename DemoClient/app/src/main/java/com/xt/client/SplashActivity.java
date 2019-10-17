package com.xt.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.xt.client.util.DemoUtils;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    ImageView imgIv;

    private boolean isHaveAllPermission;
    private List<String> requestPermission = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SplashActivity onCreate");
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiFlags |= 0x00001000;
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        setContentView(R.layout.activity_splash);
        imgIv = findViewById(R.id.img_iv);
        checkPermission();
        initData();
    }

    private void initData() {
        Log.d(TAG, "SplashActivity initData");
        loadADs();
        loadFile();
    }

    private void loadADs() {
        Log.d(TAG, "SplashActivity loadADs");
        try {
            imgIv.setBackground(getResources().getDrawable(R.mipmap.splash_bg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        Log.d(TAG, "SplashActivity loadFile");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gotoNextPage();
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 102) {
            return;
        }
        Log.i("lxltest", "permissions:" + permissions.length);
        Log.i("lxltest", "grantResults:" + grantResults.length);
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                requestPermission.remove(permission);
            }
        }
        if (requestPermission.size() == 0) {
            isHaveAllPermission = true;
            gotoNextPage();
        } else {
            DemoUtils.showToast("请赋予权限，否则程序无法运行");
            finish();
        }
    }

    public void gotoNextPage() {
        if (!isHaveAllPermission) {
            return;
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    public void checkPermission() {
        List<String> checkPermission = new ArrayList<>();
        checkPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        checkPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);//
        checkPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);//启动
        checkPermission.add(Manifest.permission.INTERNET);//启动
        checkPermission.add(Manifest.permission.ACCESS_NETWORK_STATE);//启动
        checkPermission.add(Manifest.permission.READ_PHONE_STATE);//启动
        checkPermission.add(Manifest.permission.ACCESS_WIFI_STATE);//启动

        requestPermission.clear();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            for (String permission : checkPermission) {
                if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission.add(permission);
                }
            }
            String[] permissions = new String[requestPermission.size()];
            requestPermission.toArray(permissions);
            if (requestPermission.size() > 0) {
                this.requestPermissions(
                        permissions, 102);
            } else {
                isHaveAllPermission = true;
            }
        } else {
            isHaveAllPermission = true;
        }
    }


}
