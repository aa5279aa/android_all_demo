package com.common.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.common.ui.activity.GuideActivity;
import com.common.ui.activity.TestActivity;
import com.common.ui.config.CommonUIConfig;
import com.common.ui.dialog.GuideDialog;
import com.common.ui.model.GuideModel;
import com.common.util.DeviceUtil;

public class GuideHelper {


    public static void jumpGuidePageOrNot(Context context, Class<? extends Activity> activityClass, GuideModel guideModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonUIConfig.SPNAME, 0);
        Intent intent = new Intent();
        if (sharedPreferences.getBoolean(CommonUIConfig.SPVALUE_IS_FRIST, false)) {
            intent.setClass(context, activityClass);
        } else {
            sharedPreferences.edit().putBoolean(CommonUIConfig.SPVALUE_IS_FRIST, true).apply();
            guideModel.className = activityClass.getName();
            intent.setClass(context, activityClass);
            intent.putExtra(CommonUIConfig.GUIDEMOEL, guideModel);
        }
        context.startActivity(intent);
    }

    public static void showGuideDialogOrNot(Context context, GuideModel guideModel) {
        String SP_VALUE = CommonUIConfig.SPVALUE_IS_FRIST + DeviceUtil.getVersionName(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(CommonUIConfig.SPNAME, 0);
        if (sharedPreferences.getBoolean(SP_VALUE , false)) {
            return;
        }
        sharedPreferences.edit().putBoolean(SP_VALUE, true).apply();
        GuideDialog guideDialog = new GuideDialog(context);
        guideDialog.bindData(guideModel);
        guideDialog.show();

    }


    public static GuideModel buildGuideModel(int[] imgResourceIds, boolean isShowDots, String jumpButtonText, int jumpButtonResId) {
        GuideModel guideModel = new GuideModel();
        guideModel.imgResourceIds = imgResourceIds;
        guideModel.isShowDots = isShowDots;
        guideModel.jumpButtonText = jumpButtonText;
        guideModel.jumpButtonResId = jumpButtonResId;
        return guideModel;
    }


}
