package com.xt.client.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.xt.client.application.DemoApplication;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.Nullable;
//import fi.iki.elonen.NanoHTTPD;

public class MyHttpService extends Service {

//    App app;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        try {
//            app = new App(DemoApplication.getInstance());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public static class App extends NanoHTTPD {
//        Context context;
//
//        public App(Context context) throws IOException {
//            super(8080);
//            this.context = context;
//            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
//            Log.i("lxltest", "Running! Point your browsers to http://localhost:8080/");
//        }
//
//
//        @Override
//        public Response serve(IHTTPSession session) {
//
//            Intent intent = new Intent();
//            //第一种方式
//            ComponentName cn = new ComponentName("com.commonrail.mft.decoder.pc", "com.commonrail.mft.decoder.ui.SplashActivity");
//            try {
//                intent.setComponent(cn);
//                //第二种方式
//                //intent.setClassName("com.example.fm", "com.example.fm.MainFragmentActivity");
////                intent.putExtra("test", "intent1");
//                if (context != null) {
//                    context.startActivity(intent);
//                }
//            } catch (Exception e) {
//                //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
//            }
//
//            String msg = "<html><body><h1>Hello server</h1>\n";
//            Map<String, String> parms = session.getParms();
//            if (parms.get("username") == null) {
//                msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
//            } else {
//                msg += "<p>Hello, " + parms.get("username") + "!</p>";
//            }
//            return newFixedLengthResponse(msg + "</body></html>\n");
//        }
//    }
}
