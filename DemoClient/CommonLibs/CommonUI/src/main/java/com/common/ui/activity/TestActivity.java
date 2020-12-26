package com.common.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.common.ui.R;
import com.common.ui.model.GuideModel;
import com.common.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TestActivity extends Activity implements View.OnClickListener {

    private List<View> mViewList = new ArrayList<>();
    private ViewPager mViewPager;    //滑动页
    private ImageView[] mDotList;
    private int mLastPosition; //记录选中页面的位置
    private GuideModel mGuideModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiFlags |= 0x00001000;
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        setContentView(R.layout.common_guide_layout);
        findViewById(R.id.iv_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int[] screenSize = DeviceUtil.getScreenSize(this);
        int pixelFromDip = DeviceUtil.getPixelFromDip(this, 1);
        Log.i("lxltest", "pixelFromDip:" + pixelFromDip);
    }
}