package com.xt.client;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xt.client.aidl.ProcessAidlInter;
import com.xt.client.model.UserModel;
import com.xt.client.service.Other2ProcessService;
import com.xt.client.service.OtherProcessService;
import com.xt.client.util.ReflectUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author aa5279aa
 * @date 2019/6/11
 */

public class Main4Activity extends Activity implements View.OnClickListener {

    static final String TAG = "lxltest";
    Messenger mMessager;
    ProcessAidlInter processAidlInter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send) {
//            new Test().test();
            try {
                startRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.btn_close) {
            //反射获取activityThread
            try {
                Activity activity = Main4Activity.this;
                Object mainThread = ReflectUtil.getPrivateField(activity, Activity.class, "mMainThread");

                Log.i("lxltest", mainThread.toString());
                Object global = ReflectUtil.getPrivateField(getWindowManager(), "mGlobal");
                Log.i("lxltest", global.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.test_btn1) {
            Intent intent = new Intent();
            intent.setClass(Main4Activity.this, Other2ProcessService.class);
            bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    processAidlInter = ProcessAidlInter.Stub.asInterface(service);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Service.BIND_AUTO_CREATE);

        } else if (id == R.id.test_btn2) {
            try {
                String processName = processAidlInter.getProcessName();
                String threadName = processAidlInter.getThreadName();
                UserModel userModel = processAidlInter.getUserModel();
                Log.i("lxltest", "processName:" + processName);
                Log.i("lxltest", "threadName:" + threadName);
                Log.i("lxltest", "userModel.pid:" + userModel.pid);


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.test_btn3) {
            Intent intent = new Intent();
            intent.setClass(Main4Activity.this, OtherProcessService.class);
            bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mMessager = new Messenger(service);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Service.BIND_AUTO_CREATE);
            startService(intent);
        } else if (id == R.id.test_btn4) {
            sendMessage();
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i("lxltest", "------>receive response from server!" + msg.what);
        }
    }


    private void sendMessage() {
        if (mMessager == null) {
            return;
        }
        Message msg = new Message();
        msg.what = 1;
        msg.replyTo = new Messenger(new MyHandler());
        Bundle bundle = new Bundle();
        bundle.putString("key", "lxl");
        msg.setData(bundle);
        try {
            mMessager.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        List list = new ArrayList();
        if (list.size() > 0) {

        }

    }


    @SuppressWarnings("AlibabaThreadPoolCreation")
    private void startRequest() throws Exception {

//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
//
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {

        try {
            String url = "https://www.csdn.net/";
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()//默认就是GET请求，可以不写
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: " + response.body().string());
                }
            });
//                    call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            }
//        });
    }

}
