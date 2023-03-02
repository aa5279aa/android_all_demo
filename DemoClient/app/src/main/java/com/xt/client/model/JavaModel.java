package com.xt.client.model;

import android.util.Log;

public class JavaModel {
    public String name;
    public int age;
    public String moblie;


    public JavaModel(String name) {
        this.name = name;
    }

    public void printJavaLog() {
        Log.i("lxltest", "java log");
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

}
