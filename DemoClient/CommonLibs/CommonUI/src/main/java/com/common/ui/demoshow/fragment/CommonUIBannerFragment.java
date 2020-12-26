package com.common.ui.demoshow.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.ui.R;
import com.common.ui.view.bar.CommonInfoBar;

/**
 * Created by lxl on 2017/5/25.
 */
public class CommonUIBannerFragment extends CommonUIBaseFragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCommonInfoBar1();
    }


    @Override
    public String getPageTitle() {
        return "UI通栏样式库";
    }

    private void addCommonInfoBar1() {
        addTitle("CommonInfoBar_使用类型1，包含所有数据");
        CommonInfoBar.CommonInfoBarStyleControl control = new CommonInfoBar.CommonInfoBarStyleControl();
        control.mTitleText = "这是标题";
        control.mTitleStyle = R.style.text_12_999999;

        control.mValueText = "这里是内容";
        control.mValueStyle = R.style.text_14_333333;


        control.mHintText = "默认描述性文案";
        control.mHintStyle = R.style.text_12_333333;

        control.mHasArrow = true;

        CommonInfoBar bar = new CommonInfoBar(getContext(), null, 0, control);
        mContainer.addView(bar);
        addLine();
    }


}
