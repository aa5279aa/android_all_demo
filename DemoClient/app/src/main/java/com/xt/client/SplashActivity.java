package com.xt.client;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.xt.client.util.DemoUtils;
import com.xt.client.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.animation.Animator;
import androidx.core.animation.AnimatorListenerAdapter;
import androidx.core.animation.AnticipateInterpolator;
import androidx.core.animation.ObjectAnimator;
import androidx.core.splashscreen.SplashScreen;

public class SplashActivity extends Activity {

    private String TAG = "SplashActivity";
    ImageView imgIv;

    private boolean isHaveAllPermission;
    private List<String> requestPermission = new ArrayList<>();

    public SplashActivity() {
        Log.i("lxltest", "SplashActivity init");
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SplashActivity onCreate");
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        getWindow().setAttributes(lp);
        View decorView = getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        systemUiVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
        setContentView(R.layout.activity_splash);
        imgIv = findViewById(R.id.img_iv);
        checkPermission();
        initData();
//        final View content = findViewById(android.R.id.content);
//        content.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        // Check if the initial data is ready.
//                        if (false) {
//                            // The content is ready; start drawing.
//                            content.getViewTreeObserver().removeOnPreDrawListener(this);
//                            return true;
//                        } else {
//                            // The content is not ready; suspend.
//                            return false;
//                        }
//                    }
//                });
        splashScreen.setOnExitAnimationListener(splashScreenView -> {
            final ObjectAnimator slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    String.valueOf(View.TRANSLATION_Y),
                    0f,
                    0f
            );
            slideUp.setInterpolator(new AnticipateInterpolator());
            slideUp.setDuration(1000L);

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenView.remove();
                }
            });

            // Run your animation.
            slideUp.start();
        });

    }


    private void fullScreen(Activity activity) {
        //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
        Window window = getWindow();
        View decorView = window.getDecorView();
        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);
        //导航栏颜色也可以正常设置
        window.setNavigationBarColor(Color.WHITE);
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
                    Thread.sleep(5000);
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
        checkPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission.add(Manifest.permission.READ_MEDIA_AUDIO);
            checkPermission.add(Manifest.permission.READ_MEDIA_IMAGES);
            checkPermission.add(Manifest.permission.READ_MEDIA_VIDEO);
        } else {
            checkPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);//启动
        }
        checkPermission.add(Manifest.permission.INTERNET);//启动
        checkPermission.add(Manifest.permission.ACCESS_NETWORK_STATE);//启动
        checkPermission.add(Manifest.permission.READ_PHONE_STATE);//启动
        checkPermission.add(Manifest.permission.ACCESS_WIFI_STATE);//启动

        requestPermission.clear();
        if (Build.VERSION.SDK_INT >= 23) {
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lxltest", "SplashActivity_onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lxltest", "SplashActivity_onResume");
    }
}
