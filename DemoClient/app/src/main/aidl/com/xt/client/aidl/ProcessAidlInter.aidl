// ProcessAidlInter.aidl
package com.xt.client.aidl;
import com.xt.client.model.UserModel;
import com.xt.client.aidl.IClientCallBack;

// Declare any non-default types here with import statements

interface ProcessAidlInter {
    void registerCallBack(IClientCallBack callback);
    String getProcessName();
    String getThreadName();
    UserModel getUserModel();
    int getProcessTime(int adjust);
    boolean setServiceRunning(boolean state);
}
