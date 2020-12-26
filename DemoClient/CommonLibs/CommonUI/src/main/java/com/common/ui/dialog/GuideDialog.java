package com.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.common.ui.R;
import com.common.ui.model.GuideModel;
import com.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用dialog
 */
public class GuideDialog extends Dialog implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mRoot;
    private ViewPager mViewPager;

    private List<View> mViewList = new ArrayList<>();
    private MyPagerAdapter mAdapter;


    public GuideDialog(Context context) {
        super(context, R.style.NativeInsertDialog);
        initView();
        initViewPager();
    }

    private void initView() {
        mRoot = View.inflate(getContext(), R.layout.common_dialog_guide_layout, null);
        mViewPager = mRoot.findViewById(R.id.viewpager);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(mRoot, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    private void initViewPager() {   //初始化引导页面
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    public void bindData(GuideModel dialogModel) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View inflate;
        int length = dialogModel.imgResourceIds.length;
        for (int i = 0; i < length; i++) {
            int resourceId = dialogModel.imgResourceIds[i];
            inflate = inflater.inflate(R.layout.common_dialog_guide_item, null);
            inflate.setBackgroundResource(resourceId);
            TextView startButton = inflate.findViewById(R.id.iv_start);
            if (i != (length - 1)) {
                startButton.setVisibility(View.GONE);
            } else {
                if (!StringUtil.emptyOrNull(dialogModel.jumpButtonText)) {
                    startButton.setText(dialogModel.jumpButtonText);
                }
                startButton.setBackgroundResource(dialogModel.jumpButtonResId);
                startButton.setVisibility(View.VISIBLE);
                startButton.setOnClickListener(this);
            }
            mViewList.add(inflate);
        }
        mAdapter = new MyPagerAdapter(mViewList, getContext());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public interface ClickCallBack {
        void clickButton(int position, View view);
    }

    @Override
    public void show() {
        super.show();
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
            if (mImageViewList != null && mImageViewList.size() > 0) {
                View child = mImageViewList.get(position);
                container.addView(child);
                return child;
            }
            return null;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (mImageViewList != null && mImageViewList.size() > 0) {
                container.removeView(mImageViewList.get(position));

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
}
