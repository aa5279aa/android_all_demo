package com.xt.client.inter;

import android.content.Context;

public interface ServiceProviderInterface {

    public void onMainThread(Context context);

    public void onSelfThread(Context context);

}
