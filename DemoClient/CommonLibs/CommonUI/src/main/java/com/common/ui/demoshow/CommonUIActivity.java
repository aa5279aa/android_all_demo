package com.common.ui.demoshow;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.common.ui.R;
import com.common.ui.demoshow.fragment.CommonUIBannerFragment;
import com.common.ui.demoshow.fragment.CommonUIButtonFragment;
import com.common.ui.demoshow.fragment.CommonUIDialogFragment;
import com.common.ui.view.bar.CommonInfoBar;
import com.common.util.DeviceUtil;

/**
 * Created by lxl on 2017/5/23.
 */
public class CommonUIActivity extends CommonUIBaseActivity implements View.OnClickListener {

    private final int UI_BUTTON_ID = 1;
    private final int UI_BANNER_ID = 2;
    private final int UI_FILTER_ID = 3;
    private final int UI_TITLE_ID = 4;
    private final int UI_OTHER_ID = 5;
    private final int UI_TOASTY_ID = 6;
    private final int UI_TEXTVIEW_ID = 7;
    private final int UI_DIALOG_ID = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer.addView(createCommonInfoBar(UI_BUTTON_ID, "按钮类型"));
        mContainer.addView(createCommonInfoBar(UI_BANNER_ID, "通栏类型"));
        mContainer.addView(createCommonInfoBar(UI_FILTER_ID, "筛选类型"));
        mContainer.addView(createCommonInfoBar(UI_TITLE_ID, "头部类型"));
        mContainer.addView(createCommonInfoBar(UI_OTHER_ID, "其余类型"));
        mContainer.addView(createCommonInfoBar(UI_TOASTY_ID, "toasty"));
        mContainer.addView(createCommonInfoBar(UI_TEXTVIEW_ID, "文本类型"));
        mContainer.addView(createCommonInfoBar(UI_DIALOG_ID, "dialog"));
    }

    @Override
    public String getPageTitle() {
        return "UI样式库";
    }

    public CommonInfoBar createCommonInfoBar(int id, String name) {
        CommonInfoBar.CommonInfoBarStyleControl control = new CommonInfoBar.CommonInfoBarStyleControl();
        control.mTitleText = name;
        control.mHasArrow = true;
        control.mTitleStyle = R.style.text_15_000000;
        CommonInfoBar CommonInfoBar = new CommonInfoBar(this, null, 0, control);
        CommonInfoBar.setId(id);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, DeviceUtil.getPixelFromDip(this, 40));
        lp.leftMargin = DeviceUtil.getPixelFromDip(this, 20);

        CommonInfoBar.setLayoutParams(lp);
        CommonInfoBar.setOnClickListener(this);
        return CommonInfoBar;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Fragment fragment = null;
        if (id == UI_BUTTON_ID) {
            fragment = new CommonUIButtonFragment();
        } else if (id == UI_BANNER_ID) {
            fragment = new CommonUIBannerFragment();
        } else if (id == UI_DIALOG_ID) {
            fragment = new CommonUIDialogFragment();
        }
        if (fragment == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}