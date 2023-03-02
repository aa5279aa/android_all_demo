package com.xt.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xt.client.function.serialize.ProtoSerialize;
import com.xt.client.util.IOHelper;
import com.xt.client.viewmodel.DemoRequest;
import com.xt.client.viewmodel.DemoResponse;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author aa5279aa
 * @date 2019/6/11
 */

public class ClickTestActivity extends Activity implements View.OnClickListener {

    Handler mHandler = new Handler();

    public ClickTestActivity() {
        super();
        Log.i("lxltest", getClass().getName() + ",init");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lxltest", getClass().getName() + ",onCreate");
        setContentView(R.layout.click_layout);

        final Button findViewById = findViewById(R.id.btn_send);
        findViewById.setVisibility(View.VISIBLE);
        findViewById(R.id.container).setOnClickListener(this);
        findViewById.setOnClickListener(this);

        int i = findViewById.hashCode();
        getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String x) {
                Log.i("lxltest", x);
            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send) {
            Log.i("lxltest", "btn_send");
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });

            Message lxltest = Message.obtain(mHandler, new Runnable() {
                @Override
                public void run() {
                    Log.i("lxltest", "mHandler.run");
                }
            });
            lxltest.what = 200;

//            mHandler.obtainMessage(200);

            lxltest.sendToTarget();
        } else if (id == R.id.container) {
            Toast.makeText(ClickTestActivity.this, "container", Toast.LENGTH_SHORT).show();
            Log.i("lxltest", "container");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lxltest", getClass().getName() + ",onStart");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Button) findViewById(R.id.btn_send)).setText("哈哈哈啊哈");
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ((Button)findViewById(R.id.btn_send)).setText("哈哈哈啊哈");
//            }
//        }).start();
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.i("lxltest", getClass().getName() + ",onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lxltest", getClass().getName() + ",onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lxltest", getClass().getName() + ",onDestroy");
    }

    @SuppressWarnings("AlibabaThreadPoolCreation")
    private void startDemoRequest() throws Exception {

        DemoRequest demoRequest = new DemoRequest();
//        demoRequest.valueInt = 100
//        demoRequest.valueInt64 = 1000
        demoRequest.valueString = "suc哈";

        final byte[] bytes = ProtoSerialize.serialize(demoRequest);

        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URLConnection rulConnection = null;// 此处的urlConnection对象实际上是根据URL的
                    URL url = new URL("http://10.32.151.155:5389/ProtobufWeb/test/serviceCode=10001&uid=S001");//不同的接口返回不同的数据
                    rulConnection = url.openConnection();

                    // 请求协议(此处是http)生成的URLConnection类
                    // 的子类HttpURLConnection,故此处最好将其转化
                    // 为HttpURLConnection类型的对象,以便用到
                    // HttpURLConnection更多的API.如下:

                    HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
                    httpUrlConnection.setRequestMethod("POST");
                    httpUrlConnection.connect();
                    httpUrlConnection.getOutputStream().write(bytes);
                    httpUrlConnection.getOutputStream().flush();
                    httpUrlConnection.getOutputStream().close();

                    InputStream inStrm = httpUrlConnection.getInputStream();
                    byte[] input2byte = IOHelper.input2byte(inStrm);
                    DemoResponse demoResponse = new DemoResponse();
                    ProtoSerialize.unSerialize(demoResponse, input2byte);

                    Log.i("lxltest", "result:" + demoResponse.result + ",resultCode:" + demoResponse.resultCode + ",resultMessage:" + demoResponse.resultMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
