package com.xt.client.function.route;


public class MyRouter1ProxyDemo implements RouterProxyInter {
    MyRouter1 mMyRouter1;
    public MyRouter1ProxyDemo(MyRouter1 myRouter1) {
        this.mMyRouter1 = myRouter1;
    }
    public void handleAction(String action, Object args) {
        if ("router1_action1".equals(action)) {
            String text = String.valueOf(args);
            mMyRouter1.handleRouter1Action1(text);
        } else if ("router1_action2".equals(action)) {
            mMyRouter1.handleRouter1Action2(args);
        }
    }
}

