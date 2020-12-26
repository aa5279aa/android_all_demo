package com.common.ui.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.Button;

/**
 * 这个类其实没啥用，先留着，以后bg的xml转代码
 */
public class CommonButton extends Button {

    public CommonButton(Context context) {
        super(context);
    }


    public void bindData(Model model) {
        setBackgroundResource(model.resourceBG);
        if (model.stateList == null) {
            setTextColor(model.color);
        } else {
            setTextColor(model.stateList);
        }

    }

    public static class Model {
        public int resourceBG;
        public ColorStateList stateList;
        public int color;
    }


}
