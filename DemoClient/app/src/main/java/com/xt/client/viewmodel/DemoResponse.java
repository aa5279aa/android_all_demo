package com.xt.client.viewmodel;


import com.xt.client.annotations.ProtoAnno;
import com.xt.client.viewmodel.base.BasePBModel;

public class DemoResponse extends BasePBModel {

    @ProtoAnno(tag = 1, type = ProtoAnno.Label.BOOLEAN, isArray = false)
    public boolean result;// 响应结果值

    @ProtoAnno(tag = 2, type = ProtoAnno.Label.STRING, isArray = false)
    public String resultMessage;// 响应描述

    @ProtoAnno(tag = 3, type = ProtoAnno.Label.INT, isArray = false)
    public int resultCode;// 响应结果

}
