package com.common.ui.demoshow.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.common.ui.R;
import com.common.ui.view.widget.CommonTitleBar;

public abstract class CommonUIBaseFragment extends Fragment {

    LinearLayout mContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_ui_layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setClickable(true);
        initView(view);
    }

    private void initView(View view) {
        mContainer = (LinearLayout) view.findViewById(R.id.common_ui_container);
        CommonTitleBar titleBar = (CommonTitleBar) view.findViewById(R.id.title_view);
        titleBar.setTitleText(getPageTitle());
    }

    public abstract String getPageTitle();

    protected void addLine() {
        View line = new View(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 1);
        lp.topMargin = 60;
        line.setBackgroundColor(Color.BLACK);
        mContainer.addView(line, lp);
    }

    protected void addTitle(String titleStr) {
        TextView title = new TextView(getContext());
        title.setTextColor(Color.RED);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = 60;
        title.setText(titleStr);
        mContainer.addView(title, lp);
    }
}
