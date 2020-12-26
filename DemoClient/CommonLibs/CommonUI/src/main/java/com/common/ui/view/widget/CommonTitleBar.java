package com.common.ui.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.ui.R;
import com.common.util.StringUtil;
import com.common.util.UIShowUtil;


/**
 * Created by yanglei on 2018/7/13.
 */

public class CommonTitleBar extends RelativeLayout implements View.OnClickListener {
    Activity activity;

    String mTitleText;

    public CommonTitleBar(Context context) {
        this(context, null, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.common_title_bar, this);
        activity = (Activity) context;
        initFromAttributes(context, attrs);
        initView();
        initListener();
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
            this.mTitleText = a.getString(R.styleable.CommonTitleBar_common_title);
            a.recycle();
        }
    }


    private void initListener() {
        findViewById(R.id.common_tool_back).setOnClickListener(this);
        findViewById(R.id.common_tool_backlastpage).setOnClickListener(this);
        findViewById(R.id.common_tool_title).setOnClickListener(this);
    }

    private void initView() {
        setBackgroundColor(Color.parseColor("#099fde"));
        if (!StringUtil.emptyOrNull(mTitleText)) {
            UIShowUtil.showText((TextView) findViewById(R.id.common_tool_title), mTitleText);
        }
    }

    public void setTitleText(String titleText) {
        if (!StringUtil.emptyOrNull(titleText)) {
            UIShowUtil.showText((TextView) findViewById(R.id.common_tool_title), titleText);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.common_tool_back) {
            activity.finish();
        } else if (id == R.id.common_tool_backlastpage) {

            back2LastPage();
        } else if (id == R.id.common_tool_title) {

        }
    }

    //返回到进入debug界面之前的page
    public void back2LastPage() {

    }


}
