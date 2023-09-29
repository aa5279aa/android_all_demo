package com.xt.client.service;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class ThreadService extends Service implements LocationListener {
    LocationManager systemService;

    public ThreadService() {
        Log.i("ThreadService", "ThreadService init");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ThreadService", "ThreadService onCreate");
        systemService = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("ThreadService", "onStartCommand sleep 35");
        new Thread(new Runnable() {

            @Override
            public void run() {
                int i = 0;
                while (i < 100) {
                    i++;
                    Log.i("ThreadService", this + "i:" + i);
//                    printLocation(i);
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void printLocation(int i) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<String> providers = systemService.getProviders(true);
        for (String provider : providers) {
            Location location = systemService.getLastKnownLocation(provider);
            if (location == null) {
                Log.i("ThreadService", this + "i:" + i + ",provider:" + provider + ",location:null");
            } else {
                Log.i("ThreadService", this + "i:" + i + ",provider:" + provider + ",Latitude:" + location.getLatitude() + ",time:" + location.getTime());

            }
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("ThreadService", this + ",location.Latitude:" + location.getLatitude());
        systemService.removeUpdates(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
