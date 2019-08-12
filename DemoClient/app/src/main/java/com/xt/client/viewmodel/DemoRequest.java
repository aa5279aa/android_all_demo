package com.xt.client.viewmodel;


import com.xt.client.annotations.ProtoAnno;
import com.xt.client.viewmodel.base.BasePBModel;

public class DemoRequest extends BasePBModel {

    @ProtoAnno(tag = 1, type = ProtoAnno.Label.INT, isArray = false)
    public int valueInt;

    @ProtoAnno(tag = 2, type = ProtoAnno.Label.LONG, isArray = false)
    public long valueInt64;

    @ProtoAnno(tag = 3, type = ProtoAnno.Label.STRING, isArray = false)
    public String valueString;

    public DemoRequest() {

    }

}
