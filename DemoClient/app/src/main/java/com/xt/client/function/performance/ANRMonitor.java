package com.xt.client.function.performance;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;


import java.util.HashMap;
import java.util.Map;

public class ANRMonitor {

    final static String TAG = "anr";

    public static void init(Context context) {
//        if (true) {
//            return;
//        }
        ANRMonitor anrMonitor = new ANRMonitor();
        anrMonitor.start(context);
        Log.i(TAG, "ANRMonitor init");
    }

    private void start(Context context) {
        Looper mainLooper = Looper.getMainLooper();
        mainLooper.setMessageLogging(printer);
        HandlerThread handlerThread = new HandlerThread(ANRMonitor.class.getSimpleName());
        handlerThread.start();
        //时间较长，则记录堆栈
        threadHandler = new Handler(handlerThread.getLooper());
        mCurrentThread = Thread.currentThread();
    }

    private long lastFrameTime = 0L;
    private Handler threadHandler;
    private long mSampleInterval = 40;
    private Thread mCurrentThread;//主线程
    private final Map<String, String> mStackMap = new HashMap<>();

    private Printer printer = new Printer() {
        @Override
        public void println(String it) {
            long currentTimeMillis = System.currentTimeMillis();
            //其实这里应该是一一对应判断的，但是由于是运行主线程中，所以Dispatching之后一定是Finished，依次执行
            if (it.contains("Dispatching")) {
                lastFrameTime = currentTimeMillis;
                //开始进行记录
                threadHandler.postDelayed(mRunnable, mSampleInterval);
                synchronized (mStackMap) {
                    mStackMap.clear();
                }
                return;
            }
            if (it.contains("Finished")) {
                long useTime = currentTimeMillis - lastFrameTime;
                //记录时间
                if (useTime > 20) {
                    //todo 要判断哪里耗时操作导致的
                    Log.i(TAG, "ANR:" + it + ", useTime:" + useTime);
                    //大于100毫秒，则打印出来卡顿日志
                    if (useTime > 100) {
                        synchronized (mStackMap) {
                            Log.i(TAG, "mStackMap.size:" + mStackMap.size());
                            for (String key : mStackMap.keySet()) {
                                Log.i(TAG, "key:" + key + ",state:" + mStackMap.get(key));
                            }
                            mStackMap.clear();
                        }
                    }
                }
                threadHandler.removeCallbacks(mRunnable);
            }
        }
    };


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doSample();
            threadHandler
                    .postDelayed(mRunnable, mSampleInterval);
        }
    };

    protected void doSample() {
        StringBuilder stringBuilder = new StringBuilder();

        for (StackTraceElement stackTraceElement : mCurrentThread.getStackTrace()) {
            stringBuilder
                    .append(stackTraceElement.toString())
                    .append("\n");
        }
        synchronized (mStackMap) {
            mStackMap.put(mStackMap.size() + "", stringBuilder.toString());
        }
    }

}
