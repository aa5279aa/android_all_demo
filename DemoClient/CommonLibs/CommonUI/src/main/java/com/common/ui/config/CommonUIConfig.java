package com.common.ui.config;

import android.content.Context;

import com.common.util.DeviceUtil;

public class CommonUIConfig {
    public static final String SPNAME = "CommonUIConfig";
    public static final String SPVALUE_IS_FRIST = "CommonUIConfig_isFirst";

    public static final String GUIDEMOEL = "guideModel";
    public static final String TEST = "TEST";

    public static boolean isPad = false;

    public static void init(Context context) {
        isPad = DeviceUtil.isPad(context);
    }


}
