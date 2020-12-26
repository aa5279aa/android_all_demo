package com.common.ui.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.common.ui.R;

public class CommonDrawableTextView extends LinearLayout {
    private final int ICON_WIDTH_DEFAULT = getResources().getDimensionPixelSize(R.dimen.dip14);
    private final int ICON_HEIGHT_DEFAULT = getResources().getDimensionPixelSize(R.dimen.dip14);
    private final int ICON_MARGIN_RIGTH_DEFAULT = getResources().getDimensionPixelSize(R.dimen.dip4);
    private final int TEXT_SIZE_DEFAULT = getResources().getDimensionPixelSize(R.dimen.dip12);
    private final int TEXT_COLOR_DEFAULT = getResources().getColor(R.color.color_333333);
    private ImageView imageView;
    private TextView textView;

    public CommonDrawableTextView(Context context) {
        super(context, null);
    }

    public CommonDrawableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = li.inflate(R.layout.common_back_button_layout, this);
        imageView = view.findViewById(R.id.iv_back);
        textView = view.findViewById(R.id.tv_back);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonDrawableTextView);
        Drawable drawable = typedArray.getDrawable(R.styleable.CommonDrawableTextView_common_drawable_src_c);
//        int drawableWidth = typedArray.getDimensionPixelSize(R.styleable.CommonDrawableTextView_common_drawable_width_c, ICON_WIDTH_DEFAULT);
//        int drawableHeight = typedArray.getDimensionPixelSize(R.styleable.CommonDrawableTextView_common_drawable_height_c, ICON_HEIGHT_DEFAULT);
//        int drawableMarginRight = typedArray.getDimensionPixelSize(R.styleable.CommonDrawableTextView_common_drawable_margin_right, ICON_MARGIN_RIGTH_DEFAULT);

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(drawableWidth, drawableHeight);
//            params.rightMargin = drawableMarginRight;
//            imageView.setLayoutParams(params);
        }

        String text = typedArray.getString(R.styleable.CommonDrawableTextView_common_text);
//        int textSize = typedArray.getDimensionPixelSize(R.styleable.CommonDrawableTextView_common_text_size, TEXT_SIZE_DEFAULT);
        int textColor = typedArray.getColor(R.styleable.CommonDrawableTextView_common_text_color, TEXT_COLOR_DEFAULT);
        int textType = typedArray.getInt(R.styleable.CommonDrawableTextView_common_text_style, Typeface.NORMAL);

        textView.setText(text);
        textView.setTextColor(textColor);
//        textView.setTextSize(textSize);
        textView.setTypeface(Typeface.defaultFromStyle(textType));

        typedArray.recycle();
    }


}
