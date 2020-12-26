package com.common.ui.view.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.common.ui.R;
import com.common.util.DeviceUtil;


/**
 * @author lxl
 */
public class CommonSettingSwitchBar extends CommonInfoBar implements CompoundButton.OnCheckedChangeListener {

    private CommonSimpleSwitch mSwitchBar;
    private CompoundButton.OnCheckedChangeListener mListener;

    public CommonSettingSwitchBar(Context context) {
        this(context, null);
    }

    public CommonSettingSwitchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setViewAttr(attrs);
    }

    public void setViewAttr(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CommonSettingSwitchBar);
        Drawable drawable = a.getDrawable(R.styleable.CommonSettingSwitchBar_common_drawable_src);
        int colorId = a.getResourceId(R.styleable.CommonSettingSwitchBar_common_title_color, 0);
        int sizeId = a.getResourceId(R.styleable.CommonSettingSwitchBar_common_title_size, 0);
        int width = (int) a.getDimension(R.styleable.CommonSettingSwitchBar_common_drawable_width, LayoutParams.WRAP_CONTENT);
        int height = (int) a.getDimension(R.styleable.CommonSettingSwitchBar_common_drawable_height, LayoutParams.WRAP_CONTENT);
        a.recycle();
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        mValueText.setTypeface(Typeface.DEFAULT_BOLD);
        mValueText.setLayoutParams(labelParams);
        mValueText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        mValueText.setLineSpacing(3.4f, 1.0f);
//        mValueText.setPadding(0, 0, DeviceUtil.getPixelFromDip(getContext(), 10.0f), 0);

        LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(width, height);
        valueParams.gravity = Gravity.CENTER_VERTICAL;
        mSwitchBar = new CommonSimpleSwitch(getContext());
        if (drawable != null) {
            mSwitchBar.setDrawable(drawable, width, height);
        }
        if (colorId != 0) {
            mTitleText.setTextColor(getResources().getColorStateList(colorId));
        }
        if (sizeId != 0) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(sizeId));
        }
        mSwitchBar.setOnCheckedChangeListener(this);
        addView(mSwitchBar, valueParams);
        setClickable(false);
        setFocusable(false);
    }


    /**
     * @param listener 通知对象
     * @see CompoundButton.OnCheckedChangeListener
     */
    public void setOnCheckdChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mListener = listener;
    }

    /**
     * 设置switcher的有效化
     *
     * @param enable
     */
    public void setSwitchEnable(boolean enable) {
        if (mSwitchBar != null) {
            mSwitchBar.setFocusable(enable);
            mSwitchBar.setClickable(enable);
            mSwitchBar.setEnabled(enable);
        }
    }

    /**
     * 设置SwitchBar的On/Off状态
     *
     * @param checked true - On状态， false - Off状态
     */
    public void setSwitchChecked(boolean checked) {
        if (mSwitchBar != null) {
            mSwitchBar.setChecked(checked);
        }
    }

    /**
     * 获取SwitchBar的On/Off状态
     *
     * @return true - On状态， false - Off状态
     */
    public boolean isSwitchChecked() {
        if (mSwitchBar != null) {
            return mSwitchBar.isChecked();
        }
        return false;
    }

    /**
     * 设置Switch的On文本
     *
     * @param textOn On显示的文本
     */
    @Deprecated
    public void setSwitchTextOn(CharSequence textOn) {
        if (mSwitchBar != null) {
            // mSwitchBar.setTextOn(textOn);
        }
    }

    /**
     * 设置Switch的Off文本
     */
    @Deprecated
    public void setSwitchTextOff(CharSequence textOff) {
        if (mSwitchBar != null) {
            // mSwitchBar.setTextOff(textOff);
        }
    }


    public CompoundButton getSwitchBar() {
        return mSwitchBar;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mListener != null) {
            mListener.onCheckedChanged(buttonView, isChecked);
        }
        if (isChecked) {
            mTitleText.setSelected(true);
        } else {
            mTitleText.setSelected(false);
        }

    }
}
