package com.common.util;

import java.text.DecimalFormat;

/**
 * 数值工具类
 */
public class NumberUtil {

    /**
     *  除法运算，保留小数
     *
     * @param denominator 被除数 分母
     * @param numerator   除数 分子
     * @return 商
     */
    public static String toFloat(int denominator, int numerator) {
        DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
        return df.format((float) denominator / numerator);
    }

}