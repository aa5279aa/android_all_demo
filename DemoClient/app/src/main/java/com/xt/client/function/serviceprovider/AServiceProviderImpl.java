package com.xt.client.function.serviceprovider;

import android.content.Context;
import android.util.Log;

import com.xt.client.inter.ServiceProviderInterface;
import com.xt.router_api.ApplicationInit;


@ApplicationInit(moduleName = "A")
public class AServiceProviderImpl implements ServiceProviderInterface {
    @Override
    public void onMainThread(Context context) {
        Log.i("lxltest", "onMainThread A");
    }

    @Override
    public void onSelfThread(Context context) {
        Log.i("lxltest", "onSelfThread A");
    }
}
