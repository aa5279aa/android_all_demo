package com.xt.client.application;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.xt.client.function.serviceprovider.AServiceProviderImpl;
import com.xt.client.function.serviceprovider.BServiceProviderImpl;
import com.xt.client.inter.ServiceProviderInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class DemoApplication extends Application {

    private static DemoApplication instance;
    Handler handler = new Handler();

    public DemoApplication() {
        Log.i("lxltest", "DemoApplication init");
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        aptLaunch();
//        normalLaunch();

    }

    private void aptLaunch() {
        ServiceLoader<ServiceProviderInterface> load = ServiceLoader.load(ServiceProviderInterface.class);
        for (ServiceProviderInterface serviceProviderInterface : load) {
            serviceProviderInterface.onMainThread(this);
        }
        new Thread(() -> {
            for (ServiceProviderInterface serviceProviderInterface : load) {
                serviceProviderInterface.onSelfThread(instance);
            }
        }).start();
    }

    private void normalLaunch() {
        ServiceProviderInterface aServiceProvider = new AServiceProviderImpl();
        ServiceProviderInterface bServiceProvider = new BServiceProviderImpl();

        List<ServiceProviderInterface> list = new ArrayList<>();
        list.add(aServiceProvider);
        list.add(bServiceProvider);

        for (ServiceProviderInterface serviceProviderInterface : list) {
            serviceProviderInterface.onMainThread(this);
        }
        new Thread(() -> {
            for (ServiceProviderInterface serviceProviderInterface : list) {
                serviceProviderInterface.onSelfThread(instance);
            }
        }).start();
    }


    public static DemoApplication getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }
}
