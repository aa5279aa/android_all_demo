package com.common.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.common.ui.R;
import com.common.ui.config.CommonUIConfig;
import com.common.ui.model.GuideModel;
import com.common.util.DeviceUtil;
import com.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {

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
        setContentView(R.layout.common_guide_activity);
        initData();
        initView();
        initViewPager();
        initDots();
    }


    public void initData() {
        Bundle extras = getIntent().getExtras();
        String tt = extras.getString(CommonUIConfig.TEST);
        mGuideModel = getIntent().getParcelableExtra(CommonUIConfig.GUIDEMOEL);
    }

    //接口ViewPager.OnPageChangeListener需要重写的三个方法
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) { //当滚动页面为当前页面时
        if (mDotList == null || mDotList.length == 0) {
            return;
        }
        setCurrentDotPosition(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void setCurrentDotPosition(int i) {
        mDotList[i].setEnabled(true);
        mDotList[mLastPosition].setEnabled(false);
        mLastPosition = i;
    }

    @Override
    public void onClick(View v) {
        startHomeActivity();
    }


    class MyPagerAdapter extends PagerAdapter {  //滑动页的适配器

        private List<View> mImageViewList;
        private Context mContext;

        MyPagerAdapter(List<View> list, Context context) {
            super();
            mImageViewList = list;
            mContext = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mImageViewList != null) {
                if (mImageViewList.size() > 0) {
                    View child = mImageViewList.get(position);
                    container.addView(child);
                    return child;
                }
            }
            return null;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (mImageViewList != null) {
                if (mImageViewList.size() > 0) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            container.removeView(mImageViewList.get(position));
                        }
                    });
                }
            }
        }

        @Override
        public int getCount() {
            if (mImageViewList != null) {
                return mImageViewList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private void initView() { //给引导页面布局初始化
        LayoutInflater inflater = LayoutInflater.from(this);
        View inflate;
        int length = mGuideModel.imgResourceIds.length;
        for (int i = 0; i < length; i++) {
            int resourceId = mGuideModel.imgResourceIds[i];
            inflate = inflater.inflate(R.layout.common_guide_layout, null);
            inflate.setBackgroundResource(resourceId);
            TextView startButton = inflate.findViewById(R.id.iv_start);
            if (i != (length - 1)) {
                startButton.setVisibility(View.GONE);
            } else {
                if (!StringUtil.emptyOrNull(mGuideModel.jumpButtonText)) {
                    startButton.setText(mGuideModel.jumpButtonText);
                }
                startButton.setBackgroundResource(mGuideModel.jumpButtonResId);
                startButton.setVisibility(View.VISIBLE);
                startButton.setOnClickListener(this);
            }
            mViewList.add(inflate);
        }
    }

    private void initViewPager() {   //初始化引导页面
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MyPagerAdapter adapter = new MyPagerAdapter(mViewList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initDots() {  //初始化下面三个小点
        if (!mGuideModel.isShowDots) {
            return;
        }
        LinearLayout dotsLayout = findViewById(R.id.ll_dots_layout);
        mDotList = new ImageView[mViewList.size()];
        for (int i = 0; i < mViewList.size(); i++) {
            mDotList[i] = new ImageView(this);
            mDotList[i].setBackgroundResource(R.drawable.original_dot);
            mDotList[i].setEnabled(false);
            dotsLayout.addView(mDotList[i]);
        }
        mLastPosition = 0;
        mDotList[0].setEnabled(true);
    }

    private void startHomeActivity() {
        int pixelFromDip = DeviceUtil.getPixelFromDip(this, 1);
//        String className = mGuideModel.className;
//        try {
//            Class<?> aClass = Class.forName(className);
//            Intent intent = new Intent(GuideActivity.this, aClass);
//            startActivity(intent);
//            finish();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

}