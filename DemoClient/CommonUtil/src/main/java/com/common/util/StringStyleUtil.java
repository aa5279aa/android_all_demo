package com.common.util;


import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;

/**
 * @author lxl
 * 负责带UI的字符串处理
 */
public final class StringStyleUtil {

    public static SpannableString makeStyledStringByColorId(Context context, String content, int colorId) {
        SpannableString string = new SpannableString(content);
        string.setSpan(new ForegroundColorSpan(colorId), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }


    public static SpannableString makeStyledString(Context context, String content, int colorId, int textSize) {
        SpannableString string = new SpannableString(content);
        string.setSpan(new ForegroundColorSpan(colorId), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        string.setSpan(new AbsoluteSizeSpan(textSize), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static SpannableString makeStyledStringByStyleId(Context context, String content, int style) {
        SpannableString string = new SpannableString(content);
        string.setSpan(new TextAppearanceSpan(context, style), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static SpannableString makeIconString(Context context, String content, int insertIndex, int resId) {
        SpannableString span = new SpannableString(content);
        ImageSpan image = new ImageSpan(context, resId, DynamicDrawableSpan.ALIGN_BOTTOM);
        span.setSpan(image, insertIndex, insertIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
