package com.common.ui.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.common.ui.R;

/**
    @author bguang
 */
public class CommonStateButton extends Button {

    private int textColorNormal, textColorUnable, textColorPressed;
    private Drawable bgNormal, bgUnable, bgPressed;
    private float roundRadius;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonStateButton(Context context) {
        super(context);
        init(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonStateButton);
            textColorNormal = typedArray.getColor(R.styleable.CommonStateButton_text_color_normal, getResColor(R.color.color_ffffff));
            textColorUnable = typedArray.getColor(R.styleable.CommonStateButton_text_color_unable, getResColor(R.color.color_BCBCBC));
            textColorPressed = typedArray.getColor(R.styleable.CommonStateButton_text_color_pressed, getResColor(R.color.color_ffffff));

            bgNormal = typedArray.getDrawable(R.styleable.CommonStateButton_bg_normal);
            bgUnable = typedArray.getDrawable(R.styleable.CommonStateButton_bg_unable);
            bgPressed = typedArray.getDrawable(R.styleable.CommonStateButton_bg_pressed);

            roundRadius = typedArray.getDimension(R.styleable.CommonStateButton_btn_round_radius, 0);
            typedArray.recycle();
        }else {
            bgNormal = context.getDrawable(R.drawable.common_button_selector_blue);
            bgUnable = context.getDrawable(R.drawable.common_button_selector_gray);
            bgPressed = context.getDrawable(R.drawable.common_button_selector_blue);

            textColorNormal = getResColor(R.color.color_ffffff);
            textColorPressed = getResColor(R.color.color_BCBCBC);
            textColorUnable = getResColor(R.color.color_ffffff);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context, AttributeSet attrs) {
        getAttrs(context, attrs);

        setClickable(true);
        StateListDrawable drawable = new StateListDrawable();
        GradientDrawable gdEnableBg = new GradientDrawable();//创建drawable
        gdEnableBg = (GradientDrawable) bgNormal;
        gdEnableBg.setCornerRadius(roundRadius);

        GradientDrawable gdUnEnableBg = new GradientDrawable();//创建drawable
        gdUnEnableBg = (GradientDrawable) bgUnable;
        gdUnEnableBg.setCornerRadius(roundRadius);

        GradientDrawable gdPressedBg = new GradientDrawable();//创建drawable
        gdUnEnableBg = (GradientDrawable) bgPressed;
        gdPressedBg.setCornerRadius(roundRadius);

        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, gdPressedBg);
        drawable.addState(new int[]{android.R.attr.state_enabled}, gdEnableBg);
        drawable.addState(new int[]{}, gdUnEnableBg);
        this.setBackground(drawable);

        setTextColor(createColorStateList(textColorPressed, textColorNormal, textColorUnable));
        setSingleLine(true);
    }
    /**
     * 对TextView设置不同状态时其文字颜色。
     */
    private ColorStateList createColorStateList(int pressed, int enabled, int unEnabled) {
        int[] colors = new int[]{pressed, enabled, unEnabled};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    private int getResColor(@ColorRes int resColor) {
        return getContext().getResources().getColor(resColor);
    }

    @Override
    public void setOnClickListener(@Nullable final View.OnClickListener l) {
        View.OnClickListener clickListener = v -> l.onClick(v);
        super.setOnClickListener(clickListener);
    }
}
