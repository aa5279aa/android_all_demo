package com.common.ui.demoshow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.common.ui.R;
import com.common.ui.view.widget.CommonTitleBar;

public abstract class CommonUIBaseActivity extends FragmentActivity {

    LinearLayout mContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_ui_layout);
        initView();
    }

    private void initView() {
        mContainer = (LinearLayout) findViewById(R.id.common_ui_container);
        CommonTitleBar titleBar = (CommonTitleBar) findViewById(R.id.title_view);
        titleBar.setTitleText(getPageTitle());
    }

    public abstract String getPageTitle();

    protected void addLine() {
        View line = new View(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 1);
        lp.topMargin = 60;
        line.setBackgroundColor(Color.BLACK);
        mContainer.addView(line, lp);
    }

    protected void addTitle(String titleStr) {
        TextView title = new TextView(this);
        title.setTextColor(Color.RED);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = 60;
        title.setText(titleStr);
        mContainer.addView(title, lp);
    }
}
