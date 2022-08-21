package com.xt.client.function.route;


/**
 * 模式路由处理类
 */
public interface RouterBase {

    /**
     * @param action 功能
     * @param args   参数
     */
    public void handleAction(String action, Object args);

}
