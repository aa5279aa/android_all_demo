package com.common.ui.demoshow.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.ui.R;
import com.common.ui.view.widget.CommonButton;

/**
 * Created by lxl on 2017/5/25.
 */
public class CommonUIButtonFragment extends CommonUIBaseFragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCommonButton1();
        addCommonButton2();
    }


    @Override
    public String getPageTitle() {
        return "UI按钮样式库";
    }

    public void addCommonButton1() {
        addTitle("Button，背景样式通过参数来设置，不再通过xml");

        CommonButton button = new CommonButton(getContext());
        button.setText("button-点击变色");
        CommonButton.Model model = new CommonButton.Model();
        model.resourceBG = R.drawable.original_button_selector;

        int[] enalbe = new int[]{android.R.attr.state_enabled};
        int[] colors = new int[]{getContext().getResources().getColor(R.color.color_333333), getContext().getResources().getColor(R.color.color_666666)};

        model.stateList = new ColorStateList(new int[][]{enalbe, new int[]{}}, colors);
        button.bindData(model);

        mContainer.addView(button);
        addLine();
    }

    public void addCommonButton2() {
        addTitle(" ");
        CommonButton button = new CommonButton(getContext());
        button.setText("button-固定色");
        CommonButton.Model model = new CommonButton.Model();
        model.resourceBG = R.drawable.original_button_selector;
        model.color = getContext().getResources().getColor(R.color.color_333333);
        button.bindData(model);
        mContainer.addView(button);
        addLine();
    }
}
