package com.xt.client.model;

import com.xt.client.annotations.CountAnno;

public class CountModel {

    @CountAnno(count = 1)
    public int num1;

    @CountAnno(count = 10)
    public int num2;

    @CountAnno(count = 100)
    public int num3;

    public int num4;

    @CountAnno(count = 1, type = CountAnno.Label.SINGLE)
    public String str1;

    @CountAnno(count = 1, type = CountAnno.Label.DOUBLE)
    public String str2;

    @CountAnno(count = 1, type = CountAnno.Label.THRICE)
    public String str3;

    public String str4;
}
